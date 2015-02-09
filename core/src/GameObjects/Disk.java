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
	float m, n; // y = mx + n
    boolean start = false;
    float speed = 1;
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
        	this.m = (diskY - toolY) / (diskX - toolX);
            this.start = true;
            this.update();
        }
    }
	
	private void update() {
        // y - y0 = m (x - x0)
        // y = mx + y0 - mx0
        this.n = this.getY() - this.m * this.getX();
    }
 
    public void move()
    {
        if (this.start)
        {
            this.setX(this.getX() + this.speed);
            this.setY(this.getX() * this.m + this.n);
            if (game.isHoreg(this))
            {
            	this.m *= -1;
            }
        }
        
    }
	
}
