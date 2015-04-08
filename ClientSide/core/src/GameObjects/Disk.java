package GameObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.mygdx.game.UnitConvertor;

public class Disk  extends Actor {
	Texture texture = new Texture("disk.png");
	float radius  ; 
	boolean toHuman = false;
	Vector2 targetLoc = null;
	boolean moving , downSpawn;
	float m, n; // y = mx + n
    
	Limits game;
	Wall targetW = null;
	Wall wY = null , wX = null;
	Line l;
    Music hitBall;
    Music hitWall;
    float rSize = 7;
    public Disk(Limits game) {
		super();
		this.game = game;
		this.radius  = (float) (rSize * game.getxUnit() );
		this.setWidth(radius * 2 );
		this.setHeight(radius * 2);
		this.hitBall = Gdx.audio.newMusic(Gdx.files.internal("hit.mp3"));
		this.hitWall= Gdx.audio.newMusic(Gdx.files.internal("hit.mp3"));
		Random r = new Random();
		downSpawn = r.nextInt(2) % 2 == 0;
	}

	@Override
	public void draw(Batch batch, float alpha){
		Vector2 v = UnitConvertor.toGame(super.getX(),super.getY());
        batch.draw(texture,v.x,v.y,this.getWidth(), this.getHeight());
    }
	
	
	public void checkCollision(Tool tool){
        long hitTime = 0;
        float diskX = this.getX();
        float diskY = this.getY() ;

        float toolX = tool.getX() ;
        float toolY = tool.getY() ;

        if (Math.sqrt( Math.pow(diskX - toolX, 2) + Math.pow(diskY - toolY, 2))
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
	    	if (l.getY(game.getLeft())  > game.getBottom() && l.getY(game.getLeft()) < game.getTop())
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
	    	if (l.getY(game.getRight()) > game.getBottom()  && l.getY(game.getRight()) < game.getTop())
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
	    	if (l.getY(game.getLeft()) > game.getBottom() && l.getY(game.getLeft()) < game.getTop())
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
	    	if (l.getY(game.getRight()) > game.getBottom() && l.getY(game.getRight()) < game.getTop())
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
	    	this.addMoveToAction(l.getX(game.getTop()),game.getTop());
	    	break;
	    case Left:
	    	this.addMoveToAction(game.getLeft(), l.getY(game.getLeft()));
	    	break;
	    	
	    case Right:
	    	this.addMoveToAction(game.getRight(),l.getY(game.getRight()));
	    	break;
	    }
	}

	public void addMoveToAction(float x , float y)
	{
		game.addMoveToAction(this, x, y);
		this.targetLoc = new Vector2(x,y);
	}
 
    public void update(Tool t1 , Tool t2)
    {
    	this.checkCollision(t1);
		this.checkCollision(t2);
		
        if (this.getActions().size == 0)// Not Moving , there is a chanse it is near a wall now, it need to be handeld 
        {
        	if (targetLoc!= null && this.getX() == targetLoc.x && this.getY() == targetLoc.y)
        	{
        		float newA = this.l.getA() * (- 1);
        		
        		boolean changed = false;
        		l = new Line(newA, new Vector2(this.getX() , this.getY()));
        		if (targetW == Wall.Bottom && this.getY() == game.getBottom())
        		{
        			if (this.getX() >= game.leftGoal  * game.getGameWidth() -32 &&
        					this.getX() <= game.rightGoal * game.getGameWidth())
        			{
        				this.game.incTop();
        				downSpawn = true;
        				this.spawn();				
        				return;
        			}
        			wY = Wall.Top;
        			changed = true;
        		}
        			
        		if (targetW == Wall.Top && this.getY() == game.getTop())
        		{
        			if (this.getX() >= game.leftGoal  * game.getGameWidth() - 32&&
        					this.getX() <= game.rightGoal * game.getGameWidth())
        			{
        				this.game.incBottom();
        				downSpawn = false;
        				this.spawn();
        				return;
        			}
        			wY = Wall.Bottom;
        			changed = true;
        		}
        			
        		if (targetW == Wall.Left && this.getX() == game.getLeft())
        		{
        			wX = Wall.Right;
        			changed = true;
        		}
        			
        		if (targetW == Wall.Right && this.getX() == game.getRight())
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
            		this.spawn();
            	}
        	}

        	if (!game.inGameBounds(this))
        	{
        		this.spawn();
        	}
        }
    }


	public void spawn() {
		this.clearActions();
		if (downSpawn)
		{
			this.setPosition(game.getGameWidth()/2 - this.getWidth()/2 , 
					(float) (game.getGameHeight() * 0.4 - this.getHeight()/2));
		}
		else
		{
			this.setPosition(game.getGameWidth()/2 - this.getWidth()/2 , 
					(float) (game.getGameHeight() * 0.6 + this.getHeight()/2));
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
    
    
}
