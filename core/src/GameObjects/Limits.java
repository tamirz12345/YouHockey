package GameObjects;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by user-pc on 03/02/2015.
 */
public class Limits {
	private float mid;
	private float gameWidth;
	private float gameHeight;
    public Limits()
    {
    	mid = (float) 0.5;
    	gameWidth = Gdx.graphics.getHeight();
    	gameHeight = Gdx.graphics.getWidth();
    	
    	
    	
    }
    
    

    public float getMid() {
		return mid;
	}



	public void setMid(float mid) {
		this.mid = mid;
	}



	public Vector2 validateTool(Vector2 pos, boolean isBelow, float height, float width)
    {
        Vector2 valid = new Vector2();
        if (!isBelow) {
            valid.x = (float) Math.min(pos.x, Gdx.graphics.getWidth() * mid)   - width / 2;
        }

        else {
            valid.x = (float) Math.max(pos.x, Gdx.graphics.getWidth() * mid)   - width / 2;
            
        }
        valid.y = pos.y - height / 2 ;
        return valid;
    }
	
	
	public Set<Wall> isHoreg(Disk d)
	{
		Set<Wall> s = new HashSet<Wall>();
		if (d.getY() + d.getWidth()   >= this.gameWidth)
		{
			s.add(Wall.Right);
		}
		if (d.getY() <= 0)
		{
			s.add(Wall.Left);
		}
		
		if (d.getX() + d.getHeight() / 2 <= 0)
		{
			s.add(Wall.Top);
		}
		if (d.getX()  >= this.gameHeight)
		{
			s.add( Wall.Bottom);
		}
		
		
		
		
		
		
		return s;
		
		
	}
}

