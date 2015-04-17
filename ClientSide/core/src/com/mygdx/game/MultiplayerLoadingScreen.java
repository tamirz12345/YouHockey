package com.mygdx.game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.ClosedByInterruptException;

import GameObjects.Limits;
import Network.Message;
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
        	  String ipS = "192.168.1.107";
        	  int portS = 3000;
        	  InetSocketAddress serverAddress;
        	  String sentence;
        	  String recivedString;
        	  Message m ;
        	  BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
        	  Socket clientSocket = null;
			try {
				  clientSocket = new Socket();
				  serverAddress = new InetSocketAddress(ipS , portS);
				  clientSocket.connect(serverAddress , 5000);
				  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
				  BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				  sentence = "660-";
				  outToServer.writeBytes(sentence );

				  recivedString = inFromServer.readLine();
				  m = new Message(recivedString);
				   
				  System.out.println("FROM SERVER: " + m.getType());
				  
				  
				  
				  
				  
				  clientSocket.close();
				  
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				returnToMenu();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				returnToMenu();
			}
        	  
	      
	     
          return "Executed";
        }

        protected void onPostExecute(String result) {
            
            
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
        
        
        
        @Override
		protected void onCancelled() {
		
			super.onCancelled();
		}

		public void returnToMenu()
        {
        	Gdx.app.postRunnable(new Runnable() {
				
				@Override
				public void run() {
					game.setScreen(new Menu(game));
					
				}
			});
        	
        }
    }
	
	
	
}
