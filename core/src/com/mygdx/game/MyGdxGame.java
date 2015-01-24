package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture player1;
	private Vector3 p1Pos;
	private OrthographicCamera camera;
	Rectangle p1Rec;
	
	
	
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		player1 = new Texture("player.jpg");
		p1Pos= new Vector3(0, 240, 0);
		camera = new OrthographicCamera();
	    camera.setToOrtho(false, 800, 480);
	    
	    
	    
	    
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(player1, p1Pos.x, p1Pos.y);
		batch.end();
		
		if(Gdx.input.isTouched()) {
	          p1Pos.set(Gdx.input.getX(), Gdx.input.getY(),0);
	          camera.unproject(p1Pos);
	          
	    }
		
		
		
	}
}
