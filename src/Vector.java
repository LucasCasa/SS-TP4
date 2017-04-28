import java.rmi.MarshalException;
import java.util.DoubleSummaryStatistics;

/**
 * Created by lcasagrande on 21/03/17.
 */
public class Vector {
    double x = 0;
    double y = 0;

    public Vector(double x, double y){
        this.x = x;
        this.y = y;
    }
    public Vector(){

    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public Vector add(Vector v){
        x+= v.x;
        y+= v.y;
        return this;
    }
    @Override
    public String toString() {
        return "[Vector X: "+ x + " ,Y: " + y;
    }

    public void perpendicular(){
        double aux = x;
        x = -y;
        y = aux;
    }
}