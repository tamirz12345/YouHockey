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

public class EndGame  extends ApplicationAdapter implements InputProcessor, Screen {
	
	YouHockey game;
	Rectangle menuButton;
	Texture menuTexture; // menu = pvp
	Limits lim;
	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 tempTouch;
	Vector2 temp1, temp2;
	Music m ;
	boolean started = false;
	boolean isVictory; 
	public static Texture backgroundTexture;
    public static Sprite backgroundSprite;
    private SpriteBatch spriteBatch;
	
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
		
        spriteBatch = new SpriteBatch();
    	lim = new Limits(null);
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, lim.getGameHeight(), lim.getGameWidth());
		temp1 = UnitConvertor.toGame((float)lim.getxUnit() * 20 , (float) (lim.getyUnit() * 70));

        temp2 = UnitConvertor.toGame((float)lim.getxUnit() * 20, (float) (lim.getyUnit() * 50));
        menuButton = new Rectangle(temp2.x,
                temp2.y,(float) ((float)20 * lim.getxUnit()),
        		(float) ((float)40 * lim.getyUnit()));

        tempTouch = new Vector3();
        menuTexture = new Texture("MENUBTN.jpg");
        Gdx.input.setCatchBackKey(true);
	}

	public void render (float delta) {
		Gdx.gl.glClearColor(1, 10, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(backgroundTexture, 0 , 0  , lim.getGameHeight(),lim.getGameWidth());
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
