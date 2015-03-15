package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.scenario.Settings;

public class YouHockey extends Game {
	
	@Override
	public void create() {
		
		setScreen(new Menu(this));
		
	}

}
