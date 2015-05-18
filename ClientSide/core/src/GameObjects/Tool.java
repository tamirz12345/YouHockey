package GameObjects;

import javax.swing.plaf.SpinnerUI;

import android.util.Log;

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
           super.setPosition(50 , 20 );
            
        } else {
          
            super.setPosition(50 , 80 );
            
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
            super.setPosition((float) (game.getGameWidth()*0.5),(float)0.5 * game.calcMid());
            
        } else {
          
            super.setPosition((float) (game.getGameWidth()*0.5),(float)1.5 * game.calcMid());
            
        }
	}
    
	@Override
	public void setPosition(float x, float y) {
		double distance = Math.sqrt(Math.pow(x-  this.getX(), 2 )+ Math.pow(y-  this.getY(), 2 ));
		if (distance > this.R)
		{
			this.move(x,y);
		}
			
		else
		{
			Vector2 temp = game.validateTool(this , x , y);
			
			
			super.setPosition(temp.x, temp.y);
			if (game.isMultiplayer)
			{
				String msg = "907-"+x+"-"+y+"-";
				
				game.toSend.add(msg);
				Log.d("toolTamir", msg + "added to queue");
			}
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