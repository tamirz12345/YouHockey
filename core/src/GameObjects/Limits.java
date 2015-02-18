package GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

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
        if (isBelow) {
            valid.y = (float) Math.min(pos.y, Gdx.graphics.getWidth() * mid + height)   - width / 2;
        }

        else {
            valid.y = (float) Math.max(pos.y, Gdx.graphics.getWidth() * mid + height)   - width / 2;
            
        }
        valid.x = pos.x - width / 2 ;
        /*
        if (valid.y < 0)
        	valid.y = 0 ;
        if (valid.y > gameWidth  - width)
        	valid.y  = gameWidth  - width;
        */
        
        return valid;
    }
	
	
	public Wall isHoreg(Disk d)
	{
		
		
		if (d.getY() + d.getWidth()   >= this.gameWidth)
		{
			return Wall.Right;
		}
		if (d.getY() <= 0)
		{
			return Wall.Left;
		}
		
		if (d.getX() + d.getHeight() / 2 <= 0)
		{
			return Wall.Top;
		}
		if (d.getX()  >= this.gameHeight)
		{
			return Wall.Bottom;
		}
		
		return null;
		
		
		
		
		
		
		
	}



	public float getGameWidth() {
		return gameWidth;
	}

	public boolean inGameBounds(Actor a)
	{
		
		if (a.getY() <= a.getHeight() || a.getY() >= gameHeight)
			return false;
		if (a.getX() <= 0 || a.getX() >= gameWidth - a.getWidth())
			return false;
		if (a.getY() == Float.NaN || a.getY() == Float.NaN )
			return false;
		return true;
	}

	public float getGameHeight() {
		return gameHeight;
	}
}

