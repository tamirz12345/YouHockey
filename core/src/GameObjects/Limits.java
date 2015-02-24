package GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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
	
	public float leftGoal = (float) 0.35  ;
	public float rightGoal = (float) 0.65 ;
	private float  speedUnit = (float) 0.55; 
	private float ScoreBottom;
	private float ScoreTop;
	private Music goalSound ;
	private Music ohSound ;
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
    	speedUnit = (float) (speedUnit * Math.sqrt(Math.pow(gameHeight , 2 ) + Math.pow(gameWidth , 2 )));
    	goalSound = Gdx.audio.newMusic(Gdx.files.internal("score.mp3"));
    	ohSound = Gdx.audio.newMusic(Gdx.files.internal("oh.mp3"));
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



	public Vector2 validateTool(Tool t , float x , float y )
    {
        Vector2 valid = new Vector2();
        if (t.isBelow) {
            valid.y = (float) Math.min(y , Gdx.graphics.getWidth() * mid)  + t.getHeight();
        }

        else {
            valid.y = (float) Math.max(y, Gdx.graphics.getWidth() * mid )+ t.getHeight()   ;
            
        }
        
        
        if ( x < 0 )
        	valid.x = 0  ;
        if (x > this.getGameWidth() - t.getWidth());
        	valid.x = this.getGameWidth() - t.getWidth();
        	
    
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
		goalSound.play();
	}
	
	public void incTop()
	{
		this.ScoreTop ++;
		ohSound.play();
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
				+ Math.pow(a.getY()-y, 2)) /speedUnit* oneF;
		if (speed == 0 )
		{
			speed = (float) (oneF * 0.4);
		}
		moveAction.setDuration(speed);
		moveAction.setPosition(x, y);
		a.addAction(moveAction);
		
	}
}

