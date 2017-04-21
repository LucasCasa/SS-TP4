import java.rmi.MarshalException;
import java.util.DoubleSummaryStatistics;

/**
 * Created by lcasagrande on 21/03/17.
 */
public class Vector {
    double x = 0;
    double y = 0;

    public Vector(double module, double angle){
        x = Math.cos(angle)*module;
        y = Math.sin(angle)*module;
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public void add(Vector v){
        x+= v.x;
        y+= v.y;
    }
    @Override
    public String toString() {
        return "[Vector X: "+ x + " ,Y: " + y;
    }

    public void perpendicular(){
        double aux = -x;
        x = y;
        y = aux;
    }
}