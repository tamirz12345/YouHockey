package GameObjects;
 
import com.badlogic.gdx.Gdx;
 
 
public class Line {
        private float a;
        private float b;
 
    public Line (float a, float b)
    {
        this.a = a;
        this.b = b;    
    }
   
   
 
    public float getY(float x) {
                return this.a * x + b;
        }
    
    public float getX(float Y) {
        return (Y - b) / a;
        
        
        
}



	public float getA() {
		return a;
	}



	public float getB() {
		return b;
	}
    
    
}