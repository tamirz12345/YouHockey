package com.mygdx.game;

import GameObjects.Limits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Menu extends ScreenAdapter{
	YouHockey game;
	Rectangle PlayButton;
	Texture playTexture;
	Limits lim;
	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 tempTouch;
	Vector2 temp;
	Music m ;
	boolean started = false;
	public Menu(YouHockey youHockey) {
    	this.game = youHockey;
		this.create();
	}

	public void create () {
    	lim = new Limits();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		
		camera.setToOrtho(false, lim.getGameHeight(), lim.getGameWidth());
		temp = UnitConvertor.toGame(lim.getGameWidth() / 2 , lim.getGameHeight() /2 );
		m =  Gdx.audio.newMusic(Gdx.files.internal("areYouReadyKids.mp3"));
        PlayButton = new Rectangle(temp.x,
        		temp.y,(float)300.0, (float)300.0);
        
        tempTouch = new Vector3();
        playTexture = new Texture("playButton.png");
        Gdx.input.setCatchBackKey(true);
	}

	public void render (float delta) {
		Gdx.gl.glClearColor(1, 10, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		
		batch.draw(playTexture, temp.x, temp.y);
		
		batch.end();
		if (started && !m.isPlaying())
		{
			game.setScreen(new MainGame(game));
		}
		if(Gdx.input.isTouched()) {
			camera.unproject(tempTouch.set(Gdx.input.getX(), Gdx.input.getY(),0));
			//tempTouch = UnitConvertor.toNormal(tempTouch);
			
            if (PlayButton.contains(tempTouch.x, tempTouch.y))
            {
            	m.play();
            	started = true;
            	
            }
	    }
		
		if (Gdx.input.isKeyPressed(Keys.BACK)){
			System.exit(0);
			
			
			
		}
		
	}
	
	
	
}
