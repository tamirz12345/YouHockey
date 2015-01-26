package com.mygdx.game;

import GameObjects.Tool;

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

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	private OrthographicCamera camera;
	private Music music;
	Tool t1;
	Vector3 tempTouch;
	float height,width;
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
	    //camera.setToOrtho(false, 800, 480);
		height= Gdx.graphics.getHeight();
		width= Gdx.graphics.getWidth();
		camera.setToOrtho(false, width,height);
        t1 = new Tool("player1.png");
        tempTouch= new Vector3();
	    music = Gdx.audio.newMusic(Gdx.files.internal("backroundMusic.mp3"));
	    music.setLooping(true);
	    music.play();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(t1.img, t1.rec.x, t1.rec.y);
		batch.end();
		
		if(Gdx.input.isTouched()) {
			tempTouch.set(Gdx.input.getX(), Gdx.input.getY(),0);
			camera.unproject(tempTouch);
            t1.move(tempTouch.x, tempTouch.y);
	        
	    }
	}
}
