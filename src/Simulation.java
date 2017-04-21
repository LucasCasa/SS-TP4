import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcasagrande on 19/04/17.
 */
public class Simulation {
    List<String> data = new ArrayList<>();
    static double dt = 5;
    List<Particle> objects;
    public Simulation(Particle e, Particle s, Particle m,Particle r){
       objects = new ArrayList<>();
       objects.add(s);
       objects.add(e);
       objects.add(m);
       objects.add(r);
       data = new ArrayList<>();

    }

    public List<String> simulate(int time){
        /*for(Particle t : objects) {
            Vector force = new Vector(0, 0);
            for (Particle o : objects) {
                if (!t.equals(o)) {
                    force.add(t.getGravityForces(o));
                }
            }
            t.advanceFirst(force);
        }*/
        for(double i = 0; i<time;i+=dt){
            updateObjects(i);
        }
        return data;
    }

    public void updateObjects(double time){
        if(((int)time % 43200) == 0){
            data.add("4\n4\n");
        }
            for(Particle t : objects){

            Vector force = new Vector(0,0);
            for(Particle o : objects){
                if(!t.equals(o)){
                    force.add(t.getGravityForces(o));
                }
            }
            t.updatePosition(force,dt);
            if(((int)time % 43200) == 0) {
                /*if(t.id == 1){
                    System.out.println((t.lastRx - t.getX()) / (dt));
                }*/
                data.add(t.x + "\t" + t.y + "\t" + (3.1 - t.id) * 1e9 + "\n");
            }
        }
        for(Particle p : objects){
            p.setNewPositions();
        }
        /*Particle e = objects.get(1);
        Particle m = objects.get(2);
        Vector fe = e.getGravityForces(objects.get(0));
        Vector fm = m.getGravityForces(objects.get(0));
        e.updatePosition(fe,dt);
        m.updatePosition(fm,dt);*/

    }
}
