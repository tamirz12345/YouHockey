package GameObjects;



import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class resetButton  extends Actor{
	Texture img = new Texture("reset.png");

	public resetButton() {
		this.setPosition(64, 0);
		
	}
	
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(img , this.getX() , this.getY());
	}
    
}
