package com.mygdx.Screens;

import GameObjects.Limits;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class EndGame  extends ApplicationAdapter implements InputProcessor, Screen {
	
	YouHockey game;
	Rectangle menuButton;
	Texture menuTexture; // menu = pvp
	Limits lim;
	public SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 tempTouch;
	Vector2 temp1, temp2;
	Music m ;
	boolean started = false;
	boolean isVictory; 
	Viewport viewport;
	public static Texture backgroundTexture;
    public static Sprite backgroundSprite;
    
	
	public EndGame(YouHockey youHockey, boolean isVictory) {
    	this.game = youHockey;
    	this.isVictory = isVictory; 
		this.create();
	}

	public void create () {
		if (isVictory) {
			backgroundTexture = new Texture(Gdx.files.internal("victory-11.png"));
		}
		else {
			backgroundTexture = new Texture(Gdx.files.internal("defeat.png"));
		}
		
		batch = new SpriteBatch();
		
		camera = new OrthographicCamera();
	    viewport = new StretchViewport(100,100,camera); //stretch screen to [0,100]x[0,100] grid
	    viewport.apply();
	    
	    camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0); //set camera to look at center of viewport
		
        

        tempTouch = new Vector3();
        menuTexture = new Texture("MENUBTN.jpg");
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(this);
	}

	public void render (float delta) {
		camera.update();
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		UnitConvertor.draw(batch , backgroundTexture , 0 , 0 , 50 ,50);
		batch.end();
		
		
		if(Gdx.input.isTouched()) {
			camera.unproject(tempTouch.set(Gdx.input.getX(), Gdx.input.getY(),0));

            if (menuButton.contains(tempTouch.x, tempTouch.y))
            {
                game.setScreen(new Menu(game));
            }
	    }
		
		if (Gdx.input.isKeyPressed(Keys.BACK)){
			
			game.setScreen(new Menu(game));
		}
		
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
