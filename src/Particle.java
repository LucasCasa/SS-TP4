/**
 * Created by Lucas on 02/04/2017.
 */
import java.util.ArrayList;
import java.util.List;

public class Particle {


    int id;
    double radius;
    static double GRAVITY = 6.693e-11;
    double x;
    double y;
    double vx;
    double vy;
    double mass;
    double lastRx = 0;
    double nextX = 0;
    double lastRy = 0;
    double nextY = 0;
    Vector f;
    double ax;
    double ay;

    public Particle previous, next;

    //Vector nextSpeed;
    //Vector speed;
    List<Particle> neighbors;
    double lastUpdate = 0;
    int timesModified = 0;

    public Particle(int id,double radius, double x, double y,Vector speed,double mass){
        this.id = id;
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.vx = speed.getX();
        this.vy = speed.getY();
        //this.nextSpeed = new Vector(0.03,0);
        this.neighbors = new ArrayList<>();
        this.mass = mass;
    }

    public Particle(int id,double radius, double x, double y,double velx,double vely,double mass){
        this.id = id;
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.vx = velx;
        this.vy = vely;
        this.lastRx = x - velx*Simulation.dt;
        this.lastRy = y- vely*Simulation.dt;
        this.neighbors = new ArrayList<>();
        this.mass = mass;
        this.f = new Vector();
    }


    public Particle(int id,double radius, double x, double y,double velx,double vely,double ax,double ay,double mass){
        this.id = 1;
        this.radius = radius;
        this.x=x;
        this.y = y;
        this.vx = velx;
        this.vy = vely;
        this.ax = ax;
        this.ay = ay;
        this.mass = mass;
        this.f = new Vector();
    }

    public Particle(int id, double radius){
        this.id = id;
        this.radius = radius;
        //this.nextSpeed = new Vector(0.03,0);
    }

    public Particle(Particle p){
        x = p.x;
        y = p.y;
        vx = p.vx;
        vy = p.vy;
        mass = p.mass;
        radius = p.radius;
        lastRx = p.lastRx;
        lastRy = p.lastRy;
        id = p.id;
        f = p.f;
    }

    public void setPrevious(double x, double y){
        lastRx = x;
        lastRy = y;
    }

    public Particle(double mass, double radius){
        this.mass = mass;
        this.radius = radius;
    }

    public Particle(double x, double radius, double mass){
        this.x = x;
        this.y = 0;
        this.vx=0;
        this.vy=0;
        this.ax=0;
        this.ay=0;
        this.radius=radius;
        this.mass=mass;
    }

    public Particle(double x, double vx, double ax, double radius, double mass){
        this.x= x;
        this.y=0;
        this.vx = vx;
        this.vy = 0;
        this.ax = ax;
        this.ay = 0;
        this.radius = radius;
        this.mass = mass;

    }
    public double getRadius() {
        return radius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static double dist2(Particle p1, Particle p2){
        return (p2.x-p1.x)*(p2.x-p1.x) + (p2.y-p1.y)*(p2.y-p1.y);
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }

    //System.out.println("CUANTO VALE EL MODULO DE SPEED? " + p.getSpeed().getModule())
    public int getId() {
        return id;
    }
/*
    public Vector getSpeed() {
        return speed;
    }

    public void setSpeed(Vector speed) {
        this.speed = speed;
    }
*/
    public double getSpeedX(){
        return vx;
    }
    public double getSpeedY(){
        return vy;
    }

    public double getMass(){
        return mass;
    }

    @Override
    public String toString() {
        return "Particula - Id: " + id + " Pos:(" +x+","+y+") Vel:("+vx +"," + vy +") Radio:"+radius;
    }

    public void setSpeedX(double speedX) {
        this.vx = speedX;
    }
    public void setSpeedY(double speedY){
        this.vy = speedY;
    }

    public void update(double time) {
        x += vx * (time - lastUpdate);
        y += vy * (time - lastUpdate);
        lastUpdate = time;
    }

    public Vector getGravityForces(Particle o){
        double dist2 = dist2(this,o);
        double module = GRAVITY*mass*o.mass / (dist2);
        double dist = Math.sqrt(dist2);
        double ex = (o.x-x)/dist;
        double ey = (o.y - y)/dist;
        return new Vector(module*ex,module*ey);
    }
    public static double angle(Particle p, Particle o) {
        return Math.atan2(o.getY()-p.y,o.getX()-p.x);
    }

    public void updatePosition(Vector force,double dt) {

        double rx = 2*x - lastRx + (force.getX()/mass)*dt*dt;
        double ry = 2*y - lastRy + (force.getY()/mass)*dt*dt;
        double fm = Math.sqrt((force.x*force.x + force.y*force.y));
        f = new Vector(force.x / fm,force.y / fm);
        vx = (rx - lastRx) / (2*dt);
        vy = (ry - lastRy) / (2*dt);
        lastRx = x;
        nextX = rx;
        lastRy = y;
        nextY = ry;


    }

    public void setNewPositions(){
        x = nextX;
        y = nextY;
    }
    public void advanceFirst(Vector v){
        double rx = x + vx*Simulation.dt + 0.5*mass*v.getX()*Simulation.dt*Simulation.dt;
        double ry = y + vy*Simulation.dt + 0.5*mass*v.getY()*Simulation.dt*Simulation.dt;
        lastRx = x;
        lastRy = y;
        x = rx;
        y = ry;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Particle){
            return id == ((Particle) o).id;
        }
        return false;
    }
}
