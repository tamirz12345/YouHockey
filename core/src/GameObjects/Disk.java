package GameObjects;

import java.util.Set;
import java.util.Vector;




import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.sun.xml.internal.bind.util.Which;

public class Disk  extends Actor{
	Texture texture = new Texture("disk.png");
	float radius = 32 ; 
	boolean toHuman = false;
	Vector2 delta;
	boolean moving;
	float m, n; // y = mx + n
    double speed = 0.05;
	Limits game;
	
	
	public Disk(Limits game) {
		super();
		delta = null;
		this.game = game;
		this.setWidth(64);
		this.setHeight(64);
	}


	@Override
	public void draw(Batch batch, float alpha){
		
        batch.draw(texture,super.getX(),super.getY());
    }
	
	
	public void TestAct()
	{
		MoveToAction moveAction = new MoveToAction();
	    
	    moveAction.setDuration(10f);
		if (toHuman)
		{
			moveAction.setPosition(0f, 0f);
			
		}
		else
		{
			moveAction.setPosition(Gdx.graphics.getWidth()
					, Gdx.graphics.getHeight());
			
		}
		this.addAction(moveAction);
		toHuman = !toHuman;
	}
	
	
	public void checkCollision(Tool tool){
        float diskX = this.getX();
        float diskY = this.getY() ;

        float toolX = tool.getX() ;
        float toolY = tool.getY() ;

        if (Math.sqrt( Math.pow(diskX - toolX, 2) + Math.pow(diskY - toolY, 2))
        		<= this.radius + tool.radius)
        {
        	this.clearActions();
        	float  a =(diskX - toolX)/ (diskY - toolY);
            float b = diskX - diskX * a ; 
            Line l = new Line(a,b);
            Wall wY = null , wX = null;
            if (diskX - toolX >= 0)
            {
            	wX = Wall.Bottom;
            }
            else
            {
            	wX = Wall.Top;
            }
            
            if (diskY- toolY >= 0)
            {
            	wY = Wall.Left;
            }
            else
            {
            	wY = Wall.Right;
            }
            LineToAction(l , wX , wY);
            
    	    
    	    
        }
    }
	
	
	
	public void LineToAction(Line l , Wall XDirection , Wall YDirection )
	{
		MoveToAction moveAction = new MoveToAction();
	    moveAction.setDuration(4f);
	    Wall targetW = null;
	    
	    if (XDirection == Wall.Top && YDirection == Wall.Left)
	    {
	    	if (l.getY(0) > 0)
	    	{
	    		targetW = Wall.Left;
	    	}
	    	else
	    	{
	    		targetW = Wall.Top;
	    	}
	    }
	    
	    else if (XDirection == Wall.Top && YDirection == Wall.Right)
	    {
	    	if (l.getY(game.getGameWidth()) >0)
	    	{
	    		targetW = Wall.Right;
	    	}
	    	else
	    	{
	    		targetW = Wall.Top;
	    	}
	    }
	    
	    
	    else if (XDirection == Wall.Bottom && YDirection == Wall.Left)
	    {
	    	if (l.getY(0) <game.getGameHeight())
	    	{
	    		targetW = Wall.Left;
	    	}
	    	else
	    	{
	    		targetW = Wall.Bottom;
	    	}
	    }
	    
	    else if (XDirection == Wall.Bottom && YDirection == Wall.Right)
	    {
	    	if (l.getY(game.getGameWidth()) > 0)
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
	    	moveAction.setPosition(game.getGameHeight()
	    			, l.getX(game.getGameHeight()));
	    	this.addAction(moveAction);
	    	break;
	    case Top:
	    	moveAction.setPosition(0
	    			, l.getX(0));
	    	this.addAction(moveAction);
	    	break;
	    case Left:
	    	moveAction.setPosition(l.getY(0), 0);
	    	this.addAction(moveAction);
	    	break;
	    	
	    case Right:
	    	moveAction.setPosition(l.getY(game.getGameWidth())
	    			, l.getY(game.getGameWidth()));
	    	this.addAction(moveAction);
	    	break;
	    }
	    
	    
	}
 
    public void update()
    {
    	
        if (this.moving)
        {
        	Wall w = game.isHoreg(this);
        	if (w != null)
        	{
	            switch (w) {
				case Bottom:
					this.delta.x *= -1 ; 
					this.setX(game.getGameHeight());
					break;
				case Top:
					this.delta.x *= -1 ; 
					this.setX(0);
					break;
				case Left:
					this.delta.y *= -1 ;
					this.setY(0);
				case Right:
					this.delta.y *= -1 ;
					this.setY(game.getGameWidth());
					break;
				
				default:
					break;
				}
        	}
            move();
        }
        
    }
    
    public void move()
    {
    	this.setX((float) (this.getX() + this.speed * this.delta.x));
        this.setY((float) (this.getX() + this.speed * this.delta.y));	
    }
    
	
}
