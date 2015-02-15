package com.mygdx.game;

import GameObjects.Disk;
import GameObjects.Limits;
import GameObjects.Tool;
import GameObjects.resetButton;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera camera;
	Music music;
	Tool t1;
    Tool bot;
    //resetButton resetB;
	Vector3 tempTouch;
	float height,width;
	ShapeRenderer shaper;
    final int TOOL_WIDTH = 64;
    final int TOOL_HEIGHT = 64;
    Disk disk;
    Texture diskT ;
    Limits lim;
    Stage stage;
    float delta;
    @Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		height = Gdx.graphics.getWidth();
		width = Gdx.graphics.getHeight();
		camera.setToOrtho(false, height, width);
        t1 = new Tool("player2.png", true, TOOL_HEIGHT, TOOL_WIDTH);
        bot = new Tool("player2.png", false, TOOL_HEIGHT, TOOL_WIDTH);
        
        tempTouch = new Vector3();
        lim = new Limits();
        disk = new Disk(lim);
        disk.setPosition((float) (height * 0.7 - disk.getHeight()/2), width/2 - disk.getWidth()/2);
        stage = new Stage();
        stage.addActor(bot);
        stage.addActor(t1);
        stage.addActor(disk);
        Gdx.input.setInputProcessor(stage);
	    music = Gdx.audio.newMusic(Gdx.files.internal("backroundMusic.mp3"));
	    music.setLooping(true);
	    music.play();
	    shaper = new ShapeRenderer();
	    
	        

			
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 10, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		delta = Gdx.graphics.getDeltaTime();
		
		disk.update(t1 , bot );
		shaper.begin(ShapeType.Line);
		shaper.line(height * lim.getMid(), 0, height * lim.getMid(), width,Color.BLACK,Color.BLACK);
		shaper.end();
		
		batch.begin();
		
		
		stage.draw();
		batch.end();
		
		
		if(Gdx.input.isTouched()) {
			camera.unproject(tempTouch.set(Gdx.input.getX(), Gdx.input.getY(),0));
            t1.move(tempTouch.x, tempTouch.y);
            bot.move(height-t1.getX(), t1.getY());
            
            
            
	    }
		stage.act(delta);
	}
	
	public void resetGame()
	{
		disk.setPosition((float) (height * 0.7 - disk.getHeight()/2), width/2 - disk.getWidth()/2);
		
		t1 = new Tool("player2.png", true, TOOL_HEIGHT, TOOL_WIDTH);
		
		bot = new Tool("player2.png", false, TOOL_HEIGHT, TOOL_WIDTH);
		
	}
}
