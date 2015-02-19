package GameObjects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by user-pc on 19/02/2015.
 */
public class Goal {
    public Vector2 src;
    public Vector2 dst;

    public Goal(int isBottom) {
        if (isBottom == 1) {
            this.src = new Vector2(0, 50);
            this.dst = new Vector2(20, 70);
        }
    }

}
