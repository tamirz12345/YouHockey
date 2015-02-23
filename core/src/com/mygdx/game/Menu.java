package com.mygdx.game;

import GameObjects.Disk;
import GameObjects.Goal;
import GameObjects.Limits;
import GameObjects.Tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Menu extends ScreenAdapter{
	YouHockey game;
	Rectangle PlayButton;
	Texture playTexture;
	Limits lim;
	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 tempTouch;
	Vector2 temp;
	public Menu(YouHockey youHockey) {
    	this.game = youHockey;
		this.create();
	}

	public void create () {
    	lim = new Limits();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		
		camera.setToOrtho(false, lim.getGameHeight(), lim.getGameWidth());
        PlayButton = new Rectangle((float)(lim.getGameWidth() * 0.2),
        		(float) (lim.getGameHeight() * 0.4),(float)300.0, (float)300.0);
        temp = UnitConvertor.toGame(PlayButton.x , PlayButton.y);
        tempTouch = new Vector3();
        playTexture = new Texture("playButton.png");
	}

	public void render (float delta) {
		Gdx.gl.glClearColor(1, 10, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(playTexture, temp.x, temp.y, PlayButton.width, PlayButton.height);
		batch.end();
		if(Gdx.input.isTouched()) {
			camera.unproject(tempTouch.set(Gdx.input.getX(), Gdx.input.getY(),0));
			tempTouch = UnitConvertor.toNormal(tempTouch);
            if (PlayButton.contains(tempTouch.x, tempTouch.y))
            {
            	game.setScreen(new MainGame(game));
            }
	    }
		
	}
	
	
	
}
