package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.scenario.Settings;

public class YouHockey extends Game {
	public SpriteBatch batcher;
	@Override
	public void create() {
		batcher = new SpriteBatch();
		setScreen(new MainGame(this));
		
	}

}
