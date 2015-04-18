package com.mygdx.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class YouHockey extends Game {
	
	@Override
	public void create() {
		
		setScreen(new Menu(this));
		
	}

}
