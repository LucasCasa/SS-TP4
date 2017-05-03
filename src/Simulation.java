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

    public void calculateDays(double time){
        System.out.println("Calculating Positions on days");
        for(double i = 0; i<(time);i+=dt){
            if( (i % 86400)  == 0){
                    ArrayList<Particle> positions = new ArrayList<>();
                    for (Particle o : objects) {
                        positions.add(new Particle(o));
                    }
                    days.add(positions);
                    System.out.print(" " + (i / (86400)));
            }
            updateObjects(i);
        }
        System.out.println("");
    }
    public List<String> simulate(int time,int vStart, int vFinish,int vJump){
        for(int i = 0; i<days.size();i++){
            double dayMin = Double.MAX_VALUE;
            List<Particle> pl = days.get(i);
            for(int k = vStart; k<=vFinish;k+=vJump) {
                objects.clear();
                for (Particle p : pl) {
                    objects.add(new Particle(p));
                }
                loadRocket(k);
                for (double j = 0; j < time; j += dt) {
                    updateObjects(j);
                    double dist = getDistanceToMars();
                    if (dist < min) {
                        min = dist;
                        dayOfLunch = i;
                        speedOfLunch = k;
                    }
                    if(dist < dayMin){
                        dayMin = dist;
                    }
                }
            }
            System.out.println(i + "\t" + dayMin);

        }
        System.out.println("DIA:" + dayOfLunch);
        System.out.println("VELOCIDAD: " + speedOfLunch);
        System.out.println("DISTANCIA:" + Math.sqrt(min));
        return data;
    }
    public void simulateStarting(int day,int time,int s){
        min = Double.MAX_VALUE;
        List<Particle> pl = days.get(day);
        objects.clear();
        for(Particle p : pl){
            objects.add(new Particle(p));
        }
        loadRocket(s);
        for(double j = 0; j<time;j+=dt){
            if((j % 86400) == 0){
                data.add("4\n"+j+"\n");
                for (Particle t : objects) {
                    data.add(t.x + "\t" + t.y + "\t" + t.radius + "\t" +t.vx+ "\t" +t.vy+ "\t");
                    switch (t.id){
                        case 0:
                            data.add("1\t1\t0\n");
                            break;
                        case 1:
                            data.add("0\t0.4\t1\n");
                            break;
                        case 2:
                            data.add("0.8\t0\t0\n");
                            break;
                        case 3:
                            data.add("1\t1\t1\n");

                    }
                    if(t.id == 1){
                        rocketData.add(t.x + "\t" + t.y + "\n");
                    }
                }

            }
            updateObjects(j);
            double dist = getDistanceToMars();
                if(dist < min){
                    min = dist;
                }
        }
        System.out.println(s + "\t" + Math.sqrt(min));
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
        /*double auxX = speed.x * 0.9238795 - speed.y * 0.382683;
        speed.x = speed.x * 0.382683 + speed.y * 0.9238795;
        speed.y = -auxX;
        */
        speed.perpendicular();
        Particle rocket = new Particle(3,1e9,earth.x + add.getX(),earth.getY() + add.getY(),earth.vx + speed.getX(),earth.vy + speed.getY(),2e5);
        Vector f = rocket.getGravityForces(earth).add(rocket.getGravityForces(sun));
        double lastpx = rocket.getX() - rocket.vx*dt + (0.5/rocket.mass)*f.getX()*dt*dt;
        double lastpy = rocket.getY() - rocket.vy*dt + (0.5/rocket.mass)*f.getY()*dt*dt;
        rocket.setPrevious(lastpx,lastpy);
        objects.add(rocket);
    }
    public void updateObjects(double time){
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
