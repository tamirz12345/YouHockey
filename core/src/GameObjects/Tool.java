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
    public Vector3 position;

    public Tool(String image_path) {
        this.img = new Texture(Gdx.files.internal(image_path));
        this.position = new Vector3(0, 240, 0);
        this.rec = new Rectangle(position.x, position.y, 64, 64);
    }

    public Vector3 move(int x, int y) {
        this.position.set(x - rec.width / 2 , y + rec.height / 2 ,0);
        return this.position;
    }



}
