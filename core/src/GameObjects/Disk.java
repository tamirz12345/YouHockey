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
	boolean toHuman;
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
        float diskX = this.getX() + this.getWidth() / 2;
        float diskY = this.getY() + this.getHeight() / 2;

        float toolX = tool.getX() + tool.getWidth() / 2;
        float toolY = tool.getY() + tool.getHeight() / 2;

        if (Math.sqrt( Math.pow(diskX - toolX, 2) + Math.pow(diskY - toolY, 2))
        		<= this.radius + tool.radius)
        {
        	delta = new Vector2( diskX - toolX, diskY - toolY);
            this.moving = true;
            
        }
    }
	
	
 
    public void update()
    {
        if (this.moving)
        {
            
            Set<Wall> w = game.isHoreg(this);
            if (w.size() == 2)
            {
            	delta.x *= -1;
            	delta.y *= -1;
            	move();
            	return;
            }
            
        	if (w.contains(Wall.Bottom) ||  w.contains(Wall.Top))
        	{
        		delta.x *= -1;
        		move();
        		return;
        	}
        	else if (w.contains(Wall.Left) || w.contains(Wall.Right))
        	{
        		delta.y *= -1 ; 
        		move();
        		return;
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
