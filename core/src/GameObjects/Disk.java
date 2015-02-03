package GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Disk  extends Actor{
	Texture texture = new Texture("disk.png");
	@Override
	public void draw(Batch batch, float alpha){
        batch.draw(texture,super.getX(),super.getY());
    }
}
