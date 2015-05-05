package GameObjects;

import java.util.concurrent.BlockingQueue;

import Network.Message;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.mygdx.Screens.UnitConvertor;

/**
 * Created by user-pc on 03/02/2015.
 */
public class Limits {
	private float mid;
	private float gameWidth;
	private float gameHeight;
	public  float top;
	public float left;
	public float right ; 
	public float bottom;
	
	public float leftGoal = (float) 0.35  ;
	public float rightGoal = (float) 0.65 ;
	private float  speedUnit = (float) 0.55; 
	private Integer ScoreBottom;
	private Integer ScoreTop;
	private Music goalSound ;
	private Music ohSound ;
	
	private double xUnit ;
	private double yUnit;
	
	BlockingQueue<String> toSend;
	public boolean isMultiplayer = false;
    public Limits(BlockingQueue<String> q)
    {
    	mid = (float) 0.5;
    	gameWidth = Gdx.graphics.getHeight();
    	gameHeight = Gdx.graphics.getWidth();
    	
    	
    	
    	ScoreBottom = 0 ;
    	ScoreTop = 0;
    	speedUnit = (float) (speedUnit * Math.sqrt(Math.pow(gameHeight , 2 ) + Math.pow(gameWidth , 2 )));
    	goalSound = Gdx.audio.newMusic(Gdx.files.internal("score.mp3"));
    	ohSound = Gdx.audio.newMusic(Gdx.files.internal("oh.mp3"));
    	
    	xUnit = 0.01 * gameWidth ;
    	yUnit = 0.01 * gameHeight;
    	
    	
    	
    	
    	left = 0;
    	bottom = (float) (5 * yUnit)  ;
    	top =gameHeight - bottom  ;
    	right= gameWidth -  left;
    	
    	
    	if (q != null)
    	{
    		isMultiplayer = true;
    		toSend = q;
    	}
    	
    }

    public float getMid() {
		return mid;
	}
    
    public float calcMid()
    {
    	return (float) ((this.getMid() ) * (this.top + this.bottom));
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



	public double getxUnit() {
		return xUnit;
	}

	public double getyUnit() {
		return yUnit;
	}

	public Vector2 validateTool(Tool t , float x , float y )
    {
        
        if (t.isBelow) {
            y = (float) Math.min(y , this.calcMid()) ;
        }

        else {
          y= (float) Math.max(y, this.calcMid())+ t.getHeight()  ;
            
        }
        
        
        if ( x < left )
        	x = left  ;
        if (x + t.getWidth()  > right)
        {
        	System.out.println(x + " > " + right);
        	x = right - t.getWidth();
        }
        	
        if (y - t.getHeight()< bottom )
        	y = bottom + t.getHeight();
        if (y > top)
        	y = top;
         
    
        return new Vector2(x,y);
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
		if (goalSound.isPlaying())
			goalSound.stop();
		goalSound.play();
	}
	
	public void incTop(Disk d)
	{
		this.ScoreTop ++;
		if (ohSound.isPlaying())
			ohSound.stop();
		ohSound.play();
		if(isMultiplayer)
		{
			float newX = d.getX() / this.getGameWidth();
			float newY = d.getY() / this.getGameHeight();
			String msgS= "905-"+"1"+"-"+newX+"-"+newY+"-";
			Log.d("myDebug","adding to TOSend queue : " + msgS);
			toSend.add(msgS);
			Log.d("myDebug","addedto TOSend queue. ");
		}
	}
	
	
	public void addMoveToAction(Actor a, float x , float y , Character typeC)
	{
		if (!this.inGameBounds(x, y))
		{
			System.out.println("Out Of Bound ( "+x+","+y+" )");
		}
		MoveToAction moveAction = new MoveToAction();
		
		float speed =  (float) (Math.sqrt(Math.pow(a.getX()-  x , 2) 
				+ Math.pow(a.getY()-y, 2)) /speedUnit);
		if (speed == 0 )
		{
			speed =0.2f;
		}
		moveAction.setDuration(speed);
		moveAction.setPosition(x, y);
		a.addAction(moveAction);
		if (isMultiplayer)
		{
			if ( typeC == 'h')
			{
				float newX = x / this.getGameWidth();
				float newY= y / this.getGameHeight();
				String msg=  "901-"+Float.toString(newX)+"-"+Float.toString(newY)+
						"-"+Float.toString(speed)+"-";
				Log.d("myDebug","adding to TOSend queue : " + msg);
				toSend.add(msg);
				Log.d("myDebug","addedto TOSend queue : ");
			}
			else if (typeC == 'd' && a.getY() < this.calcMid())
			{
				float newX = x / this.getGameWidth();
				float newY= y / this.getGameHeight();
				String xDir , yDir;
				xDir = ((Disk) a).getXDir();
				yDir = ((Disk) a).getYDir();
				String msg=  "906-"+Float.toString(newX)+"-"+Float.toString(newY)+
						"-"+Float.toString(speed)+"-"+xDir+"-"+yDir+"-";
				Log.d("myDebug","adding to TOSend queue : " + msg);
				toSend.add(msg);
				Log.d("myDebug","addedto TOSend queue : ");
			}
		}
		
	}
	
	
	public Vector2 leftBottomCorner()
	{
		return UnitConvertor.toGame(this.left, this.bottom);
	}
	
	public Vector2 leftTopCorner()
	{
		return UnitConvertor.toGame(this.left, this.top);
	}
	public Vector2 rightBottomCorner()
	{
		return UnitConvertor.toGame(this.right, this.bottom);
	}
	public Vector2 rightTopCorner()
	{
		return UnitConvertor.toGame(this.right, this.top);
	}

	public Integer getScoreBottom() {
		return ScoreBottom;
	}

	public Integer getScoreTop() {
		return ScoreTop;
	}

	public void addMoveToAction(Tool tool, float x, float y, float time) {
		// TODO Auto-generated method stub
		if (!this.inGameBounds(x, y))
		{
			System.out.println("Out Of Bound ( "+x+","+y+" )");
		}
		MoveToAction moveAction = new MoveToAction();
		
		
		moveAction.setDuration(time);
		moveAction.setPosition(x, y);
		tool.addAction(moveAction);
	}
	
	
}

