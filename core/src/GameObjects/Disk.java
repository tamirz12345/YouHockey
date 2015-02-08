package GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

public class Disk  extends Actor{
	Texture texture = new Texture("disk.png");
	boolean toHuman;
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
	
	
}
