import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcasagrande on 19/04/17.
 */
public class Simulation {
    List<String> data = new ArrayList<>();
    List<List<Particle>> days = new ArrayList<>();
    static double dt = 100;
    List<Particle> objects;
    double min = Double.MAX_VALUE;
    int dayOfLunch = 0;
    int speedOfLunch = 0;
    List<String> rocketData = new ArrayList<>();
    public Simulation(Particle e, Particle s, Particle m){
       objects = new ArrayList<>();
       objects.add(s);
       objects.add(e);
       objects.add(m);
       data = new ArrayList<>();

    }

    public void calculateDays(int start,double time){
        System.out.println("Calculating Positions on days");
        for(double i = 0; i<time;i+=dt){
            if( (i % 86400)  == 0){
                ArrayList<Particle> positions = new ArrayList<>();
                for(Particle o : objects){
                    positions.add(new Particle(o));
                }
                days.add(positions);
                System.out.print(" "+(i / (86400)));
            }
            updateObjects(i);
        }
        System.out.println("");
    }
    public List<String> simulate(int time){
        for(int i = 0; i<days.size();i++){
            List<Particle> pl = days.get(i);
            for(int k = 3200; k<=4500;k+=100) {
                objects.clear();
                for (Particle p : pl) {
                    objects.add(new Particle(p));
                }
                loadRocket(3700);
                System.out.println("Day " + i*7 + " : ");
                for (double j = 0; j < time; j += dt) {
                    updateObjects(j);
                    double dist = getDistanceToMars();
                    if (dist < min) {
                        min = dist;
                        dayOfLunch = i;
                        speedOfLunch = k;
                    }
                }
            }
        }
        System.out.println("DIA:" + dayOfLunch);
        System.out.println("VELOCIDAD: " + speedOfLunch);
        System.out.println("DISTANCIA:" + Math.sqrt(min));
        return data;
    }
    public void simulateStarting(int day,int time,int s){
        List<Particle> pl = days.get(day);
        objects.clear();
        for(Particle p : pl){
            objects.add(new Particle(p));
        }
        loadRocket(s);
        System.out.print("Day " + day + " : ");
        for(double j = 0; j<time;j+=dt){
            if((j % 86400) == 0){
                data.add("4\n"+j+"\n");
                for (Particle t : objects) {
                    data.add(t.x + "\t" + t.y + "\t" + t.radius /*((4 - t.id) * 1e9)*/ + "\t" + t.vx + "\t" + t.vy + "\t" + t.f.x+ "\t" + t.f.y+"\n");
                    if(t.id == 3){
                        rocketData.add(t.x + "\t" + t.y + "\n");
                    }
                }

            }
            updateObjects(j);
            if( (j % (time / 100))  == 0){
                System.out.print((j / (time / 100)) + " ");
            }

            double dist = getDistanceToMars();
                /*if(dist > lastDist){
                    valid = false;
                }*/
            if(dist < min){
                min = dist;
            }
        }
        System.out.println("");
        System.out.println(Math.sqrt(min));
    }
    private double getDistanceToMars() {
        double mx = objects.get(2).getX();
        double my = objects.get(2).getY();
        double rx = objects.get(3).getX();
        double ry = objects.get(3).getY();
        return (mx - rx)*(mx - rx) + (my - ry)*(my - ry);
    }

    public void loadRocket(double s){
        Particle sun = objects.get(0);
        Particle earth = objects.get(1);
        double module = 1500000 + 6317000;
        double dist = Math.sqrt(Particle.dist2(earth,sun));
        double ex = (earth.x - sun.x)/dist;
        double ey = (earth.y - sun.y)/dist;
        Vector add = new Vector(module*ex,module*ey);
        Vector speed = new Vector((7120 + s)*ex,(7120 + s)*ey);
        speed.perpendicular();
        Particle rocket = new Particle(3,500000,earth.x + add.getX(),earth.getY() + add.getY(),earth.vx + speed.getX(),earth.vy + speed.getY(),2e5);
        Vector f = rocket.getGravityForces(earth).add(rocket.getGravityForces(sun));
        double lastpx = rocket.getX() - rocket.vx*Simulation.dt - (0.5/rocket.mass)*f.getX()*Simulation.dt*Simulation.dt;
        double lastpy = rocket.getY() - rocket.vy*Simulation.dt - (0.5/rocket.mass)*f.getY()*Simulation.dt*Simulation.dt;
        rocket.setPrevious(lastpx,lastpy);
        /*
        f = earth.getGravityForces(sun);
        lastpx = earth.getX() - earth.vx*Simulation.dt - (0.5/earth.mass)*f.getX()*Simulation.dt*Simulation.dt;
        lastpy = earth.getY() - earth.vy*Simulation.dt - (0.5/earth.mass)*f.getY()*Simulation.dt*Simulation.dt;
        earth.setPrevious(lastpx,lastpy);
        */

        objects.add(rocket);
    }
    public void updateObjects(double time){
        /*if(((int)time % 43200) == 0){
            data.add("4\n"+time+"\n");
        }*/
            for(Particle t : objects){
            Vector force = new Vector();
            for(Particle o : objects){
                if(!t.equals(o)){
                    force.add(t.getGravityForces(o));
                }
            }
            t.updatePosition(force,dt);
        }
        for(Particle p : objects){
            p.setNewPositions();
        }
    }
}
