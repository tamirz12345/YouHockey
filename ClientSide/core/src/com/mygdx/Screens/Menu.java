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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;



public class Menu extends ApplicationAdapter implements InputProcessor, Screen{
	YouHockey game;
	Rectangle PlayButton, PVPButton , Logo;
	Texture playTexture , pvpTexture , LogoTexture;
	Viewport viewport; //the viewport
	Limits lim;
	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 tempTouch;
	Vector2 temp1, temp2 , logoLoc;
	Music m ;
	boolean started = false;
	public Menu(YouHockey youHockey) {
    	this.game = youHockey;
    	
		this.create();
	}
	
	public void create () {
		
		batch = new SpriteBatch();
	    playTexture = new Texture(Gdx.files.internal("bot.png"));
	    pvpTexture = new Texture(Gdx.files.internal("pvp.png"));
	    LogoTexture = new Texture(Gdx.files.internal("uhockey.png"));
	    Logo = new Rectangle(0, 70 , 100 , 30);
	    PlayButton = new Rectangle(25, 50 , 50 , 20);
	    PVPButton = new Rectangle(25, 30 , 50 , 20 );
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
	  
	    UnitConvertor.draw(batch , LogoTexture ,Logo.x,Logo.y , Logo.height , Logo.width );
	    UnitConvertor.draw(batch , playTexture ,PlayButton.x,PlayButton.y , PlayButton.height , PlayButton.width );
	    UnitConvertor.draw(batch , pvpTexture ,PVPButton.x,PVPButton.y , PVPButton.height , PVPButton.width );
	    batch.end();
	    if (Gdx.input.isKeyPressed(Keys.BACK)){
			
			super.dispose();
			System.exit(0);
			
		}
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
	    
	    if (PlayButton.contains(pos.x, pos.y))
	    {
	    	game.setScreen(new MainGame(game));
	    	
	    }
	    else if (PVPButton.contains(pos.x, pos.y))
	    {
	    	game.setScreen(new MultiplayerLoadingScreen(game));
	    }
	 
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
