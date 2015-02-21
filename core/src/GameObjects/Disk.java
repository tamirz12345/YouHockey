package GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.mygdx.game.UnitConvertor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class Disk  extends Actor{
	Texture texture = new Texture("disk.png");
	float radius = 32 ; 
	boolean toHuman = false;
	Vector2 targetLoc = null;
	boolean moving;
	float m, n; // y = mx + n
    double speed = 0.05;
	Limits game;
	Wall targetW = null;
	Wall wY = null , wX = null;
	Line l;
    Music music;


    public Disk(Limits game) {
		super();
		this.game = game;
		this.setWidth(64);
		this.setHeight(64);
		this.music = Gdx.audio.newMusic(Gdx.files.internal("hit.mp3"));
	}

	@Override
	public void draw(Batch batch, float alpha){
		Vector2 v = UnitConvertor.toGame(super.getX(),super.getY());
        batch.draw(texture,v.x,v.y);
    }
	
	
	public void checkCollision(Tool tool){
        long hitTime = 0;
        float diskX = this.getX();
        float diskY = this.getY() ;

        float toolX = tool.getX() ;
        float toolY = tool.getY() ;

        if (Math.sqrt( Math.pow(diskX - toolX, 2) + Math.pow(diskY - toolY, 2))
        		<= this.radius + tool.radius)
        {
        	if (!music.isPlaying())
        	{
        		music.play();
        	}
        	

            this.clearActions();
        	float  a =(diskY - toolY)/ (diskX - toolX);
           
            l = new Line(a, new Vector2(diskX , diskY));
            
            if (diskY - toolY >= 0)
            {
            	wY = Wall.Top;
            }
            else
            {
            	wY = Wall.Bottom;
            }
            
            if (diskX- toolX >= 0)
            {
            	wX = Wall.Right;
            }
            else
            {
            	wX = Wall.Left;
            }
            LineToAction(l , wX , wY);
        }
    }
	
	
	/*
	 * Pre Condition : Recives line it wants to move by it 
	 * 				   XDirectino is bottom or top
	 * 				   YDriection is left ot right
	 * What id does is giving the line and the directions it needs to moves it generates
	 * the next action and adds it to the action of this disk
	 */
	public void LineToAction(Line l , Wall XDirection , Wall YDirection )
	{
		
	    
	    
	    if (YDirection == Wall.Top && XDirection == Wall.Left)
	    {
	    	if (l.getY(game.getLeft()) > 0 && l.getY(game.getLeft())<game.getGameHeight())
	    	{
	    		targetW = Wall.Left;
	    	}
	    	else
	    	{
	    		targetW = Wall.Top;
	    	}
	    }
	    
	    else if (YDirection == Wall.Top && XDirection == Wall.Right)
	    {
	    	if (l.getY(game.getRight()) >0  && l.getY(game.getRight()) < game.getGameHeight())
	    	{
	    		targetW = Wall.Right;
	    	}
	    	else
	    	{
	    		targetW = Wall.Top;
	    	}
	    }
	    
	    
	    else if (YDirection == Wall.Bottom && XDirection == Wall.Left)
	    {
	    	if (l.getY(game.getLeft()) > 0 && l.getY(game.getLeft())<game.getGameHeight())
	    	{
	    		targetW = Wall.Left;
	    	}
	    	else
	    	{
	    		targetW = Wall.Bottom;
	    	}
	    }
	    
	    else if (YDirection == Wall.Bottom && XDirection == Wall.Right)
	    {
	    	if (l.getY(game.getRight()) >0  && l.getY(game.getRight()) < game.getGameHeight())
	    	{
	    		targetW = Wall.Right;
	    	}
	    	else
	    	{
	    		targetW = Wall.Bottom;
	    	}
	    }
	    
	    
	    
	    switch (targetW)
	    {
	    case Bottom:
	    	this.addMoveToAction(l.getX(game.getBottom()), game.getBottom());
	    	break;
	    case Top:
	    	this.addMoveToAction(l.getX(game.getTop()),game.getTop());
	    	break;
	    case Left:
	    	this.addMoveToAction(game.getLeft(), l.getY(game.getLeft()));
	    	break;
	    	
	    case Right:
	    	this.addMoveToAction(game.getRight(),l.getY(game.getRight()));
	    	break;
	    }
	}

	public void addMoveToAction(float x , float y)
	{
		if (!game.inGameBounds(x, y))
		{
			System.out.println("Out Of Bound ( "+x+","+y+" )");
		}
		MoveToAction moveAction = new MoveToAction();
		float oneF = 1f;
		float speed = (int) Math.sqrt(Math.pow(this.getX()-  x , 2) 
				+ Math.pow(this.getY()-y, 2)) / 100 * oneF;
		moveAction.setDuration(speed);
		moveAction.setPosition(x, y);
		this.addAction(moveAction);
		this.targetLoc = new Vector2(x,y);
	}
 
    public void update(Tool t1 , Tool t2)
    {
    	this.checkCollision(t1);
		this.checkCollision(t2);
		//To DO :
    	//Check if it near one of the walls and if it does  so decide the next action using the function
    	//LineToAction(Line l , Wall XDirection , Wall YDirection )
        if (this.getActions().size == 0)// Not Moving , there is a chanse it is near a wall now, it need to be handeld 
        {
        	if (targetLoc!= null && this.getX() == targetLoc.x && this.getY() == targetLoc.y)
        	{
        		float newA = this.l.getA() * (- 1);
        		
        		boolean changed = false;
        		l = new Line(newA, new Vector2(this.getX() , this.getY()));
        		if (targetW == Wall.Bottom && this.getY() == game.getBottom())
        		{
        			wY = Wall.Top;
        			changed = true;
        		}
        			
        		if (targetW == Wall.Top && this.getY() == game.getTop())
        		{
        			wY = Wall.Bottom;
        			changed = true;
        		}
        			
        		if (targetW == Wall.Left && this.getX() == game.getLeft())
        		{
        			wX = Wall.Right;
        			changed = true;
        		}
        			
        		if (targetW == Wall.Right && this.getX() == game.getRight())
        		{
        			wX = Wall.Left;
        			changed = true;
        		}
        		if (changed)
        		{
        			LineToAction(l, wX , wY);
        			if (!music.isPlaying())
                	{
                		music.play();
                	}
        		}
        			
        		else
        		{
            		System.out.println("Disk stoped because of a problem somewhere");
            		this.spawn();
            	}
        	}

        	if (!game.inGameBounds(this))
        	{
        		this.spawn();
        	}
        }
    }


	public void spawn() {
		this.clearActions();
		this.setPosition(game.getGameWidth()/2 - this.getWidth()/2 , 
				(float) (game.getGameHeight() * 0.4 - this.getHeight()/2));
	}

	@Override
	public void clearActions() {
		// TODO Auto-generated method stub
		super.clearActions();
		targetLoc = null;
		wX = null;
		wY = null;
		targetW = null;
	}
    
    
}
