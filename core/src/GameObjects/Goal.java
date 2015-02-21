package GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.UnitConvertor;

/**
 * Created by user-pc on 19/02/2015.
 */
public class Goal {
    public Vector2 src;
    public Vector2 dst;
    public Limits game;
    public float height ;
    public Goal(boolean isBottom , Limits game , float height) {
        if (isBottom) {
            this.src = new Vector2((float) (game.getGameWidth() * 0.4)
            		, (float) (0.05 * game.getGameHeight()));
            this.dst = new Vector2((float )(game.getGameWidth() * 0.6), 
            		(float) (0.05 * game.getGameHeight()));
            
        }
        
        
        
        this.game = game;
        this.height = height;
    }
	public Vector2 getSrc(boolean origin) {
		if (origin)
			return UnitConvertor.toGame(src.x , src.y);
		return UnitConvertor.toGame(src.x , src.y + this.height);
		
	}
	public Vector2 getDst(boolean origin) {
		if (origin)
			return UnitConvertor.toGame(dst.x , dst.y);
		return UnitConvertor.toGame(dst.x , dst.y + this.height);
	}
    
    
    

}
