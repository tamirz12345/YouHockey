package com.mygdx.game;

import GameObjects.Limits;
import Network.ServerChat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MultiplayerLoadingScreen  extends ScreenAdapter{
	YouHockey game;
	
	Limits lim;
	public static Texture backgroundTexture;
    public static Sprite backgroundSprite;
    private SpriteBatch spriteBatch;
	boolean started = false;
	Thread serverCon;
	int countR = 0 ;
	int rindurs = 50;
	ServerChat sc ;
	public MultiplayerLoadingScreen(YouHockey youHockey) {
    	this.game = youHockey;
		this.create();
		
	}

	public void create () {
		backgroundTexture = new Texture(Gdx.files.internal("loading.png"));
        lim = new Limits();
        spriteBatch = new SpriteBatch();
        sc = new ServerChat();
        serverCon = new Thread(sc);
        serverCon.start();
       
	}

	@SuppressWarnings("deprecation")
	public void render (float delta) {
		Gdx.gl.glClearColor(1, 10, 1, 1);
		spriteBatch.begin();
		spriteBatch.draw(backgroundTexture, 0 , 0  , lim.getGameHeight(),lim.getGameWidth());
        spriteBatch.end();
        
        if (countR++==rindurs)
        {
        	sc.stop();
        	serverCon.interrupt();
        	game.setScreen(new MainGame(game));
        	
        }
        	
		
}
}
