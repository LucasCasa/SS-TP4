import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by lcasagrande on 19/04/17.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Particle sun = new Particle(0,695700000,0,0,0,0,1.988e30);
        Particle earth = new Particle(1,6317000,1.391734353396533E11,	-0.571059040560652E11,10801.9,27565.2,5.972e24);
        Particle mars = new Particle(2,3389000,0.831483493435295E11,-1.914579540822006E11,23637.9,11429.0,6.4185e23);
/*        Vector add = new Vector(1500000 + 6317000,Particle.angle(sun,earth));
        Vector speed = new Vector(7120,Math.atan2(sun.getY()-add.y,sun.getX()-add.x));
        speed.perpendicular();
        Particle rocket = new Particle(3,500000,earth.x + add.getX(),earth.getY() + add.getY(),earth.vx + speed.getX(),earth.vy + speed.getY(),2e5);
        Vector f = rocket.getGravityForces(earth);
        System.out.println("EARTH: "+ earth.vx + " " + earth.vy);
        System.out.println("ROCKET: "+ rocket.vx + " " + rocket.vy);
        double lastpx = rocket.getX() - rocket.vx*Simulation.dt - (0.5/rocket.mass)*f.getX()*Simulation.dt*Simulation.dt;
        double lastpy = rocket.getY() - rocket.vy*Simulation.dt - (0.5/rocket.mass)*f.getY()*Simulation.dt*Simulation.dt;
        rocket.setPrevious(lastpx,lastpy);
        f = earth.getGravityForces(sun);
        lastpx = earth.getX() - earth.vx*Simulation.dt - (0.5/earth.mass)*f.getX()*Simulation.dt*Simulation.dt;
        lastpy = earth.getY() - earth.vy*Simulation.dt - (0.5/earth.mass)*f.getY()*Simulation.dt*Simulation.dt;
        earth.setPrevious(lastpx,lastpy);
        //System.out.println(Particle.angle(p,p2)*180/Math.PI);
        //System.out.println(earth.getGravityForces(sun));*/
        Simulation s = new Simulation(earth,sun,mars);
        /*List<String> d = s.simulate(59356800);

        for(String line : d){
            fl.write(line);
        }
        fl.close();*/
        s.calculateDays(686*3600*24);
        //s.simulate(365*24*3600);
        s.simulateStarting(511,365*24*3600);
        FileWriter fl = new FileWriter("out.txt");
        /*for(List<Particle> days : s.days){
            fl.write("3\n" + s.days.indexOf(days) + "\n");
            for(Particle t : days){
                fl.write(t.x + "\t" + t.y + "\t" + ((4 - t.id)*1e9) + "\t" +1 + "\t" +1+"\n");
            }
        }*/
        for(String st : s.data){
            fl.write(st);
        }
        fl.close();
    }
}

