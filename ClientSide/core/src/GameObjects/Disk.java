package GameObjects;

import java.util.Random;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.mygdx.Screens.UnitConvertor;

public class Disk  extends Actor {
	Texture texture = new Texture("disk.png");
	float radius  ; 
	boolean toHuman = false;
	Vector2 targetLoc = null;
	public boolean moving , downSpawn;
	float m, n; // y = mx + n
    
	Limits game;
	Wall targetW = null;
	Wall wY = null , wX = null;
	Line l;
	boolean wait = false;
    Music hitBall;
    Music hitWall;
    float rSize = 7;
    float myTop , myRight ; 
    public Disk(Limits game, int spawnL) {
		super();
		this.game = game;
		this.radius  = 5;
		this.setWidth(radius * 2 );
		this.setHeight(radius * 2);
		this.hitBall = Gdx.audio.newMusic(Gdx.files.internal("hit.mp3"));
		this.hitWall= Gdx.audio.newMusic(Gdx.files.internal("hit.mp3"));
		if (game.isMultiplayer)
		{
			downSpawn = spawnL == 1 ;
		}
		else	
		{
			Random r = new Random();
			downSpawn = r.nextInt(2) % 2 == 0;
		}
		myTop = game.getTop() -this.getHeight();
		myRight = game.getRight()   - this.getRight();
		
	}

	public Disk(Limits lim, int spawnL, float w, float h) {
		super();
		this.game = lim;
		this.radius  = w;
		this.setWidth(w * 2 );
		this.setHeight(h * 2);
		this.hitBall = Gdx.audio.newMusic(Gdx.files.internal("hit.mp3"));
		this.hitWall= Gdx.audio.newMusic(Gdx.files.internal("hit.mp3"));
		if (game.isMultiplayer)
		{
			downSpawn = spawnL == 1 ;
		}
		else	
		{
			Random r = new Random();
			downSpawn = r.nextInt(2) % 2 == 0;
		}
		
		
	}
	
	public String getDir() {
		if (targetW == Wall.Top)
			return "top";
		else if (targetW == Wall.Bottom)
			return "bottom";
		else if (targetW == Wall.Right)
			return "right";
		else if (targetW == Wall.Left)
			return "left";
		return null;
	}

	public void setDir(String dir) {
		// TODO Auto-generated method stub
		if (dir.compareTo("bottom")==0)
			targetW = Wall.Top;
		else if (dir.compareTo("top")==0)
			targetW = Wall.Bottom;
		else if (dir.compareTo("left")==0)
			targetW = Wall.Right;
		else if (dir.compareTo("right")==0)
			targetW = Wall.Left;
	}
	
	public void draw(SpriteBatch batch){
		if (wait)
    		return;
		UnitConvertor.draw(batch , texture, this.getX(),this.getY(), this.getWidth() , this.getHeight());
    }
	
	
	public void checkCollision(Tool tool){
        
        float diskX = this.getX();
        float diskY = this.getY() ;

        float toolX = tool.getX() ;
        float toolY = tool.getY() ;

        if (Math.sqrt( Math.pow(diskX - toolX, 2) + Math.pow(diskY - toolY, 2))
        		<= (this.radius + tool.R) * 0.8)
        {
        	if (!hitBall.isPlaying())
        	{
        		hitBall.play();
        	}
        	
        	tool.clearActions();
            this.clearActions();
        	float  a =(diskY - toolY)/ (diskX - toolX);
           
            l = new Line(a, new Vector2(diskX , diskY));
            
            if (diskY - toolY >= 0)
            {
            	wY = Wall.Top;
            }
            else
            {
            	wY = Wall.Bottom;
            }
            
            if (diskX- toolX >= 0)
            {
            	wX = Wall.Right;
            }
            else
            {
            	wX = Wall.Left;
            }
            LineToAction(l , wX , wY);
        }
    }
	
	
	/*
	 * Pre Condition : Recives line it wants to move by it 
	 * 				   XDirectino is bottom or top
	 * 				   YDriection is left ot right
	 * What id does is giving the line and the directions it needs to moves it generates
	 * the next action and adds it to the action of this disk
	 */
	public void LineToAction(Line l , Wall XDirection , Wall YDirection )
	{
		
	    
	    
	    if (YDirection == Wall.Top && XDirection == Wall.Left)
	    {
	    	if (l.getY(game.getLeft())  > game.getBottom() && l.getY(game.getLeft()) < myTop)
	    	{
	    		targetW = Wall.Left;
	    	}
	    	else
	    	{
	    		targetW = Wall.Top;
	    	}
	    }
	    
	    else if (YDirection == Wall.Top && XDirection == Wall.Right)
	    {
	    	if (l.getY(myRight) > game.getBottom()  && l.getY(myRight) < myTop)
	    	{
	    		targetW = Wall.Right;
	    	}
	    	else
	    	{
	    		targetW = Wall.Top;
	    	}
	    }
	    
	    
	    else if (YDirection == Wall.Bottom && XDirection == Wall.Left)
	    {
	    	if (l.getY(game.getLeft()) > game.getBottom() && l.getY(game.getLeft()) < myTop)
	    	{
	    		targetW = Wall.Left;
	    	}
	    	else
	    	{
	    		targetW = Wall.Bottom;
	    	}
	    }
	    
	    else if (YDirection == Wall.Bottom && XDirection == Wall.Right)
	    {
	    	if (l.getY(myRight) > game.getBottom() && l.getY(myRight) < myTop)
	    	{
	    		targetW = Wall.Right;
	    	}
	    	else
	    	{
	    		targetW = Wall.Bottom;
	    	}
	    }
	    
	    
	    
	    switch (targetW)
	    {
	    case Bottom:
	    	this.addMoveToAction(l.getX(game.getBottom()), game.getBottom());
	    	break;
	    case Top:
	    	this.addMoveToAction(l.getX(myTop),myTop);
	    	break;
	    case Left:
	    	this.addMoveToAction(game.getLeft(), l.getY(game.getLeft()));
	    	break;
	    	
	    case Right:
	    	this.addMoveToAction(myRight,l.getY(myRight));
	    	break;
	    }
	}

