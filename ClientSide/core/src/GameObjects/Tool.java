package GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.Screens.UnitConvertor;

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
    float R  ;
    Limits game ;
    Character typeC ;
    public Tool(String image_path, boolean isBelow,float radius , Limits game) {
        this.img = new Texture(Gdx.files.internal(image_path));
        this.isBelow = isBelow;
        this.game = game;
        this.R  = (float) (radius *  game.getxUnit());
        this.setWidth((float) (R * 2));
        this.setHeight(this.getWidth());
        if (this.isBelow) {
            this.setPosition((float) (game.getGameWidth()*0.5),(float)0.5 * game.calcMid());
            this.TimeToMove = 3;
        } else {
          
            this.setPosition((float) (game.getGameWidth()*0.5),(float)1.5 * game.calcMid());
            this.TimeToMove = 10;
        }
        
       
    }

    public void move(float x, float y) {

    	Vector2 temp = game.validateTool(this , x , y);
        
        this.clearActions();
        
        if (isBelow)
        	typeC  = 'h';
        else
        	typeC  = 'e';
        game.addMoveToAction(this, temp.x, temp.y, typeC);
        
        
    }

    

    public void move(float x, float y, float time) {

    	Vector2 temp = game.validateTool(this , x , y);
        
        this.clearActions();
        game.addMoveToAction(this, temp.x, temp.y,time);
        
        
    }

	

	
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		//this.update();
		float densityIndependentSize = Gdx.graphics.getDensity();
		Vector2 v = UnitConvertor.toGame(this.getX(), this.getY());
		batch.draw(img , v.x , v.y , this.getWidth() , this.getHeight());
	}
    
    
    
}