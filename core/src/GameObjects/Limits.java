package GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

/**
 * Created by user-pc on 03/02/2015.
 */
public class Limits {
	private float mid;
	private float gameWidth;
	private float gameHeight;
	private  float top;
	public float left;
	public float right ; 
	public float bottom;
	
	public float leftGoal = (float) 0.4  ;
	public float rightGoal = (float) 0.6 ;
	
	private float ScoreBottom;
	private float ScoreTop;
    public Limits()
    {
    	mid = (float) 0.5;
    	gameWidth = Gdx.graphics.getHeight();
    	gameHeight = Gdx.graphics.getWidth();
    	top = (float) (gameHeight * 0.95)  ;
    	left = 0;
    	right= gameWidth - 32;
    	bottom = (float) (gameHeight *  0.05)  + 32;
    	
    	
    	ScoreBottom = 0 ;
    	ScoreTop = 0;
    	
    	
    }

    public float getMid() {
		return mid;
	}

	public void setMid(float mid) {
		this.mid = mid;
	}

	public float getTop() {
		return top;
	}

	public float getLeft() {
		return left;
	}

	public float getRight() {
		return right;
	}

	public float getBottom() {
		return bottom;
	}



	public Vector2 validateTool(Vector2 pos, boolean isBelow, float height, float width)
    {
        Vector2 valid = new Vector2();
        if (isBelow) {
            valid.y = (float) Math.min(pos.y + height, Gdx.graphics.getWidth() * mid + height)   - width / 2;
        }

        else {
            valid.y = (float) Math.max(pos.y + height, Gdx.graphics.getWidth() * mid + height)   - width / 2;
            
        }
        valid.x = pos.x - width / 2 ;
    
        return valid;
    }

	public float getGameWidth() {
		return gameWidth;
	}

	public boolean inGameBounds(Actor a)
	{
		
		if (a.getY() < bottom  || a.getY() > top)
			return false;
		if (a.getX() <left  || a.getX() > right )
			return false;
		if (a.getY() == Float.NaN || a.getY() == Float.NaN )
			return false;
		return true;
	}
	
	public boolean inGameBounds(float x , float y)
	{
		if (y < bottom  || y > top)
		{
			System.out.println("y =  " + y + " out of bounds");
			return false;
		}
		if (x <left  || x > right )
		{
			System.out.println("x=  " + x + " out of bounds");
			return false;
		}
		if (y== Float.NaN || y== Float.NaN )
			return false;
		return true;
	}

	public float getGameHeight() {
		return gameHeight;
	}

	public void incBottom() {
		this.ScoreBottom ++;
		
	}
	
	public void incTop()
	{
		this.ScoreTop ++;
	}
	
	
	public void addMoveToAction(Actor a, float x , float y)
	{
		if (!this.inGameBounds(x, y))
		{
			System.out.println("Out Of Bound ( "+x+","+y+" )");
		}
		MoveToAction moveAction = new MoveToAction();
		float oneF = 1f;
		float speed = (int) Math.sqrt(Math.pow(a.getX()-  x , 2) 
				+ Math.pow(a.getY()-y, 2)) / 200 * oneF;
		if (speed == 0 )
		{
			speed = (float) (oneF * 0.4);
		}
		moveAction.setDuration(speed);
		moveAction.setPosition(x, y);
		a.addAction(moveAction);
		
	}
}

