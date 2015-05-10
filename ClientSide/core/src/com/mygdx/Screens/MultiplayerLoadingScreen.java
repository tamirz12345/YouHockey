package com.mygdx.Screens;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MultiplayerLoadingScreen  extends ScreenAdapter{
	YouHockey game;
	public String status ="Connecting to server";
	Limits lim;
	public static Texture backgroundTexture;
    public static Sprite backgroundSprite;
    private SpriteBatch spriteBatch;
	boolean started = false;
	
	int countR = 0 ;
	int rindurs = 50;
	BitmapFont font ;
	public MultiplayerLoadingScreen(YouHockey youHockey) {
    	this.game = youHockey;
		this.create();
		
	}

	public void create () {
		backgroundTexture = new Texture(Gdx.files.internal("loading.png"));
        lim = new Limits(null);
        spriteBatch = new SpriteBatch();
        
        ServerChat Schat = new ServerChat();
        Schat.execute();
        font = new BitmapFont(Gdx.files.internal("data/calibri.fnt"), false);
        


	}

	@SuppressWarnings("deprecation")
	public void render (float delta) {
		Gdx.gl.glClearColor((float)0.5,0.8f, (float)0.7, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		//spriteBatch.draw(backgroundTexture, 0 , 0  , lim.getGameHeight(),lim.getGameWidth());
		font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		font.setScale(0.2f);
		font.draw(spriteBatch, status, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        spriteBatch.end();
        
        
        	
		
}
	
	public class ServerChat extends AsyncTask<String, Void, String> {
		public String ipS = "192.168.1.106";
	  	public int portS = 3000;
	  	
	  	InetSocketAddress serverAddress;
	  	String sentence;
	  	String recivedString;
	  	Message m ;
	  	DataOutputStream outToServer ;
		BufferedReader inFromServer ;
	  	Socket clientSocket = null;
	  	String rival;
	  	int freePort;
		
		
        protected String doInBackground(String... params) {
        	  
			try {
				  freePort = findPort(1025, 10000);
				  clientSocket = new Socket();
				  serverAddress = new InetSocketAddress(ipS , portS);
				  clientSocket.connect(serverAddress , 5000);
				  outToServer = new DataOutputStream(clientSocket.getOutputStream());
				  inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				  if (gameRequest())
				  {
					  System.out.println("Waiting To rival \n");
				  }
				  rival = pairUp(freePort);
				  
				  
				  
				  
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				returnToMenu();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				returnToMenu();
			}
        	  
	      
	     
          return rival;
        }

        protected void onPostExecute(String result) {
            final String res = result;
            System.out.println(res);
            try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            Gdx.app.postRunnable(new Runnable() {
				
				@Override
				public void run() {
					game.setScreen(new Multiplayer(game,
							ipS+":" + Integer.toString(portS), res,freePort));
					
				}
			});
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
        
        
        
        
        public int findPort(int start , int end) throws IOException
        {
        	ServerSocket s = new ServerSocket(0);
        	
        	for (int i = start ; i < end ; i ++ ) {
                try {
                	
                    ServerSocket temp = new ServerSocket(i);
                    return i;
                } catch (IOException ex) {
                    continue; // try next port
                }
            }

            // if the program gets here, no port in the range was found
            throw new IOException("no free port found");
        }
        
        
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
		
		
		
		public String pairUp(int fPort)
		{
			String ret = null;
			try {
				
				
				recivedString = inFromServer.readLine();
				m = new Message(recivedString);
				
				if (m.getType().compareTo("301") == 0 )
				{
					String[] params= m.getParameters();
					ret= params[0] + ":" + params[1];
					return ret;
					
					
				}
				
				else if  (m.getType().compareTo("303") == 0)
				{
					sentence = Integer.toString(fPort);
					sentence = "661-"+sentence +"-";
					outToServer.writeBytes(sentence );
					
					recivedString = inFromServer.readLine();
					m = new Message(recivedString);
					
					if (m.getType().compareTo("301") == 0 )
					{
						String[] params= m.getParameters();
						ret = params[0] ;
						return ret;
						
						
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			catch ( Exception e) {
				// TODO: handle exception\
				e.printStackTrace();
			}
			return ret;
		}
    }
	
	
	
}
