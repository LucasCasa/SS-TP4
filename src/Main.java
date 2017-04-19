/**
 * Created by lcasagrande on 19/04/17.
 */
public class Main {

    public static void main(String[] args) {
        Particle sun = new Particle(0,695700000,0,0,0,0,1.988e30);
        Particle earth = new Particle(1,6317000,1.391734353396533E11,	-0.571059040560652E11,10801.963811159256,27565.215006898345,5.972e24);
        Particle mars = new Particle(2,3389000,0.831483493435295E11,-1.914579540822006E11,23637.912321314047,11429.021426712032,6.4185e23);

        Particle rocket = new Particle(2,1,0,-1,0,0,2e5);
        //System.out.println(Particle.angle(p,p2)*180/Math.PI);
        System.out.println(earth.getGravityForces(sun));
    }
}

