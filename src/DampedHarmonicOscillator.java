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

    public DampedHarmonicOscillator(double tf){
        this.p = new Particle(1,1,initr,0,initv,0,0,0,m); //El objeto no esta acelerado inicialmente
        this.ti = 0;
        this.tf = tf;
    }

    public double Solution(double t){
        return Math.pow(Math.E,-(gamma/(2*m))*t)*Math.cos(Math.sqrt((k/m)-((gamma*gamma)/(4*m*m)))*t);
    }

    public double getAcceleration(Particle p){
        return getForce(p)/m;
    }

    public double getAccelerationGPR5(double predictedX, double predictedV){
        return -k*predictedX -gamma*predictedV;
    }

    public double getForce(Particle p){
        return -k*p.x -gamma*p.vx;
    }

    public void beeman(double time){
        p.next = new Particle(m, 1);

        //Seguir esto. Esta en la diapo 19
        p.next.x = p.x + p.vx*time + (2.0/3.0)*p.ax*time*time - (1.0/6.0)*p.ax*time*time;

        //Version predictor-corrector
        Particle predicted = new Particle(p.next.x, 1, m);
        predicted.vx = p.vx + (3.0/2.0)*p.ax*time-(1.0/2.0)*p.ax*time;
        p.next.ax = getAcceleration(predicted);

        p.next.vx = p.vx - (1.0/3.0)*p.next.ax*time + (5.0/6.0)*p.ax*time - (1.0/6.0)*p.ax*time;

        p.previous.x  = p.x;
        p.previous.vx = p.vx;
        p.previous.ax = p.ax;

        p.x = p.next.x;
        p.vx = p.next.vx;
        p.ax = p.next.ax;

    }

    public void gearPredictorCorrector5(double time){

        //Primero calculo los r'
        p.ax = getAcceleration(p);
        double aprimera = -(k/m)*p.vx - (gamma/m)*p.ax;
        double asegunda = -(k/m)*p.ax - (gamma/m)*aprimera;
        double atercera =  -(k/m)*aprimera - (gamma/m)*asegunda;   //Todo esto vale para el paso inicial!! Dsps me baso en los predichos

        //Creo la nueva particula para setearle los r' predichos
        p.next = new Particle(m,1);


        //Predecir los r'
        Particle predicted = new Particle(0, 1, m);

        predicted.x = p.x + p.vx*time + p.ax*((time*time)/2) + aprimera*((time*time*time)/6) + asegunda*((time*time*time*time)/24) + atercera*((time*time*time*time)/120);
        predicted.vx = p.vx + p.ax*time + aprimera*((time*time)/2) + asegunda*((time*time*time)/6) + atercera*((time*time*time*time)/24);
        predicted.ax = p.ax + aprimera*time + asegunda*((time*time)/2) + atercera*((time*time*time)/6);
        double aprimeraPredicha = aprimera + asegunda*time + atercera*((time*time)/2);
        double asegundaPredicha = asegunda + atercera*time;
        double aterceraPredicha = atercera;  //Hace falta?

        //Calcular el factor de correcion DELTA R2
        double factorCorreccion = (getAccelerationGPR5(predicted.x, predicted.vx) - predicted.ax)*((time*time)/2);


        //Calcular los r' corregidos
        p.next.x = predicted.x + (3.0/16.0)*factorCorreccion;
        p.next.vx = predicted.vx + (251/360)*(factorCorreccion/time);
        p.next.ax = predicted.ax + (factorCorreccion/(time*time))*2;

        //Actualizo la particula
        p.previous.x  = p.x;
        p.previous.vx = p.vx;
        p.previous.ax = p.ax;

        p.x = p.next.x;
        p.vx = p.next.vx;
        p.ax = p.next.ax;
    }




}
