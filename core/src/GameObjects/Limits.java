package GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by user-pc on 03/02/2015.
 */
public class Limits {
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

