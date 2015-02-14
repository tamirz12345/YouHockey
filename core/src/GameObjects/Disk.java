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
	Wall targetW = null;
	
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
	
	
	/*
	 * Pre Condition : Recives line it wants to move by it 
	 * 				   XDirectino is bottom or top
	 * 				   YDriection is left ot right
	 * What id does is giving the line and the directions it needs to moves it generates
	 * the next action and adds it to the action of this disk
	 */
	public void LineToAction(Line l , Wall XDirection , Wall YDirection )
	{
		MoveToAction moveAction = new MoveToAction();
	    moveAction.setDuration(4f);
	    
	    
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
 
    public void update(Tool t1 , Tool t2)
    {
    	this.checkCollision(t1);
		this.checkCollision(t2);
        if (this.getActions().size == 0)// Not Moving , there is a chanse it is near a wall now, it need to be handeld 
        {
        	//To DO :
        	//Check if it near one of the walls and if it does  so decide the next action using the function
        	//LineToAction(Line l , Wall XDirection , Wall YDirection )
        	
        }
        
    }
    
   
}
