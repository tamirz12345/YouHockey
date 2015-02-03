package GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by user-pc on 25/01/2015.
 */

public class Tool {
    public Texture img;
    public Rectangle rec;
    Vector2 target;
    Vector2 delta;
    boolean isBelow;
    final int TimeToMove = 5;
    int moves;
    Limits limit = new Limits();

    public Tool(String image_path, boolean isBelow, int height, int width) {
        this.img = new Texture(Gdx.files.internal(image_path));
        this.isBelow = isBelow;

        if (this.isBelow) {
            this.rec = new Rectangle(700, 240, width, height);
        }
        else {
          this.rec = new Rectangle(100, 240, width, height);
        }

        target = null;
        delta = null;
        moves = 0;
    }

    public void move(float x, float y) {
    	target = new Vector2(x,y);
    	target = limit.validateTool(target, this.isBelow, this.rec.getHeight(), this.rec.getWidth(), 50);

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


/**
 * Created by user-pc on 02/02/2015.
 */
public static class Limits {

    public Limits()
    {}

    public Vector2 validateTool(Vector2 pos, boolean isBelow, float height, float width, int limitPercent)
    {
        Vector2 valid = new Vector2();
        if (isBelow) {
            valid.x = (float) Math.max(pos.x, Gdx.graphics.getWidth() * (limitPercent / 100))   - width / 2;
            valid.y = pos.y - height / 2 ;
        }

        else {
            valid.x = (float) Math.min(pos.x, Gdx.graphics.getWidth() * (limitPercent / 100))   - width / 2;
            valid.y = pos.y - height / 2 ;
        }

        return valid;
    }
    }
}
