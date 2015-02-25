package GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.UnitConvertor;

/**
 * Created by user-pc on 25/01/2015.
 */

public class Tool  extends Actor{
    public Texture img;
    
    Vector2 target;
    Vector2 delta;
    boolean isBelow;
    int TimeToMove ;
    int moves;
    float radius  ;
    Limits game ;
    
    public Tool(String image_path, boolean isBelow,float radius , Limits game) {
        this.img = new Texture(Gdx.files.internal(image_path));
        this.isBelow = isBelow;
        this.game = game;
        this.setWidth((float) (radius * game.getxUnit()));
        this.setHeight(this.getWidth());
        if (this.isBelow) {
            this.setPosition((float) (game.getGameWidth()*0.5),(float) (game.getGameHeight() * (game.getMid() - 0.3)));
            this.TimeToMove = 3;
        } else {
          
            this.setPosition((float) (game.getGameWidth()*0.5),(float) (game.getGameHeight() * (game.getMid() + 0.3)));
            this.TimeToMove = 10;
        }
        this.radius  = this.getWidth()   / 2 ; 
       
    }

    public void move(float x, float y) {

    	Vector2 temp = game.validateTool(this , x , y);
        
        this.clearActions();
        game.addMoveToAction(this, x, y);
        
        
    }

    

	

	

	
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		//this.update();
		Vector2 v = UnitConvertor.toGame(this.getX(), this.getY());
		batch.draw(img , v.x , v.y , this.getWidth() , this.getHeight());
	}
    
    
    
}