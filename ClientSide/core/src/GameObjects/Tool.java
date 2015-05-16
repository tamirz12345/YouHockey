package GameObjects;

import javax.swing.plaf.SpinnerUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    public Tool(String image_path, boolean isBelow, Limits game) {
        this.img = new Texture(Gdx.files.internal(image_path));
        this.game = game;
        this.isBelow = isBelow;
        this.R= 3 ; 
        this.setWidth(2 * R);
        this.setHeight(2 * R);
        
        if (this.isBelow) {
            this.setPosition(50 , 20 );
            
        } else {
          
            this.setPosition(50 , 80 );
            
        }
    }

    public Tool(String image_path, boolean isBelow, float w, float h, Limits lim) {
    	this.img = new Texture(Gdx.files.internal(image_path));
        this.isBelow = isBelow;
        this.game = lim;
        this.R  = w;
        this.setWidth(2*w);
        this.setHeight(2*h);
        if (this.isBelow) {
            this.setPosition((float) (game.getGameWidth()*0.5),(float)0.5 * game.calcMid());
            
        } else {
          
            this.setPosition((float) (game.getGameWidth()*0.5),(float)1.5 * game.calcMid());
            
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

	

	
	
	public void draw(SpriteBatch batch) {
		//this.update();
		
		UnitConvertor.draw(batch , img, this.getX(),this.getY(), this.getWidth() , this.getHeight());
		
	}
    
    
    
}