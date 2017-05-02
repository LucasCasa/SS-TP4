import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by ncastano on 19/04/17.
 */
public class DampedHarmonicOscillator {
    private static final double m = 70;
    private static final double k = Math.pow(10,4);
    private static final double gamma = 100;

    private static final double initr = 1;
    private static final double initv = -gamma / (2*m);

    private  double ti;
    private double tf;
    private Particle p;
    private GPR5Particle particle;

    public DampedHarmonicOscillator(double tf){
        this.p = new Particle(1,1,initr,0,initv,0,0,0,m); //El objeto no esta acelerado inicialmente
        this.particle = new GPR5Particle(1,1,initr,0,initv,m,k,gamma);
        this.ti = 0;
        this.tf = tf;

    }

    public double Solution(double t){
        return Math.pow(Math.E,-(gamma/(2*m))*t)*Math.cos(Math.sqrt((k/m)-((gamma*gamma)/(4*m*m)))*t);
    }

    public double getAcceleration(Particle p){
        return getForce(p)/m;
    }

    public double getAccelerationGPC5(double predictedX, double predictedV){
        return ((-k*predictedX -gamma*predictedV)/m);
    }

    public double getForce(Particle p){
        return -k*p.x -gamma*p.vx;
    }

    public void verlet(double dt) {

        p.next = new Particle(m,1);

        p.next.x = 2*p.x - p.previous.x + getAcceleration(p)*dt*dt;
        p.next.vx = ((p.x - p.previous.x)/ (2*dt));


        p.previous.x  = p.x;
        p.previous.vx = p.vx;
        p.previous.ax = p.ax;

        p.x = p.next.x;
        p.vx = p.next.vx;
        p.ax = getAcceleration(p.next);


    }

    public void velocityVerlet(double dt){
        p.next = new Particle(m,1);

        p.next.x = p.x + p.vx*dt + (dt*dt)*getAcceleration(p);
        double pasoIntermedio1 = p.vx + (getAcceleration(p)*(dt/2));
        p.next.vx = pasoIntermedio1;
        double pasoIntermedio2 = getAcceleration(p.next)*(dt/2);
        p.next.vx += pasoIntermedio2;

        p.previous.x  = p.x;
        p.previous.vx = p.vx;
        p.previous.ax = p.ax;

        p.x = p.next.x;
        p.vx = p.next.vx;
        p.ax = getAcceleration(p.next);

    }


    public void beeman(double time){
        p.next = new Particle(m, 1);

        //Seguir esto. Esta en la diapo 19
        p.next.x = p.x + p.vx*time + (2.0/3.0)*p.ax*time*time - (1.0/6.0)*p.ax*time*time;

        //Version predictor-corrector
        Particle predicted = new Particle(p.next.x, 1, m);
        predicted.vx = p.vx + (3.0/2.0)*p.ax*time-(1.0/2.0)*p.ax*time;
        p.next.ax = getAcceleration(predicted);

        p.next.vx = p.vx + (1.0/3.0)*p.next.ax*time + (5.0/6.0)*p.ax*time - (1.0/6.0)*p.ax*time;

        p.previous.x  = p.x;
        p.previous.vx = p.vx;
        p.previous.ax = p.ax;

        p.x = p.next.x;
        p.vx = p.next.vx;
        p.ax = getAcceleration(p.next);

    }

    public void gearPredictorCorrector5(double time){
        //Creo la nueva particula para setearle los r' predichos
        //particle.next = new GPR5Particle(m,1);


        //Predecir los r'
        Particle predicted = new GPR5Particle(0, 1, m);

        predicted.x = particle.x + particle.vx*time + particle.ax*((time*time)/2) + particle.aprimera*((time*time*time)/6) + particle.asegunda*((time*time*time*time)/24) + particle.atercera*((time*time*time*time)/120);
        predicted.vx = particle.vx + particle.ax*time + particle.aprimera*((time*time)/2) + particle.asegunda*((time*time*time)/6) + particle.atercera*((time*time*time*time)/24);
        predicted.ax = particle.ax + particle.aprimera*time + particle.asegunda*((time*time)/2) + particle.atercera*((time*time*time)/6);
        double aprimeraPredicha = particle.aprimera + particle.asegunda*time + particle.atercera*((time*time)/2);
        double asegundaPredicha = particle.asegunda + particle.atercera*time;
        double aterceraPredicha = particle.atercera;  //Hace falta?

        //Calcular el factor de correcion DELTA R2
        double factorCorreccion = (getAccelerationGPC5(predicted.x, predicted.vx) - predicted.ax)*((time*time)/2.0);

        //Calcular los r' corregidos
        particle.next.x = predicted.x + (3.0/16.0)*factorCorreccion;
        particle.next.vx = predicted.vx + (251.0/360.0)*(factorCorreccion/time);
        particle.next.ax = predicted.ax + (factorCorreccion/(time*time))*2;
        particle.next.aprimera=  aprimeraPredicha + (11/18.0)*(factorCorreccion/(time*time*time))*6;
        particle.next.asegunda = asegundaPredicha + (1/6.0)*(factorCorreccion/(time*time*time*time))*24;
        particle.next.atercera = aterceraPredicha + (1/60.0)*(factorCorreccion/(time*time*time*time*time))*120;

        //Actualizo la particula
        particle.previous.x  = particle.x;
        particle.previous.vx = particle.vx;
        particle.previous.ax = particle.ax;
        particle.previous.aprimera = particle.aprimera;
        particle.previous.asegunda = particle.asegunda;
        particle.previous.atercera = particle.atercera;


        particle.x = particle.next.x;
        particle.vx = particle.next.vx;
        particle.ax = particle.next.ax;
        particle.aprimera = particle.next.aprimera;
        particle.asegunda = particle.next.asegunda;
        particle.atercera = particle.next.atercera;

    }

