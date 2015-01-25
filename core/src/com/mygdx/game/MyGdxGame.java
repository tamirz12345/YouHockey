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

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
	    //camera.setToOrtho(false, 800, 480);
		float h = Gdx.graphics.getHeight();
		float w= Gdx.graphics.getWidth();
		camera.setToOrtho(false, w,h);
        t1 = new Tool("player1.png");

	    music = Gdx.audio.newMusic(Gdx.files.internal("backroundMusic.mp3"));
	    music.setLooping(true);
	    music.play();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(t1.img, t1.position.x, t1.position.y);
		batch.end();
		
		if(Gdx.input.isTouched()) {
            t1.move(Gdx.input.getX(), Gdx.input.getY());
	        camera.unproject(t1.position);
	    }
	}
}
