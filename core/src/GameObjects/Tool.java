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
    public Rectangle rec;
    Vector2 target;
    Vector2 delta;
    boolean isBelow;
    int TimeToMove ;
    int moves;
    float radius = 32 ;
    Limits limit ;

    public Tool(String image_path, boolean isBelow, int height, int width , Limits game) {
        this.img = new Texture(Gdx.files.internal(image_path));
        this.isBelow = isBelow;
        this.limit = game;
        if (this.isBelow) {
            this.rec = new Rectangle((float) (game.getGameWidth()*0.5),(float) (game.getGameHeight() * (game.getMid() - 0.3))
            		, width, height);
            this.TimeToMove = 3;
        } else {
            this.rec = new Rectangle((float) (game.getGameWidth()*0.5),(float) (game.getGameHeight() * (game.getMid() + 0.3))
            		, width, height);
            this.TimeToMove = 10;
        }

        target = null;
        delta = null;
        moves = 0;
    }

    public void move(float x, float y) {
        target = new Vector2(x, y);
        target = limit.validateTool(target, this.isBelow, this.rec.getHeight(), this.rec.getWidth());

        delta = new Vector2();
        delta.x = (target.x - rec.x) / TimeToMove;
        delta.y = (target.y - rec.y) / TimeToMove;
        moves = 0;
    }

    public void update() {
        if (target == null) {
            return;
        }
        if (moves < TimeToMove) {
            rec.x += delta.x;
            rec.y += delta.y;
            moves++;
        } else {
            moves = 0;
            target = null;
            delta = null;
        }
        
        
        
    }

	@Override
	public float getX() {
		return this.rec.x;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return this.rec.y;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		this.update();
		Vector2 v = UnitConvertor.toGame(rec.x, rec.y);
		batch.draw(img , v.x , v.y);
	}
    
    
    
}