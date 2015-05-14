package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


//Class that converts cordinates of game to cordinates of math
public class UnitConvertor {
	static float height = Gdx.graphics.getWidth();
	public static Vector2 toNormal(float x , float y)
	{
		Vector2 v = new Vector2();
		v.x = y; 
		v.y = height - x;
		
		return v;
	}
	
	public static Vector3 toNormal(Vector3 param)
	{
		Vector2 temp =   toNormal(param.x, param.y);
		return new Vector3(temp.x,temp.y,0);
	}
	public static void draw(SpriteBatch batch ,Texture t,float x , float y , float w , float h )
	{
		Vector2 pos = UnitConvertor.toGame(x, y + h);
		batch.draw(t ,pos.x,  pos.y , w , h);
	}
	public static Vector2 toGame(float x , float y)
	{
		Vector2 v = new Vector2();
		v.y = x; 
		v.x = height - y;
		
		return v;
	}
	
	
	public static Vector3 toGame(Vector3 param)
	{
		Vector2 temp =   toGame(param.x, param.y);
		return new Vector3(temp.x,temp.y,0);
	}
}
