import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by lcasagrande on 19/04/17.
 */
public class Main {

    public static void main(String[] args) throws IOException {

        Particle sun = new Particle(0,6e9,0,0,0,0,1.988e30);
        Particle earth = new Particle(1,4e9,1.391734353396533E11,	-0.571059040560652E11,10801.9,27565.2,5.972e24);
        Vector f = earth.getGravityForces(sun);
        Particle mars = new Particle(2,3e9,0.831483493435295E11,-1.914579540822006E11,23637.9,11429.0,6.4185e23);
        Simulation s = new Simulation(earth,sun,mars);
        s.calculateDays(686 * 3600 * 24);
        s.simulate(365*24*3600,3400,3400,1);
        s.simulateStarting(0, 365 * 24 * 3600, 50600);
        FileWriter fl = new FileWriter("out.txt");
            for (String st : s.data) {
                fl.write(st);
            }
            fl.close();
    }
}