    public void gearPredictorCorrector5V2(double time){
        System.out.println("ENTRE EN LA VERSION 2");

        //Condiciones iniciales
        particle.aprimera = -(k/m)*particle.vx - (gamma/m)*particle.ax;
        particle.asegunda = -(k/m)*particle.ax - (gamma/m)*particle.aprimera;;
        particle.atercera = -(k/m)*particle.aprimera - (gamma/m)*particle.asegunda;

        //Creo la nueva particula para setearle los r' predichos
        //particle.next = new GPR5Particle(m,1);

        //Predecir los r'
        Particle predicted = new GPR5Particle(0, 1, m);

        predicted.x = particle.x + particle.vx*time + particle.ax*((time*time)/2) + particle.aprimera*((time*time*time)/6) + particle.asegunda*((time*time*time*time)/24) + particle.atercera*((time*time*time*time)/120);
        predicted.vx = particle.vx + particle.ax*time + particle.aprimera*((time*time)/2) + particle.asegunda*((time*time*time)/6) + particle.atercera*((time*time*time*time)/24);
        predicted.ax = particle.ax + particle.aprimera*time + particle.asegunda*((time*time)/2) + particle.atercera*((time*time*time)/6);

        //Calcular el factor de correcion DELTA R2
        double factorCorreccion = (getAccelerationGPC5(predicted.x, predicted.vx) - predicted.ax)*((time*time)/2);

        //Calcular los r' corregidos
        particle.next.x = predicted.x + (3.0/16.0)*factorCorreccion;
        particle.next.vx = predicted.vx + (251.0/360.0)*(factorCorreccion/time);
        particle.next.ax = predicted.ax + (factorCorreccion/(time*time))*2;

        //Actualizo la particula
        particle.previous.x  = particle.x;
        particle.previous.vx = particle.vx;
        particle.previous.ax = particle.ax;

        particle.x = particle.next.x;
        particle.vx = particle.next.vx;
        particle.ax = getAccelerationGPC5(particle.next.x,particle.next.vx);//particle.next.ax;

    }



    public double eulerPosition(Particle p, double dt){
        return p.x + dt*p.vx + (((dt*dt)*getForce(p))/(2*p.mass));
    }

    public double eulerVelocity(Particle p, double dt){
        return p.vx + ((dt/p.mass)*getForce(p));
    }

    public void simulate(double dt, double dt2){
        int counter = 0;
        double difference = 0;
        double delta = 0;
        p.ax = getAcceleration(p);

        p.previous = new Particle(eulerPosition(p,-dt), eulerVelocity(p,-dt), 0, 1, m);
        p.previous.ax =  getAcceleration(p.previous);

        //particle.next = new GPR5Particle(m,1);
        //particle.previous = new GPR5Particle(initr, initv, 0, 1, m);// ----> para gearPredictorCorrector5
        //particle.previous.ax = getAccelerationGPC5(particle.x,particle.vx);
        //System.out.println("PARTICLE= " + particle.toString());
        //System.out.println("PARTICLE.PREVIOUS= "+ particle.previous.toString());

        System.out.println("SOLUCION DEL INTEGRADOR \t\t SOLUCION EXACTA");
        while(ti < tf){
            System.out.println(p.x + "\t\t" + Solution(ti));
            difference += Math.pow(p.x - Solution(ti),2);
            System.out.println("La diferencia es: "+ difference);

            velocityVerlet(dt);

            //Diapositiva 31
            if(dt2*delta <= ti){
                toFile(p.x, ti);
                delta++;
            }

            ti += dt;
            counter++;
        }
        System.out.println("ERROR: " +  difference/counter);
    }

    public void toFile(double position, double time){
        if(time == 0){
            try{
                PrintWriter pw = new PrintWriter("output.txt");
                pw.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output.txt", true)))) {
            out.write(time + "\t" + position +"\n");
            out.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args){
        DampedHarmonicOscillator oscillator = new DampedHarmonicOscillator(5);
        oscillator.simulate(0.001, 0.01);
    }

}
