package com.mygdx.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import GameObjects.Limits;
import Network.ServerChat;
import android.os.AsyncTask;

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
	
	int countR = 0 ;
	int rindurs = 50;
	
	public MultiplayerLoadingScreen(YouHockey youHockey) {
    	this.game = youHockey;
		this.create();
		
	}

	public void create () {
		backgroundTexture = new Texture(Gdx.files.internal("loading.png"));
        lim = new Limits();
        spriteBatch = new SpriteBatch();
        
        ServerChat Schat = new ServerChat();
        Schat.execute();
        
       
	}

	@SuppressWarnings("deprecation")
	public void render (float delta) {
		Gdx.gl.glClearColor(1, 10, 1, 1);
		spriteBatch.begin();
		spriteBatch.draw(backgroundTexture, 0 , 0  , lim.getGameHeight(),lim.getGameWidth());
        spriteBatch.end();
        
        
        	
		
}
	
	private class ServerChat extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
    		  BufferedReader inFromUser =
    	         new BufferedReader(new InputStreamReader(System.in));
    	      DatagramSocket clientSocket;
    	      InetAddress IPAddress ;
    	      byte[] sendData = new byte[1024];
    	      byte[] receiveData = new byte[1024];
    	      screenSwitch sSwitch = new screenSwitch();
			try {
				clientSocket = new DatagramSocket();
				IPAddress = InetAddress.getByName("192.168.223.1");
				String sentence = "660";
			    sendData = sentence.getBytes();
			    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
			    		IPAddress,3000);
			    clientSocket.send(sendPacket);
			    DatagramPacket receivePacket = new DatagramPacket(receiveData, 
			    		receiveData.length);
			    clientSocket.receive(receivePacket);
			    String modifiedSentence = new String(receivePacket.getData());
			    System.out.println("FROM SERVER:" + modifiedSentence);
			    clientSocket.close();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Gdx.app.postRunnable(sSwitch);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Gdx.app.postRunnable(sSwitch);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Gdx.app.postRunnable(sSwitch);
			}
    	      
	      
	     
          return "Executed";
        }

        protected void onPostExecute(String result) {
            
            
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
	
	
	private class screenSwitch implements Runnable
	{

		@Override
		public void run() {
			game.setScreen(new Menu(game));
			
		}
		
	}
}
