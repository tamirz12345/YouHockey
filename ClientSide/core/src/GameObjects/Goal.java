package GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.Screens.UnitConvertor;

/**
 * Created by user-pc on 19/02/2015.
 */
public class Goal {
    public Vector2 src;
    public Vector2 dst;
    public Limits game;
    public Goal(boolean isBottom , Limits game ) {
        if (isBottom) {
            this.src = new Vector2(game.leftGoal, game.bottom);
            this.dst = new Vector2(game.rightGoal, game.bottom);
            
        }
        else
        {
        	this.src = new Vector2( game.leftGoal, game.top);
            this.dst = new Vector2( game.rightGoal, game.top);
        }
        
        
        this.game = game;
        
    }
	public Vector2 getSrc(boolean origin) {
		if (origin)
			return UnitConvertor.toGame(src.x , src.y);
		return UnitConvertor.toGame(src.x , src.y );
		
	}
	public Vector2 getDst(boolean origin) {
		if (origin)
			return UnitConvertor.toGame(dst.x , dst.y);
		return UnitConvertor.toGame(dst.x , dst.y );
	}
    
    
    

}
