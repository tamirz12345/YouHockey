package GameObjects;
 
import com.badlogic.gdx.math.Vector2;
 
 
public class Line {
        private float a;
        private Vector2 p;
 
    public Line (float a,Vector2 point)
    {
        this.a = a;
        this.p = point;    
    }
   
   
 
    public float getY(float x) {
                return p.y + a * ( x - p.x);
        }
    
    public float getX(float y) {
        return p.x + (y - p.y ) /  a;
        
        
        
    }



	public float getA() {
		return a;
	}



    
    
}