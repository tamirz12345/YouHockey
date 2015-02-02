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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by user-pc on 25/01/2015.
 */

public class Tool {
    public Texture img;
    public Rectangle rec;
    Vector2 target;
    Vector2 delta;
    final int TimeToMove = 5;
    int moves;
    public Tool(String image_path) {
        this.img = new Texture(Gdx.files.internal(image_path));
        this.rec = new Rectangle(700, 240, 64, 64);
        target = null;
        delta = null;
        moves = 0;
        
    }

    public void move(float x, float y) {
    	float temp = (float) (Gdx.graphics.getWidth()*0.5);
    	target = new Vector2();
    	target.x = (float) Math.max(x, Gdx.graphics.getWidth() * 0.5)   - rec.height / 2;
    	target.y = y - rec.width / 2 ;
    	delta = new Vector2();
    	delta.x = (target.x - rec.x) / TimeToMove;
    	delta.y =  (target.y - rec.y) / TimeToMove;
    	moves = 0;
        
    }
    
    public void update()
    {
    	if (target == null)
    	{
    		return;
    	}
    	if (moves < TimeToMove)
    	{
    		rec.x += delta.x;
        	rec.y += delta.y;
        	moves++;
        	
    	}
    	else
    	{
    		moves = 0;
    		target = null;
    		delta = null;
    	}
    	
    	
    	
    	
    	
    	
    }
 


}
