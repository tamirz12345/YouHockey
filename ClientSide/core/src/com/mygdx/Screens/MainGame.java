package com.mygdx.Screens;

import GameObjects.Bot;
import GameObjects.Disk;
import GameObjects.Goal;
import GameObjects.Limits;
import GameObjects.Tool;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class MainGame  extends ApplicationAdapter implements InputProcessor, Screen  {
	YouHockey game;
	SpriteBatch batch;
	OrthographicCamera camera;
	Music music;
	Tool t1;
    Bot bot;
    //resetButton resetB;
	Vector3 tempTouch;
	float height,width;
	ShapeRenderer  shaper;
    final int TOOL_R = 5;
    StretchViewport viewport;
    Disk disk;
    Texture diskT ;
    Limits lim;
    Stage stage;
    float delta;
    Vector2 textBoxPos ;
    private String ScoreString;
    BitmapFont yourBitmapFontName;
    final int SCORE_TO_WIN = 3; 
    Vector3 worldCoordinates;
    Vector2 pos;
    public MainGame(YouHockey youHockey) {
    	this.game = youHockey;
		this.create();
	}

	public void create () {
    	lim = new Limits(null  , false);
		batch = new SpriteBatch();
		
		camera = new OrthographicCamera();
	    viewport = new StretchViewport(100,100,camera); //stretch screen to [0,100]x[0,100] grid
	    viewport.apply();
	    
	    camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0); //set camera to look at center of viewport
	    Gdx.input.setInputProcessor(this);

        t1 = new Tool("player2.png", true,  lim);
        bot = new Bot("player2.png", false , lim);
        
        tempTouch = new Vector3();
        
        disk = new Disk(lim , 0);
        disk.spawn();
        stage = new Stage();
        stage.addActor(bot);
        stage.addActor(t1);
        stage.addActor(disk);
        
        Gdx.input.setCatchBackKey(true);
	    music = Gdx.audio.newMusic(Gdx.files.internal("backroundMusic.mp3"));
	    music.setLooping(true);
	    music.play();
	    shaper = new ShapeRenderer();
	    
	    
	    ScoreString = "0 : 0";
	    yourBitmapFontName =new  BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"), false);
	    
	}

	public void render (float delta) {
		camera.update();
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
        Goal bottomGoal = new Goal(true , lim );
        Goal topGoal = new Goal(false , lim );
        
        disk.update(t1, bot);
        bot.update(disk);
        Vector2 leftB = lim.leftBottomCorner();
        Vector2 leftT = lim.leftTopCorner();
        Vector2 rightB = lim.rightBottomCorner();
        Vector2 rightT = lim.rightTopCorner();
        shaper.setProjectionMatrix(camera.combined);
        shaper.begin(ShapeType.Line);
        Gdx.gl20.glLineWidth(20);
        
        Vector2  src=  bottomGoal.getSrc(true);
        Vector2  dst = bottomGoal.getDst(true);
        
        shaper.line(src.x , src.y , leftB.x , leftB.y , 
        		Color.YELLOW , Color.YELLOW);
        shaper.line(dst.x , dst.y , rightB.x , rightB.y , 
        		Color.YELLOW , Color.YELLOW);
        src=  topGoal.getSrc(true);
        dst = topGoal.getDst(true);
        shaper.line(src.x , src.y , leftT.x , leftT.y , 
        		Color.YELLOW , Color.YELLOW);
        shaper.line(dst.x , dst.y , rightT.x , rightT.y , 
        		Color.YELLOW, Color.YELLOW);
        shaper.line(rightB.x , rightB.y , rightT.x , rightT.y , 
        		Color.GREEN , Color.GREEN);
        shaper.line(leftB.x ,leftB.y , leftT.x , leftT.y , 
        		Color.GREEN , Color.GREEN);
        
        
		
        shaper.line(lim.calcMid(), lim.getLeft(), lim.calcMid(),
        		lim.getRight(),Color.BLACK,Color.BLACK);  
        shaper.end();
        
        
        
        
        
        batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		yourBitmapFontName.setColor(Color.BLACK);
		yourBitmapFontName.setScale(0.75f);
		textBoxPos = bottomGoal.getSrc(true);
		textBoxPos=  UnitConvertor.toGame(textBoxPos.x , textBoxPos.y);
		
		ScoreString = Integer.toString(lim.getScoreTop()) + "  " + 
				Integer.toString(lim.getScoreBottom());
		yourBitmapFontName.draw(batch, ScoreString,25,100);
		t1.draw(batch);
		bot.draw(batch);
		disk.draw(batch);
		batch.end();

		
		
		if (Gdx.input.isKeyPressed(Keys.BACK)){
			music.stop();
			super.dispose();
			game.setScreen(new Menu(game));
			
		}
		stage.act(delta);
		
		if (lim.getScoreBottom() == SCORE_TO_WIN)
		{
			music.stop();
			super.dispose();
			game.setScreen(new EndGame(game, true));
		}
			
		if (lim.getScoreTop() == SCORE_TO_WIN)
		{
			music.stop();
			super.dispose();
			game.setScreen(new EndGame(game, false));
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
		/*
		worldCoordinates = camera.unproject(new Vector3(screenX,screenY,0)); //obtain the touch in world coordinates: similar to InputTransform used above
	    Gdx.app.log("Mouse Event","Click at " + worldCoordinates.x + "," + worldCoordinates.y);
	    Vector2 pos = UnitConvertor.toNormal(worldCoordinates.x, worldCoordinates.y);
	    Gdx.app.log("Mouse Event","Projected at " + pos.x + "," + pos.y);
	    t1.move(pos.x, pos.y);
        bot.move(t1.getX(), lim.getGameHeight() - t1.getY());
	    */
	    
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
		
		Vector3 worldCoordinates = camera.unproject(new Vector3(screenX,screenY,0));
		Gdx.app.log("Mouse Event","Click at " + worldCoordinates.x + "," + worldCoordinates.y);
	    Vector2 pos = UnitConvertor.toNormal(worldCoordinates.x, worldCoordinates.y);
	   
	    Gdx.app.log("Mouse Event","Projected at " + pos.x + "," + pos.y);
	    if (pos.y > lim.calcMid() - t1.getHeight())
	    {
	    	pos.y = lim.calcMid() - t1.getHeight();
	    }
	    t1.setPosition(pos.x, pos.y);
	    //bot.setPosition(pos.x, lim.getGameHeight() - pos.y);
	    
		return false;
		
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	 public void resize(int width, int height){
	    viewport.update(width, height);
	    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
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
