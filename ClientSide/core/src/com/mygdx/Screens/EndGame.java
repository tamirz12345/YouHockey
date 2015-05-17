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
	public static Texture backgroundTexture , texture;
    public static Sprite backgroundSprite;
    
	
	public EndGame(YouHockey youHockey, boolean isVictory) {
    	this.game = youHockey;
    	this.isVictory = isVictory; 
		this.create();
	}

	public void create () {
		batch = new SpriteBatch();
		if (isVictory)
		{
			texture = new Texture(Gdx.files.internal("victory-11.png"));
		}
		else
		{
			texture = new Texture(Gdx.files.internal("defeat.png"));
		}
	   
	    

	    camera = new OrthographicCamera();
	    viewport = new StretchViewport(100,100,camera); //stretch screen to [0,100]x[0,100] grid
	    viewport.apply();

	    camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0); //set camera to look at center of viewport
	    Gdx.input.setInputProcessor(this);
	}

	public void render (float delta) {
		 	camera.update();
		    Gdx.gl.glClearColor(1, 1, 1, 1);
		    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		    batch.setProjectionMatrix(camera.combined); //make batch draw to location defined by camera
		    batch.begin();
		  
		    UnitConvertor.draw(batch ,  texture ,0,0 , 100 , 100 );
		    batch.end();
		
	}

	@Override
	 public void dispose(){
	    
	 }

	 @Override
	 public void resize(int width, int height){
	    viewport.update(width, height);
	    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
	 }

	 @Override
	 public boolean keyDown(int keycode) {
	    return false;
	 }

	 @Override
	 public boolean keyUp(int keycode) {
	    return false;
	 }

	 @Override
	 public boolean keyTyped(char character) {
	    return false;
	 }

	 @Override
	 public boolean touchDown(int screenX, int screenY, int pointer, int button) {

	    
	    Vector3 worldCoordinates = camera.unproject(new Vector3(screenX,screenY,0)); //obtain the touch in world coordinates: similar to InputTransform used above
	    Gdx.app.log("Mouse Event","Click at " + worldCoordinates.x + "," + worldCoordinates.y);
	    Vector2 pos = UnitConvertor.toNormal(worldCoordinates.x, worldCoordinates.y);
	    Gdx.app.log("Mouse Event","Projected at " + pos.x + "," + pos.y);
	    return false;
	 }

	 @Override
	 public boolean touchUp(int screenX, int screenY, int pointer, int button) {
	    return false;
	 }

	 @Override
	 public boolean touchDragged(int screenX, int screenY, int pointer) {
	    return false;
	 }

	 @Override
	 public boolean mouseMoved(int screenX, int screenY) {
	    return false;
	 }

	 @Override
	 public boolean scrolled(int amount) {
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
