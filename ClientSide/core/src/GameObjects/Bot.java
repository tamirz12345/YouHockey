package GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Bot extends Tool{
		boolean kadosh = false;
	    public Bot(String image_path, boolean isBelow, Limits game) {
	    	super(image_path, false, game);
	    }
	    
	    public void update(Disk d) {
	    	if (kadosh)
	    	{
	    		if (this.getActions().size >0)
	    			return;
	    		kadosh = false;
	    	}
	    		
	    	if (d.getY() >= game.calcMid() )
	    	{
	    		if (d.getY() > this.getY()) // if disk is behind
	    		{
	    			this.setPosition(game.getGameWidth()/2, game.getTop());
	    			kadosh = true;
	    		}
	    		else 
	    		{
	    			this.setPosition(d.getX(), d.getY());
	    		}
	    	}
	    	else 
	    	{
	    		this.setPosition(d.getX(), game.getTop());
	    	}
	    }
	    	
}

