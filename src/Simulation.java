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
    double lastDist;
    public Simulation(Particle e, Particle s, Particle m){
       objects = new ArrayList<>();
       objects.add(s);
       objects.add(e);
       objects.add(m);
       //objects.add(r);
       data = new ArrayList<>();

    }

    public void calculateDays(double time){
        System.out.println("Calculating Positions on days");
        for(double i = 0; i<time;i+=dt){
            if( i % (86400)  == 0){
                ArrayList<Particle> positions = new ArrayList<>();
                for(Particle o : objects){
                    positions.add(new Particle(o));
                }
                days.add(positions);
                System.out.print(" "+(i / 86400));
            }
            updateObjects(i);
        }
        System.out.println("");
    }
    public List<String> simulate(int time){
        for(int i = 0; i<days.size();i++){
            List<Particle> pl = days.get(i);
            objects.clear();
            for(Particle p : pl){
                objects.add(new Particle(p));
            }
            loadRocket(3700);
            System.out.print("Day " + i + " : ");
            boolean valid = true;
            for(double j = 0; j<time && valid;j+=dt){
                updateObjects(j);
                if( j % (time / 100)  == 0){
                    System.out.print((j / (time / 100)) + " ");
                }
                double dist = getDistanceToMars();
                if(dist < min){
                    min = dist;
                    dayOfLunch = i;
                }
            }
            System.out.println("");
        }
        System.out.println("DIA:" + dayOfLunch);
        System.out.println("DISTANCIA:" + Math.sqrt(min));
        return data;
    }
    public void simulateStarting(int day,int time){
        List<Particle> pl = days.get(day);
        objects.clear();
        for(Particle p : pl){
            objects.add(new Particle(p));
        }
        loadRocket(3700);
        System.out.print("Day " + day + " : ");
        for(double j = 0; j<time;j+=dt){
            if((j % 3600) == 0){
                data.add("4\n"+j+"\n");
            }
            updateObjects(j);
            if( j % (time / 100)  == 0){
                System.out.print((j / (time / 100)) + " ");
            }
            if((j % 3600) == 0) {
                for (Particle t : objects) {
                    data.add(t.x + "\t" + t.y + "\t" + /*((4 - t.id) * 1e9)*/ t.radius + "\t" + t.f.x + "\t" + t.f.y + "\n");
                }
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
        Vector add = new Vector(1500000 + 6317000,Particle.angle(sun,earth));
        Vector speed = new Vector(7120 + s,Math.atan2(sun.getY()-earth.y,sun.getX()-earth.x));
        speed.perpendicular();
        Particle rocket = new Particle(3,500000,earth.x + add.getX(),earth.getY() + add.getY(),earth.vx + speed.getX(),earth.vy + speed.getY(),2e5);
        Vector f = rocket.getGravityForces(earth);
        double lastpx = rocket.getX() - rocket.vx*Simulation.dt - (0.5/rocket.mass)*f.getX()*Simulation.dt*Simulation.dt;
        double lastpy = rocket.getY() - rocket.vy*Simulation.dt - (0.5/rocket.mass)*f.getY()*Simulation.dt*Simulation.dt;
        rocket.setPrevious(lastpx,lastpy);
        objects.add(rocket);
    }
    public void updateObjects(double time){
        /*if(((int)time % 43200) == 0){
            data.add("4\n"+time+"\n");
        }*/
            for(Particle t : objects){
            Vector force = new Vector(0,0);
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
