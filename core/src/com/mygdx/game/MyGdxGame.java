package com.mygdx.game;

import GameObjects.Tool;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.sun.prism.GraphicsPipeline.ShaderType;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	private OrthographicCamera camera;
	private Music music;
	Tool t1;
    Tool bot;
	Vector3 tempTouch;
	float height,width;
	ShapeRenderer shaper;
    final int TOOL_WIDTH = 64;
    final int TOOL_HEIGHT = 64;

    @Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		height = Gdx.graphics.getWidth();
		width = Gdx.graphics.getHeight();
		camera.setToOrtho(false, height, width);

        t1 = new Tool("player1.png", true, TOOL_HEIGHT, TOOL_WIDTH);
        bot = new Tool("player1.png", false, TOOL_HEIGHT, TOOL_WIDTH);
        tempTouch = new Vector3();

	    music = Gdx.audio.newMusic(Gdx.files.internal("backroundMusic.mp3"));
	    music.setLooping(true);
	    music.play();
	    shaper = new ShapeRenderer();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 10, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		t1.update();

		shaper.begin(ShapeType.Line);
		shaper.line(height/2, 0, height/2, width,Color.BLACK,Color.BLACK);
		shaper.end();
		
		batch.begin();
		batch.draw(t1.img, t1.rec.x, t1.rec.y);
		batch.end();

		if(Gdx.input.isTouched()) {
			camera.unproject(tempTouch.set(Gdx.input.getX(), Gdx.input.getY(),0));
            t1.move(tempTouch.x, tempTouch.y);
	    }
	}
}
