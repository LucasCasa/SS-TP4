
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcasagrande on 19/04/17.
 */
public class Simulation {
    List<String> data = new ArrayList<>();
    List<List<Particle>> days = new ArrayList<>();
    static double dt = 1;
    List<Particle> objects;
    Particle mars;
    Particle rocket;
    double min = Double.MAX_VALUE;
    int dayOfLunch = 0;
    int speedOfLunch = 0;
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
            System.out.println(i);
            for(int k = 3000; k<=4000;k+=100) {
                List<Particle> pl = days.get(i);
                objects.clear();
                for (Particle p : pl) {
                    objects.add(new Particle(p));
                }
                rocket = loadRocket(k);
                mars = objects.get(2);
                //System.out.print("Day " + i + " Speed: " + k + " : ");
                double startingDist = getDistanceToMars();
                for (int j = 0; j < time / 4; j += dt) {
                    updateObjects(j);
                    double dist = getDistanceToMars();
                    if (dist < min) {
                        min = dist;
                        dayOfLunch = i;
                        speedOfLunch = k;
                    }
                }
                if (getDistanceToMars() < startingDist){
                    System.out.println(" TODA");
                    for (int j = time / 4; j < time; j += dt) {
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
        }
        System.out.println("DIA:" + dayOfLunch);
        System.out.println("Speed: " + speedOfLunch);
        System.out.println("DISTANCIA: " + Math.sqrt(min));
        return data;
    }
    public void simulateStarting(int day,int time,int speed){
        List<Particle> pl = days.get(day);
        objects.clear();
        for(Particle p : pl){
            objects.add(new Particle(p));
        }
        mars = objects.get(2);
        rocket = loadRocket(speed);
        System.out.println("Day " + day + " : ");
        Particle earth = objects.get(1);
        System.out.println(Math.sqrt((mars.x - earth.x)*(mars.x - earth.x) + (mars.y - earth.y)*(mars.y - earth.y)));
        min = Double.MAX_VALUE;
        for(double j = 0; j<time;j+=dt){
            if((j % 43200) == 0){
                data.add("4\n"+j+"\n");
            }
            if((j % 43200) == 0) {
                for (Particle t : objects) {
                    data.add(t.x + "\t" + t.y + "\t" + /*((4 - t.id) * 1e9)*/ t.radius + "\t" + t.vx + "\t" + t.vy + "\n");
                }
            }
            updateObjects(j);
            double dist = getDistanceToMars();
                /*if(dist > lastDist){
                    valid = false;
                }*/
            if(dist < min){
                min = dist;
            }
        }
        System.out.println(Math.sqrt(min));
    }
    private double getDistanceToMars() {
        return (mars.x - rocket.x)*(mars.x - rocket.x) + (mars.y - rocket.y)*(mars.y - rocket.y);
    }

    public Particle loadRocket(double s){
        Particle sun = objects.get(0);
        Particle earth = objects.get(1);
        double angle = Particle.angle(sun,earth);
        Vector add = new Vector((1500000 + 6317000),angle);
        Vector speed = new Vector(7120 + s,angle);
        speed.perpendicular();
        Particle rocket = new Particle(3,500000,earth.x + add.getX(),earth.getY() + add.getY(),earth.vx + speed.getX(),earth.vy + speed.getY(),2e5);
        Vector f = rocket.getGravityForces(earth);
        double lastpx = rocket.getX() - rocket.vx*Simulation.dt; //- (0.5/rocket.mass)*f.getX()*Simulation.dt*Simulation.dt;
        double lastpy = rocket.getY() - rocket.vy*Simulation.dt ;//- (0.5/rocket.mass)*f.getY()*Simulation.dt*Simulation.dt;
        rocket.setPrevious(lastpx,lastpy);
        objects.add(rocket);
        return rocket;
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
