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

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MultiplayerLoadingScreen   extends ApplicationAdapter implements InputProcessor , Screen{
	YouHockey game;
	
	Limits lim;
	public String status ="Connecting to server";
	public static Texture backgroundTexture;
    public static Sprite backgroundSprite;
    private SpriteBatch spriteBatch;
	boolean started = false;
	Camera camera;
	StretchViewport viewport;
	
	BitmapFont font ;
	Matrix4 mx4Font;
	public MultiplayerLoadingScreen(YouHockey youHockey) {
    	this.game = youHockey;
		this.create();
		
	}

	public void create () {
		backgroundTexture = new Texture(Gdx.files.internal("loading.png"));
        lim = new Limits(null , false);
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
	    viewport = new StretchViewport(100,100,camera); //stretch screen to [0,100]x[0,100] grid
	    viewport.apply();
	    camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0); //set camera to look at center of viewport
	    
	    
        
        font =new  BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"),false);
        
        ServerChat Schat = new ServerChat();
        Schat.execute();
	}

	
	 @Override
	 public void resize(int width, int height){
	    viewport.update(width, height);
	    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
	 }
	
	
	@SuppressWarnings("deprecation")
	public void render (float delta) {
		camera.update();
		Gdx.gl.glClearColor((float)0.5,0.8f, (float)0.7, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.setProjectionMatrix(camera.combined);
		
	
		spriteBatch.begin();
		//spriteBatch.draw(backgroundTexture, 0 , 0  , lim.getGameHeight(),lim.getGameWidth());
		font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		font.setScale(0.2f);
		
		font.draw(spriteBatch, status ,20 , 20 );
        spriteBatch.end();
        
        
        	
		
	}
	
	public class ServerChat extends AsyncTask<String, Void, String> {
	  	public int portS = 3000;
	  	public String ipS =  "192.168.1.107"; 
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
				  status = "Connected to server ";
				  outToServer = new DataOutputStream(clientSocket.getOutputStream());
				  inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				  if (gameRequest())
				  {
					  status =  "Waiting To rival ";
				  }
				  rival = pairUp(freePort);
				  status =  "rival found . starting game";
				  
				  
				  
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				returnToMenu();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				game.setScreen(new Menu(game));
			}
        	catch (RuntimeException e)  
			{
        		e.printStackTrace();
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
				returnToMenu();
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
                    temp.close();
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
				returnToMenu();
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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
