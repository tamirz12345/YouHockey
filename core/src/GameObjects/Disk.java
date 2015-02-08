package GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

public class Disk  extends Actor{
	Texture texture = new Texture("disk.png");
	float radius = 32 ; 
	boolean toHuman;
	Vector2 delta;
	boolean moving;
	
	
	
	
	public Disk() {
		super();
		delta = null;
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
            if (diskX != 0 || diskY != 0) {
                this.setX(0);
                this.setY(0);
            }
            else {
            	this.setX(100);
                this.setY(100);
            }
        }
    }
	
	
}
