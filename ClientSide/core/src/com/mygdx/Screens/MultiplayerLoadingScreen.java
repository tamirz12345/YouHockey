package com.mygdx.Screens;

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
	
	public class ServerChat extends AsyncTask<String, Void, String> {
		String ipS = "192.168.1.106";
	  	int portS = 3000;
	  	InetSocketAddress serverAddress;
	  	String sentence;
	  	String recivedString;
	  	Message m ;
	  	DataOutputStream outToServer ;
		BufferedReader inFromServer ;
	  	Socket clientSocket = null;
		
		
		
		
        protected String doInBackground(String... params) {
        	  
			try {
				  clientSocket = new Socket();
				  serverAddress = new InetSocketAddress(ipS , portS);
				  clientSocket.connect(serverAddress , 5000);
				  outToServer = new DataOutputStream(clientSocket.getOutputStream());
				  inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				  if (gameRequest())
				  {
					  System.out.println("Waiting To rival \n");
				  }
				   
				  
				  
				  
				  
				  
				  
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
			
			if (clientSocket.isConnected())
			{
				sentence = "405-";
				try {
					
					outToServer.writeBytes(sentence );
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
			}
        	Gdx.app.postRunnable(new Runnable() {
				
				@Override
				public void run() {
					game.setScreen(new Menu(game));
					
				}
			});
        	
        }
		
		
		
		
		public boolean gameRequest()
		{
			
			sentence = "660-";
			try {
				
				outToServer.writeBytes(sentence );
				recivedString = inFromServer.readLine();
				m = new Message(recivedString);
				if (m.getType().compareTo("300") == 0 )
				{
					return true;
				}
				if (m.getType().compareTo("403") == 0 )
				{
					// To be updated for now it returns to menu
					
					return false;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}

			
			
			return false;
		}
    }
	
	
	
}
