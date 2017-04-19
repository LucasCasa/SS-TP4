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

    public void beeman(double time){
        p.next = new Particle(m, 1);

        //Seguir esto. Esta en la diapo 19
        p.next.x = p.x + p.vx*time + (2.0/3.0)*p.ax*time*time - (1.0/6.0)*p.ax*time*time;
    }




}
