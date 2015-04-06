package com.mygdx.game;

import GameObjects.Disk;
import GameObjects.Goal;
import GameObjects.Limits;
import GameObjects.Tool;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class MainGame extends ScreenAdapter {
	YouHockey game;
	SpriteBatch batch;
	OrthographicCamera camera;
	Music music;
	Tool t1;
    Tool bot;
    //resetButton resetB;
	Vector3 tempTouch;
	float height,width;
	ShapeRenderer  shaper;
    final int TOOL_R = 5;
    
    Disk disk;
    Texture diskT ;
    Limits lim;
    Stage stage;
    float delta;

    public MainGame(YouHockey youHockey) {
    	this.game = youHockey;
		this.create();
	}

	public void create () {
    	lim = new Limits();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		height = Gdx.graphics.getWidth();
		width = Gdx.graphics.getHeight();
		camera.setToOrtho(false, height, width);
        t1 = new Tool("player2.png", true, TOOL_R , lim);
        bot = new Tool("player2.png", false, TOOL_R,lim);
        
        tempTouch = new Vector3();
        
        disk = new Disk(lim);
        disk.spawn();
        stage = new Stage();
        stage.addActor(bot);
        stage.addActor(t1);
        stage.addActor(disk);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
	    music = Gdx.audio.newMusic(Gdx.files.internal("backroundMusic.mp3"));
	    music.setLooping(true);
	    music.play();
	    shaper = new ShapeRenderer();
	}

	public void render (float delta) {
		Gdx.gl.glClearColor(1, 10, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//delta = Gdx.graphics.getDeltaTime();
        Goal bottomGoal = new Goal(true , lim );
        Goal topGoal = new Goal(false , lim );
        disk.update(t1, bot);
        Vector2 leftB = lim.leftBottomCorner();
        Vector2 leftT = lim.leftTopCorner();
        Vector2 rightB = lim.rightBottomCorner();
        Vector2 rightT = lim.rightTopCorner();
       
        shaper.begin(ShapeType.Line);
        Gdx.gl20.glLineWidth((float) (2 * lim.getyUnit()/ camera.zoom));
        
        Vector2  src=  bottomGoal.getSrc(true);
        Vector2  dst = bottomGoal.getDst(true);
        
        shaper.line(src.x , src.y , leftB.x , leftB.y , 
        		Color.BLUE , Color.BLUE);
        shaper.line(dst.x , dst.y , rightB.x , rightB.y , 
        		Color.BLUE , Color.BLUE);
        src=  topGoal.getSrc(true);
        dst = topGoal.getDst(true);
        shaper.line(src.x , src.y , leftT.x , leftT.y , 
        		Color.BLUE , Color.BLUE);
        shaper.line(dst.x , dst.y , rightT.x , rightT.y , 
        		Color.BLUE , Color.BLUE);
        shaper.line(rightB.x , rightB.y , rightT.x , rightT.y , 
        		Color.BLUE , Color.BLUE);
        shaper.line(leftB.x ,leftB.y , leftT.x , leftT.y , 
        		Color.BLUE , Color.BLUE);
        shaper.line(height * lim.getMid(), lim.getLeft(), height * lim.getMid(),
        		lim.getRight(),Color.BLACK,Color.BLACK);  
        shaper.end();
        
        
        
        
        
        
		batch.begin();
		stage.draw();
		batch.end();

		if(Gdx.input.isTouched()) {
			camera.unproject(tempTouch.set(Gdx.input.getX(), Gdx.input.getY(),0));
			tempTouch = UnitConvertor.toNormal(tempTouch);
            t1.move(tempTouch.x, tempTouch.y);
            bot.move(t1.getX(), height - t1.getY());
	    }
		
		if (Gdx.input.isKeyPressed(Keys.BACK)){
			music.stop();
			super.dispose();
			game.setScreen(new Menu(game));
			
		}
		stage.act(delta);
		
		
	}
	
	
}