	public void addMoveToAction(float x , float y)
	{
		game.addMoveToAction(this, x, y ,'d');
		this.targetLoc = new Vector2(x,y);
	}
 
    public void update(Tool t1 , Tool t2)
    {
    	// t2 = bot 
    	// add boolean is single player
    	if (wait || (game.isMultiplayer && this.getY()  > game.calcMid() ))
    		return;
    	this.checkCollision(t1);
    	if (t2 != null)
    	{
    		this.checkCollision(t2); //Collision isn't chacked in multiplayer
    	}
		
		
        if (this.getActions().size == 0)// Not Moving , there is a chanse it is near a wall now, it need to be handeld 
        {
        	if (targetLoc!= null && (int)this.getX() == (int)targetLoc.x && (int)this.getY() == (int)targetLoc.y)
        	{
        		float newA = this.l.getA() * (- 1);
        		
        		boolean changed = false;
        		l = new Line(newA, new Vector2(this.getX() , this.getY()));
        		if (targetW == Wall.Bottom && (int)this.getY() == (int)game.getBottom())
        		{
        			if (this.getX() >= game.leftGoal   && 
        					this.getX()  + this.getWidth()<= game.rightGoal )
        			{
        				Log.d("goalTamir","scored Buttom Goal");
        				downSpawn = true;
        				
        				this.spawn();
        				if (game.isMultiplayer)
        				{
        					wait = true;
        				}
        					
        				this.game.incTop(this);
        				
        				
        				
        				return;
        			}
        			wY = Wall.Top;
        			changed = true;
        		}
        			
        		if (targetW == Wall.Top && 
        				(int)this.getY() == (int)myTop && !game.isMultiplayer)
        		{
        			if (this.getX() >= game.leftGoal   &&
        					this.getX()+ this.getWidth() <= game.rightGoal )
        			{
        				this.game.incBottom();
        				downSpawn = false;
        				
        				this.spawn();
        				
        				
        				return;
        			}
        			wY = Wall.Bottom;
        			changed = true;
        		}
        			
        		if (targetW == Wall.Left && (int)this.getX() == (int)game.getLeft())
        		{
        			wX = Wall.Right;
        			changed = true;
        		}
        			
        		if (targetW == Wall.Right && (int)this.getX() == (int)myRight)
        		{
        			wX = Wall.Left;
        			changed = true;
        		}
        		if (changed)
        		{
        			LineToAction(l, wX , wY);
        			if (!hitWall.isPlaying())
                	{
                		hitWall.play();
                	}
        		}
        			
        		else
        		{
            		System.out.println("Disk stoped because of a problem somewhere");
            		//this.spawn();
            	}
        	}
        	
        	if (!game.inGameBounds(this))
        	{
        		this.spawn();
        	}
        	
        }
    }
    public String getXDir()
    {
    	if (wX == Wall.Left)
    		return "left";
    	else
    		return "right";
    }
    
    
    public String getYDir()
    {
    	if (wX == Wall.Top)
    		return "top";
    	else
    		return "bottom";
    }
    
    public void setXdir(String dir)
    {
    	if (dir.compareTo("left") == 0 )
    		wX = Wall.Right;
    	else
    		wX = Wall.Left;
    }
    public void setYdir(String dir)
    {
    	if (dir.compareTo("top") == 0 )
    		wX = Wall.Bottom;
    	else
    		wX = Wall.Top;
    }
    public boolean isWaiting()
    {
    	return wait;
    }
    
    public void wakeUp()
    {
    	wait = false;
    }
    
	public void spawn() {
		
		this.clearActions();
		if (downSpawn)
		{
			this.setPosition(50, 40);
		}
		else
		{
			this.setPosition(50, 60);
		}
	}

	@Override
	public void clearActions() {
		
		super.clearActions();
		targetLoc = null;
		wX = null;
		wY = null;
		targetW = null;
		
	}

	public void spawn(float x, float y) {
		// TODO Auto-generated method stub
		this.clearActions();
		this.setX(x);
		this.setY(y);
		Log.d("handler" , "spawn x = " + x +"y = "+y);
	}
    
    
}
