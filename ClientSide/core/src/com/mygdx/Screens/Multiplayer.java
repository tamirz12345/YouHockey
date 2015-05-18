package com.mygdx.Screens;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import GameObjects.Disk;
import GameObjects.Goal;
import GameObjects.Limits;
import GameObjects.Tool;
import Network.Message;
import android.os.AsyncTask;
import android.util.Log;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class Multiplayer  extends ApplicationAdapter implements InputProcessor ,  Screen  {
	private static final Integer SCORE_TO_WIN = 2;
	YouHockey game;
	SpriteBatch batch;
	OrthographicCamera camera;
	Music music;
	Tool t1;
    Tool bot;
    //resetButton resetB;
	Vector3 tempTouch;
	float height,width;
	ShapeRenderer  shaper;
    final int TOOL_R = 5;
    
    
    
    BlockingQueue<String> toSend;
	BlockingQueue<Message> toHandel;
	Socket rival ;
	ServerSocket ActiveRival;
	InetSocketAddress rivalAddress;
	
	Viewport viewport;
	int downSpawn;
    Disk disk;
    Texture diskT ;
    Limits lim;
    Stage stage;
    float delta;
    Vector2 textBoxPos ;
    private String ScoreString;
    BitmapFont yourBitmapFontName;
    
    boolean inisiator , rivalReady = false , playing = false;
    
    DataOutputStream outToRival ;
	BufferedReader inFromRival ;
    
	Thread reciver , handler , sender ;
	
	Message firstM ;
	String firstS= "900-";
    public Multiplayer(YouHockey youHockey ,String serverAddr ,String rivalAddr , int port) {
    	this.game = youHockey;
    	toSend = new LinkedBlockingDeque<String>();
    	toHandel = new LinkedBlockingDeque<Message>();
    	
    	String[] temp = rivalAddr.split(":");
    	inisiator = temp.length == 2 ; 
    	lim = new Limits(toSend);
    	
    	
    	try {
    		if (inisiator)
    		{
    			
    			Log.d("starterTamir", "Inisiating to : "+ rivalAddr);
    			rivalAddress = new InetSocketAddress(temp[0], Integer.parseInt(temp[1]));
     			rival = new Socket();
    			rival.connect(rivalAddress, 150000);
    			Log.d("starterTamir", "connected");
    			outToRival = new DataOutputStream(rival.getOutputStream());
    			inFromRival = new BufferedReader(new InputStreamReader(rival.getInputStream()));
    			Log.d("starterTamir", "streams constructed");
    			Random r = new Random();
				downSpawn = r.nextInt(2) % 2;
				
				
				firstS += Integer.toString(downSpawn)+"-\n";
				Log.d("starterTamir", "sanding " + firstS);
				outToRival.writeBytes(firstS);
				Log.d("starterTamir", "sent, now waiting");
				String recived= inFromRival.readLine();
				Log.d("starterTamir", "recived : "+ recived);
				firstM= new Message(recived);
				Log.d("starterTamir", "compare to 900 : ");
				if (firstM != null && firstM.getType().compareTo("900") == 0 )
				{
					int otherSide=Integer.parseInt(firstM.getParameters()[0]);
					if ((otherSide == 1 && downSpawn == 0 ) || (otherSide == 0 && downSpawn == 1 ))
						playing= true;
				}
				else
				{
					Log.d("starterTamir", "if not enterd massage was "+firstM.getType());
				}
    		}
    		else
    		{
    			Log.d("starterTamir", "Waiting  to : "+ rivalAddr);
    			
    			ActiveRival = new ServerSocket(port);
				rival = ActiveRival.accept();
				Log.d("starterTamir", "accepted : " + rival.getInetAddress().getHostAddress());
				outToRival = new DataOutputStream(rival.getOutputStream());
				inFromRival = new BufferedReader(new InputStreamReader(rival.getInputStream()));
				Log.d("starterTamir", "now waiting to recive");
				String recived = inFromRival.readLine();
				Log.d("starterTamir", "recived " + recived);
				firstM= new Message(recived);
				Log.d("starterTamir", "compare To if.. msg type = " + firstM.getType());
				if (firstM != null && firstM.getType().compareTo("900") == 0 )
				{
				
					Log.d("starterTamir", "parcing ...");
					downSpawn =Integer.parseInt(firstM.getParameters()[0]);
					Log.d("starterTamir", "parameter" + Integer.toString(downSpawn));
					if (downSpawn==  1)
						downSpawn = 0 ;
					else
						downSpawn = 1 ;
					
					firstS= "900-"+Integer.toString(downSpawn)+"-\n";
					Log.d("starterTamir", "sending " +firstS);
					outToRival.writeBytes(firstS);
					Log.d("starterTamir", "sent");
					playing= true;
				}
				else
				{
					Log.d("myDebug", "if not enterd massage was "+firstM.getType());
				}
    		}
    		
    		
			
			
		
			if (!playing)
				throw new Exception("Game Start Error");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			Log.d("myExeption", e.toString());
			game.setScreen(new Menu(youHockey));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			Log.d("myExeption", e.toString());
			game.setScreen(new Menu(youHockey));
		}
    	
    	
		
		
		
    	
		
		if (playing)
			this.create();
		else
			game.setScreen(new Menu(youHockey));
	}

	public void create () {
    	
		lim = new Limits(toSend);
		batch = new SpriteBatch();
		
		camera = new OrthographicCamera();
	    viewport = new StretchViewport(100,100,camera); //stretch screen to [0,100]x[0,100] grid
	    viewport.apply();
	    
	    camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0); //set camera to look at center of viewport
	    Gdx.input.setInputProcessor(this);

        t1 = new Tool("player2.png", true,  lim);
        bot = new Tool("player2.png", false , lim);
        
        tempTouch = new Vector3();
        
        disk = new Disk(lim , downSpawn);
        disk.spawn();
        stage = new Stage();
        stage.addActor(bot);
        stage.addActor(t1);
        stage.addActor(disk);
        
        Gdx.input.setCatchBackKey(true);
	    music = Gdx.audio.newMusic(Gdx.files.internal("backroundMusic.mp3"));
	    music.setLooping(true);
	    music.play();
	    shaper = new ShapeRenderer();
	    
	    
	    ScoreString = "0 : 0";
	    yourBitmapFontName =new  BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"), false);
	    
	    
	    
	    sender = new Thread(new Sender());
		handler = new Thread(new Handler());
		reciver = new Thread(new Reciver());
		
		sender.start();
		handler.start();
		reciver.start();
	    
	}
	
	public void CloseStuff()
	{
		Log.d("CloseTamir", "CloseStuff() Activated");
		try {
			playing = false;
			Log.d("CloseTamir", "interupting threads");
			reciver.interrupt();
			sender.interrupt();
			handler.interrupt();
			Log.d("CloseTamir", "Joining finished");
			rival.close();
			Log.d("CloseTamir", "Socket Closed");
			if (!inisiator)
			{
				ActiveRival.close();
				Log.d("CloseTamir", "ServerSocketClosed");
			}
				
			music.stop();
			super.dispose();
			Log.d("CloseTamir", "Disposed()");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public void render (float delta) {
		camera.update();
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
        Goal bottomGoal = new Goal(true , lim );
        Goal topGoal = new Goal(false , lim );
        
        disk.update(t1,null);
        Vector2 leftB = lim.leftBottomCorner();
        Vector2 leftT = lim.leftTopCorner();
        Vector2 rightB = lim.rightBottomCorner();
        Vector2 rightT = lim.rightTopCorner();
        shaper.setProjectionMatrix(camera.combined);
        shaper.begin(ShapeType.Line);
        Gdx.gl20.glLineWidth(20);
        
        Vector2  src=  bottomGoal.getSrc(true);
        Vector2  dst = bottomGoal.getDst(true);
        
        shaper.line(src.x , src.y , leftB.x , leftB.y , 
        		Color.YELLOW , Color.YELLOW);
        shaper.line(dst.x , dst.y , rightB.x , rightB.y , 
        		Color.YELLOW , Color.YELLOW);
        src=  topGoal.getSrc(true);
        dst = topGoal.getDst(true);
        shaper.line(src.x , src.y , leftT.x , leftT.y , 
        		Color.YELLOW , Color.YELLOW);
        shaper.line(dst.x , dst.y , rightT.x , rightT.y , 
        		Color.YELLOW, Color.YELLOW);
        shaper.line(rightB.x , rightB.y , rightT.x , rightT.y , 
        		Color.GREEN , Color.GREEN);
        shaper.line(leftB.x ,leftB.y , leftT.x , leftT.y , 
        		Color.GREEN , Color.GREEN);
        
        
		
        shaper.line(lim.calcMid(), lim.getLeft(), lim.calcMid(),
        		lim.getRight(),Color.BLACK,Color.BLACK);  
        shaper.end();
        
        
        
        
        
        batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		yourBitmapFontName.setColor(Color.BLACK);
		yourBitmapFontName.setScale(0.75f);
		textBoxPos = bottomGoal.getSrc(true);
		textBoxPos=  UnitConvertor.toGame(textBoxPos.x , textBoxPos.y);
		
		ScoreString = Integer.toString(lim.getScoreTop()) + "  " + 
				Integer.toString(lim.getScoreBottom());
		yourBitmapFontName.draw(batch, ScoreString,25,100);
		t1.draw(batch);
		bot.draw(batch);
		disk.draw(batch);
		batch.end();

		
		
		if (Gdx.input.isKeyPressed(Keys.BACK)){
			music.stop();
			super.dispose();
			CloseStuff();
			game.setScreen(new Menu(game));
			
		}
		stage.act(delta);
		
		if (lim.getScoreBottom() == SCORE_TO_WIN)
		{
			music.stop();
			super.dispose();
			CloseStuff();
			game.setScreen(new EndGame(game, true));
		}
			
		if (lim.getScoreTop() == SCORE_TO_WIN)
		{
			music.stop();
			super.dispose();
			CloseStuff();
			game.setScreen(new EndGame(game, false));
		}
		
	}
	
	
	public class Reciver implements Runnable
	{
		
		

		

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.d("threadTamir", "reciver thread started");
			while (playing)
			{
				try {
					String tmp = inFromRival.readLine();
					if (tmp == null)
					{
						if (rival.isConnected())
						{
							Log.d("myExeption" , "recived null but connected");
							continue;
						}
					
							
						else
							throw new Exception("Rival disconected ");
					}
					Log.d("reciverT", "recived : " + tmp);
					Message m = new Message(tmp);
					
					toHandel.add(m);
					Log.d("reciverT",tmp + " added succesfuly to Queue");
				}
				catch(SocketException e)
				{
					Log.d("myExeption",e.toString());
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					Log.d("myExeption",e.toString());
					game.setScreen(new Menu(game));
					
				} 
				
				catch (InterruptedException e)
				{
					Log.d("myExeption",e.toString());
					
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					Log.d("myExeption",e.toString());
					game.setScreen(new Menu(game));
				}
			}
			Log.d("loop", "Reciver loop finished");
		}
		
	}
	
	public class Sender implements Runnable
	{

		

		

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.d("threadTamir", "sender thread started");
			while (playing)
			{
				try {
					String s  = toSend.take();
					Log.d("sender" , "Sending : " + s);
					outToRival.writeBytes(s+"\n");
					Log.d("sender" , "Sent ");
				}
				catch (SocketException e)
				{
					Log.d("myExeption",e.toString());
				}
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Log.d("myExeption",e.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.d("myExeption",e.toString());
				}
				
				
			}
			
			Log.d("loop", "sender loop finished");
		}
		
	}
	
	public class Handler implements Runnable 
	{


		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.d("threadTamir", "handler thread started");
			while (playing)
			{
				try {
					Message m = toHandel.take();
					if (m == null)
						throw new Exception("null msg in the queue wtf?");
					Log.d("handler" , "Handling : " + m.toString());
					handel(m);
					Log.d("handler" , "Sent ");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Log.d("myExeption",e.toString());
					
				}
				catch (SocketException e)
				{
					Log.d("myExeption",e.toString());
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					Log.d("myExeption",e.toString());
					e.printStackTrace();
				}
				
				
			}
			Log.d("loop", "Handler loop finished");
		}
		
		private void handel(Message m) {
			// TODO Auto-generated method stub
			String opCode = m.getType();
			String[] params = m.getParameters();
			float x = 0,y=0,time ; 
			boolean flag1,flag2,flag3,flag4 = false,flag5 , flag6;
			switch (opCode) {
			case "901":
				Log.d("handler" , "case 901");
				x = Float.parseFloat(params[0]);
				y = Float.parseFloat(params[1]);
				time =  Float.parseFloat(params[2]);
				x = lim.getGameWidth()   - x ;
				y = lim.getGameHeight()  - y ; 
				Log.d("handler","Move tool to  x=  "+ x +" y= "+ y
						+" time = "+ time);
				flag1 = x >= lim.getLeft() && x <= lim.getRight();
				
				flag2 = y >= lim.getBottom() &&y <= lim.getTop();
				flag3 = time > 0 ; 
				if (flag1 && flag2 && flag3)
				{
					Log.d("handler" , "info okay");
					bot.clearActions();
					bot.move(x, y, time);
				}
				else
				{
					//Send error Massage
					Log.d("handler" , "info not  okay");
				}
				break;
			case "906":
				Log.d("diskTamir","handling : " + m.toString());
				x = Float.parseFloat(params[0]);
				y = Float.parseFloat(params[1]);
				time =  Float.parseFloat(params[2]);
				x = lim.getGameWidth()   - x ;
				y = lim.getGameHeight()  - y ; 
				String xDir , yDir , dir ;
				flag1 = x >= lim.getLeft()   && x <= lim.getRight();
				flag2 = y >= lim.getBottom() && y <= lim.getTop();
				flag3 = time > 0 ; 
				xDir = params[3];
				yDir = params[4];
				flag4 = params[3].compareTo("left") == 0 ||
						params[3].compareTo("right") == 0; 
				flag5= params[4].compareTo("top") == 0 ||
						params[4].compareTo("bottom") == 0; 
				dir  = params[5];
				flag6 = dir.compareTo("left") == 0 ||
						dir.compareTo("right") == 0||
						dir.compareTo("top") == 0 ||
						dir.compareTo("bottom") == 0;
				if (flag1 && flag2 && flag3 && flag4 && flag5 && flag6)
				{
					Log.d("diskTamir" , "info   okay ");
					disk.clearActions();
					disk.setXdir(xDir);
					disk.setXdir(yDir);
					disk.setDir(dir);
					
					lim.addMoveToAction(disk, x, y, time);
					Log.d("diskTamir" , "disk moving to x: "+x+" y: " +y 
							+" time " + time + "xDir = " + xDir + "yDir =" + yDir +"dir : " + dir);
				}
				else
				{
					//Send error Massage
					Log.d("diskTamir" , "info not  okay flags :" + flag1+","+flag2+","+flag3+","+flag4+
							","+flag5);
				}
				
				
				break;
			case "905":
				if (params[0].compareTo("1")==0)
				{
					
					Log.d("goalTamir" , "i scored");
					lim.incBottom();
					disk.downSpawn = false;
					disk.spawn();
					String response = "905-0-";
					toSend.add(response);
					Log.d("goalTamir" , response + " added to toSend");
					Log.d("goalTamir" , "Score me : " + lim.getScoreBottom()
							+"Score rival : " + lim.getScoreTop());
				}
				else if (params[0].compareTo("0")==0 && disk.isWaiting())
				{
					Log.d("goalTamir" , "rival  scored and accepted it");
					Log.d("goalTamir" , "Score me : " + lim.getScoreBottom()
							+"Score rival : " + lim.getScoreTop());
					disk.wakeUp();
					disk.downSpawn = true;
					disk.spawn();
				
				}
				else
				{
					Log.d("goalTamir" , "something is wrong");
				}
				break;
			case "907":
				Log.d("toolTamir","handling : " + m.toString());
				x = lim.getGameWidth() -   Float.parseFloat(params[0]);
				y = lim.getGameHeight() -Float.parseFloat(params[1]);
				flag1 = x >= lim.getLeft()   && x <= lim.getRight();
				flag2 = y >= lim.getBottom() && y <= lim.getTop();
				if (flag1 && flag2)
				{
					Log.d("toolTamir" , "info   okay ");
					bot.setPosition(x, y);
					
					
					Log.d("toolTamir" , "tool move to x: "+x+" y: ");
				}
				else
				{
					//Send error Massage
					Log.d("toolTamir" , "info not  okay flags :" + flag1+","+flag2);
				}
				
				break;
			default:
				break;
			}
		}
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		CloseStuff();
		playing = false; 
		super.dispose();
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
		Vector3 worldCoordinates = camera.unproject(new Vector3(screenX,screenY,0));
		Gdx.app.log("Mouse Event","Click at " + worldCoordinates.x + "," + worldCoordinates.y);
	    Vector2 pos = UnitConvertor.toNormal(worldCoordinates.x, worldCoordinates.y);
	   
	    Gdx.app.log("Mouse Event","Projected at " + pos.x + "," + pos.y);
	    if (pos.y > lim.calcMid() - t1.getHeight())
	    {
	    	pos.y = lim.calcMid() - t1.getHeight();
	    }
	    t1.setPosition(pos.x, pos.y);
	    
	    
		
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
