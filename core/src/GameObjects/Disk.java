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
	Vector2 targetLoc = null;
	boolean moving;
	float m, n; // y = mx + n
    double speed = 0.05;
	Limits game;
	Wall targetW = null;
	Wall wY = null , wX = null;
	Line l ;
	public Disk(Limits game) {
		super();
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
            l = new Line(a,b);
            
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
		
	    
	    
	    if (XDirection == Wall.Top && YDirection == Wall.Left)
	    {
	    	if (l.getY(0) > 0 && l.getY(0)<game.getGameHeight())
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
	    	if (l.getY(game.getGameWidth()) >0  && l.getY(game.getGameWidth()) < game.getGameHeight())
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
	    	if (l.getY(0) <game.getGameHeight() && l.getY(0) > 0 )
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
	    	if (l.getY(game.getGameWidth()) > 0 && l.getY(game.getGameWidth()) < game.getGameHeight())
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
	    	this.addMoveToAction(game.getGameHeight()
	    			, l.getX(game.getGameHeight()));
	    	break;
	    case Top:
	    	this.addMoveToAction(this.getHeight()
	    			, l.getX(0));
	    	break;
	    case Left:
	    	this.addMoveToAction(l.getY(0), 0);
	    	break;
	    	
	    case Right:
	    	this.addMoveToAction(l.getY(game.getGameWidth())
	    			, game.getGameWidth() - this.getWidth());
	    	break;
	    }
	    
	    
	}
	
	
	
	public void addMoveToAction(float x , float y)
	{
		MoveToAction moveAction = new MoveToAction();
		float oneF = 1f;
		float speed = (int) Math.sqrt(Math.pow(this.getX()-  x , 2) 
				+ Math.pow(this.getY()-y, 2)) / 100 * oneF;
		moveAction.setDuration(speed);
		moveAction.setPosition(x, y);
		this.addAction(moveAction);
		this.targetLoc = new Vector2(x,y);
	}
 
    public void update(Tool t1 , Tool t2)
    {
    	this.checkCollision(t1);
		this.checkCollision(t2);
		//To DO :
    	//Check if it near one of the walls and if it does  so decide the next action using the function
    	//LineToAction(Line l , Wall XDirection , Wall YDirection )
        if (this.getActions().size == 0)// Not Moving , there is a chanse it is near a wall now, it need to be handeld 
        {
        	if (targetLoc!= null && this.getX() == targetLoc.x && this.getY() == targetLoc.y)
        	{
        		float newA = this.l.getA() * (- 1);
        		float newB = this.getX() - this.getX() * newA; 
        		l = new Line(newA, newB);
        		if (targetW == Wall.Bottom)
        			wX = Wall.Top;
        		if (targetW == Wall.Top)
        			wX = Wall.Bottom;
        		if (targetW == Wall.Left)
        			wY = Wall.Right;
        		if (targetW == Wall.Right)
        			wY = Wall.Left;
        		LineToAction(l, wX , wY);
        	}
        	
        	
        }
        if (!game.inGameBounds(this))
    	{
    		this.spawn();
    	}
    }


	public void spawn() {
		this.clearActions();
		this.setPosition((float) (game.getGameHeight() * 0.7 - this.getHeight()/2),game.getGameWidth()/2 - this.getWidth()/2);
	}
    
   
}
