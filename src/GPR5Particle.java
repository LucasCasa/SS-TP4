/**
 * Created by root on 4/25/17.
 */
public class GPR5Particle extends Particle {

    double aprimera;
    double asegunda;
    double atercera;

    public GPR5Particle previous, next;

    public GPR5Particle(int id, double radius, double x, double y, double vx, double mass,double k, double gamma) {
        super(id, radius, x, y, vx, 0, 0, 0, mass);

        //Primer paso del integrador
        this.ax = ((-k*x -gamma*vx)/mass);
        this.aprimera = -(k/mass)*vx - (gamma/mass)*ax;
        this.asegunda = -(k/mass)*ax - (gamma/mass)*aprimera;
        this.atercera =  -(k/mass)*aprimera - (gamma/mass)*asegunda;  //Todo esto vale para el paso inicial!! Dsps me baso en los predichos
    }


    public GPR5Particle(double m, int i) {
        super(m,i);
    }

    /*
    public GPR5Particle(){

    }*/

    public GPR5Particle(double x, double radius, double mass) {
        super(x,radius,mass);
    }

    public GPR5Particle(double x, double vx, double ax, double radius, double mass) {
        super(x,vx,ax,radius,mass);
    }

    public String toString(){
        return "Posicion: " + x + "\n" + "Velocidad: " + vx + "\n" + "Aceleracion: " + ax + "\n" + "Aprimera: " + aprimera + "\n" + "Asegunda: " + asegunda + "\n" + "Atercera: " + atercera;
    }
}
