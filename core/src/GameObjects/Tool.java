package GameObjects;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by user-pc on 25/01/2015.
 */

public class Tool {
    public Texture img;
    public Rectangle rec;
    
    public Tool(String image_path) {
        this.img = new Texture(Gdx.files.internal(image_path));
        this.rec = new Rectangle(0, 240, 64, 64);
    }

    public void move(float x, float y) {
    	float temp = (float) (Gdx.graphics.getWidth()*0.5);
    	/*
    	if (x <= Gdx.graphics.getWidth() * 0.5)
    	{
    		this.rec.x = x + rec.height / 2 ;
    		
    	}
    	else
    	{
    		this.rec.x = temp - this.rec.height;
    		
    	}
    	*/ 
    	this.rec.x = (float) Math.min(x, Gdx.graphics.getWidth() * 0.5)   - rec.height / 2;
    	this.rec.y = y - rec.width / 2 ;
    	
        
    }
 


}
