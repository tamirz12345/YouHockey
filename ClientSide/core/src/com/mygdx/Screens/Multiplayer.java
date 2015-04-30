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

import com.badlogic.gdx.ApplicationAdapter;
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
    
    
    
    BlockingQueue<Message> toSend;
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
    
	Reciver reciver ;
	Handler handler ;
	Sender  sender  ;
	Message firstM ;
	String firstS= "900-";
    public Multiplayer(YouHockey youHockey ,String serverAddr ,String rivalAddr , int port) {
    	this.game = youHockey;
    	toSend = new LinkedBlockingDeque<Message>();
    	toHandel = new LinkedBlockingDeque<Message>();
    	
    	String[] temp = rivalAddr.split(":");
    	inisiator = temp.length == 2 ; 
    	
    	
    	
    	try {
    		if (inisiator)
    		{
    			rivalAddress = new InetSocketAddress(temp[0], Integer.parseInt(temp[1]));
     			rival = new Socket();
    			rival.connect(rivalAddress, 150000);
    			outToRival = new DataOutputStream(rival.getOutputStream());
    			inFromRival = new BufferedReader(new InputStreamReader(rival.getInputStream()));
    			Random r = new Random();
				downSpawn = r.nextInt(2) % 2;
				firstS += Integer.toString(downSpawn)+"-\n";
				
				outToRival.writeBytes(firstS);
				String recived= inFromRival.readLine();
				firstM= new Message(inFromRival.readLine());
				if (firstM.getType().compareTo("990-") == 0 )
				{
					int otherSide=Integer.parseInt(firstM.getParameters()[0]);
					if ((otherSide == 1 && downSpawn == 0 ) || (otherSide == 0 && downSpawn == 1 ))
						playing= true;
				}
    		}
    		else
    		{
    			ActiveRival = new ServerSocket(port);
				rival = ActiveRival.accept();
				outToRival = new DataOutputStream(rival.getOutputStream());
				inFromRival = new BufferedReader(new InputStreamReader(rival.getInputStream()));
				firstM= new Message(inFromRival.readLine());
				if (firstM.getType().compareTo("990-") == 0 )
				{
					downSpawn =Integer.parseInt(firstM.getParameters()[0]);
					if (downSpawn==  1)
						downSpawn = 0 ;
					else
						downSpawn = 1 ; 
					firstS= "990-"+Integer.toString(downSpawn)+"-\n";
					outToRival.writeBytes(firstS);
					playing= true;
				}
    		}
    		
    		
			
			
		
			if (!playing)
				throw new Exception("Game Start Error");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			game.setScreen(new Menu(youHockey));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			game.setScreen(new Menu(youHockey));
		}
    	
    	
		sender = new Sender(toSend,outToRival);
		handler = new Handler(toHandel);
		reciver = new Reciver(toHandel, inFromRival);
		
		
    	sender.execute();
		handler.execute();
		reciver.execute();
		
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
            bot.move(t1.getX(), height - t1.getY());
			
            
	    }
		
		if (Gdx.input.isKeyPressed(Keys.BACK)){
			music.stop();
			super.dispose();
			game.setScreen(new Menu(game));
			
		}
		stage.act(delta);
		
		
	}
	
	
	public class Reciver extends AsyncTask<String, Void, String> 
	{

		public Reciver(BlockingQueue<Message> toHandel,
				BufferedReader inFromRival) {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public class Sender extends AsyncTask<String, Void, String> 
	{

		public Sender(BlockingQueue<Message> toSend,
				DataOutputStream outToServer) {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public class Handler extends AsyncTask<String, Void, String> 
	{

		public Handler(BlockingQueue<Message> toHandel) {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
}
