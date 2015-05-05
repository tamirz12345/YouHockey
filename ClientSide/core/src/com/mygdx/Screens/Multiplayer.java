package com.mygdx.Screens;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
import com.badlogic.gdx.Input.Keys;
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


public class Multiplayer extends ScreenAdapter {
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
    	
    	
    	
    	try {
    		if (inisiator)
    		{
    			Log.d("myDebug", "Inisiating to : "+ rivalAddr);
    			rivalAddress = new InetSocketAddress(temp[0], Integer.parseInt(temp[1]));
     			rival = new Socket();
    			rival.connect(rivalAddress, 150000);
    			Log.d("myDebug", "connected");
    			outToRival = new DataOutputStream(rival.getOutputStream());
    			inFromRival = new BufferedReader(new InputStreamReader(rival.getInputStream()));
    			Log.d("myDebug", "streams constructed");
    			Random r = new Random();
				downSpawn = r.nextInt(2) % 2;
				firstS += Integer.toString(downSpawn)+"-\n";
				Log.d("myDebug", "sanding " + firstS);
				outToRival.writeBytes(firstS);
				Log.d("myDebug", "sent, now waiting");
				String recived= inFromRival.readLine();
				Log.d("myDebug", "recived : "+ recived);
				firstM= new Message(recived);
				Log.d("myDebug", "compare to 900 : ");
				if (firstM != null && firstM.getType().compareTo("900") == 0 )
				{
					int otherSide=Integer.parseInt(firstM.getParameters()[0]);
					if ((otherSide == 1 && downSpawn == 0 ) || (otherSide == 0 && downSpawn == 1 ))
						playing= true;
				}
				else
				{
					Log.d("myDebug", "if not enterd massage was "+firstM.getType());
				}
    		}
    		else
    		{
    			Log.d("myDebug", "Waiting  to : "+ rivalAddr);
    			
    			ActiveRival = new ServerSocket(port);
				rival = ActiveRival.accept();
				Log.d("YOUHOCKEY", "accepted : " + rival.getInetAddress().getHostAddress());
				outToRival = new DataOutputStream(rival.getOutputStream());
				inFromRival = new BufferedReader(new InputStreamReader(rival.getInputStream()));
				Log.d("myDebug", "now waiting to recive");
				String recived = inFromRival.readLine();
				Log.d("myDebug", "recived " + recived);
				firstM= new Message(recived);
				Log.d("myDebug", "compare To if.. msg type = " + firstM.getType());
				if (firstM != null && firstM.getType().compareTo("900") == 0 )
				{
					Log.d("myDebug", "parcing ...");
					downSpawn =Integer.parseInt(firstM.getParameters()[0]);
					Log.d("myDebug", "parameter" + Integer.toString(downSpawn));
					if (downSpawn==  1)
						downSpawn = 0 ;
					else
						downSpawn = 1 ; 
					firstS= "900-"+Integer.toString(downSpawn)+"-\n";
					Log.d("myDebug", "sending " +firstS);
					outToRival.writeBytes(firstS);
					Log.d("myDebug", "sent");
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
			
			Log.d("myDebug", e.toString());
			game.setScreen(new Menu(youHockey));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			Log.d("myDebug", e.toString());
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
		height = Gdx.graphics.getWidth();
		width = Gdx.graphics.getHeight();
		camera.setToOrtho(false, height, width);
        t1 = new Tool("player2.png", true, TOOL_R , lim);
        bot = new Tool("player2.png", false, TOOL_R,lim);
        
        tempTouch = new Vector3();
        
        disk = new Disk(lim, downSpawn);
        disk.spawn();
        stage = new Stage();
        stage.addActor(bot);
        stage.addActor(t1);
        stage.addActor(disk);
        Gdx.input.setInputProcessor(stage);
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

	public void render (float delta) {
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//delta = Gdx.graphics.getDeltaTime();
        Goal bottomGoal = new Goal(true , lim );
        Goal topGoal = new Goal(false , lim );
        
        disk.update(t1, null);
        Vector2 leftB = lim.leftBottomCorner();
        Vector2 leftT = lim.leftTopCorner();
        Vector2 rightB = lim.rightBottomCorner();
        Vector2 rightT = lim.rightTopCorner();
       
        shaper.begin(ShapeType.Line);
        Gdx.gl20.glLineWidth((float) (2 * lim.getyUnit()/ camera.zoom));
        
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
        
        
        
        
        
        
		batch.begin();
		
		yourBitmapFontName.setColor(Color.BLACK);
		textBoxPos = bottomGoal.getSrc(true);
		textBoxPos=  UnitConvertor.toGame(textBoxPos.x , textBoxPos.y);
		ScoreString = Integer.toString(lim.getScoreBottom()) + " : " + 
				Integer.toString(lim.getScoreTop());
		yourBitmapFontName.draw(batch, ScoreString,25,100);
		
		stage.draw();
		batch.end();

		if(Gdx.input.isTouched()) {
			camera.unproject(tempTouch.set(Gdx.input.getX(), Gdx.input.getY(),0));
			tempTouch = UnitConvertor.toNormal(tempTouch);
			t1.move(tempTouch.x, tempTouch.y);
            
			
            
	    }
		
		if (Gdx.input.isKeyPressed(Keys.BACK)){
			music.stop();
			super.dispose();
			game.setScreen(new Menu(game));
			
		}
		stage.act(delta);
		
		
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
					Log.d("reciverT", "recived : " + tmp);
					Message m = new Message(tmp);
					
					toHandel.add(m);
					Log.d("reciverT",tmp + " added succesfuly to Queue");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.d("reciverT",e.toString());
					game.setScreen(new Menu(game));
				}
			}
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
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Log.d("sender",e.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.d("sender",e.toString());
				}
				
				
			}
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
					Log.d("handler" , "Handling : " + m.toString());
					handel(m);
					Log.d("handler" , "Sent ");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Log.d("handler",e.toString());
				}
				
				
			}
		}
		
		private void handel(Message m) {
			// TODO Auto-generated method stub
			String opCode = m.getType();
			String[] params = m.getParameters();
			float x,y,time ; 
			boolean flag1,flag2,flag3,flag4,flag5;
			switch (opCode) {
			case "901":
				Log.d("handler" , "case 901");
				x = Float.parseFloat(params[0])*lim.getGameWidth();
				y = Float.parseFloat(params[1])*lim.getGameHeight();
				time =  Float.parseFloat(params[2]);
				x = lim.getGameWidth()   - x ;
				y = lim.getGameHeight()  - y ; 
				Log.d("handler","Move tool to  x=  "+ x +" y= "+ y
						+" time = "+ time);
				flag1 = x > lim.getLeft() && x < lim.getRight();
				
				flag2 = y > lim.getBottom() &&y < lim.getTop();
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
				x = Float.parseFloat(params[0])*lim.getGameWidth();
				y = Float.parseFloat(params[1])*lim.getGameHeight();
				time =  Float.parseFloat(params[2]);
				x = lim.getGameWidth()   - x ;
				y = lim.getGameHeight()  - y ; 
				String xDir , yDir;
				flag1 = x > lim.getLeft() && x < lim.getRight();
				flag2 = y > lim.getBottom() &&y < lim.getTop();
				flag3 = time > 0 ; 
				xDir = params[3];
				yDir = params[4];
				flag4 = params[3].compareTo("left") == 0 ||
						params[3].compareTo("right") == 0; 
				flag5= params[3].compareTo("top") == 0 ||
						params[3].compareTo("bottom") == 0; 
				
				if (flag1 && flag2 && flag3 && flag4 && flag5)
				{
					disk.setXdir(xDir);
					disk.setXdir(yDir);
					lim.addMoveToAction(disk, x, y, 'D');
				}
				else
				{
					//Send error Massage
					Log.d("handler" , "info not  okay");
				}
				
				
				
				
				
			default:
				break;
			}
		}
		
	}
	
}
