import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by lcasagrande on 19/04/17.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        /*List<String> d = s.simulate(59356800);

        for(String line : d){
            fl.write(line);
        }
        fl.close();*/
        //s.getDt();
        for(int j = 10; j>=0;j-=1) {
            Particle sun = new Particle(0,695700000,0,0,0,0,1.988e30);
            Particle earth = new Particle(1,6317000,1.391734353396533E11,	-0.571059040560652E11,10801.9,27565.2,5.972e24);
            Particle mars = new Particle(2,3389000,0.831483493435295E11,-1.914579540822006E11,23637.9,11429.0,6.4185e23);
            Simulation s = new Simulation(earth,sun,mars);
            Simulation.dt = j;
            s.calculateDays(0, 686 * 3600 * 24);
            //s.simulate(365*24*3600);
            s.simulateStarting(0, 365 * 24 * 3600, 3200);
            FileWriter fl = new FileWriter("out.txt");
        /*for(List<Particle> days : s.days){
            fl.write("3\n" + s.days.indexOf(days) + "\n");
            for(Particle t : days){
                fl.write(t.x + "\t" + t.y + "\t" + ((4 - t.id)*1e9) + "\t" +1 + "\t" +1+"\n");
            }
        }*/
            for (String st : s.data) {
                fl.write(st);
            }
            fl.close();
            fl = new FileWriter("rocket" + (int)Simulation.dt + ".txt");
            for (String st : s.rocketData) {
                fl.write(st);
            }
            fl.close();
        }
    }
}

