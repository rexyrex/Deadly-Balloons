import java.awt.Color;
import java.awt.Graphics2D;


public class Bomb {
	private double x;
	private double y;
	private double r;
	private double maxr;
	
	private long triggerTimer;
	private long triggerDelay;
	
	private long bombLength;
	private long bombTimer;
	
	private boolean isBombing;
	private boolean doneBombing;
	
	private long bombColorSwitchTimer;
	private long bombColorSwitchTime;
	private boolean colorSwitchState;
	
	private Color c;
	
	private Color cBomb1;
	private Color cBomb2;
	
	private boolean hostile;
	
	public Bomb(double x, double y){
		this.x = x;
		this.y = y;
		this.r = 5;
		this.maxr = 150 * (Math.random()+0.5);
		
		triggerTimer = System.nanoTime();
		triggerDelay = 3000;
		
		bombLength = 700;
		bombTimer = System.nanoTime();
		isBombing = false;
		doneBombing = false;
		c= new Color(255,255,255,177);
		cBomb1 = new Color(255,0,7,128);
		cBomb2 = Color.MAGENTA;
		
		bombColorSwitchTime = 20;
		bombColorSwitchTimer = System.nanoTime();
		colorSwitchState = false;
		hostile = false;
	}
	
	//enemy bomb
	public Bomb(double x, double y, boolean hostile){
		this.x = x;
		this.y = y;
		this.r = 5;
		this.maxr = 80 * (Math.random()+0.5);
		
		triggerTimer = System.nanoTime();
		triggerDelay = 3000;
		
		bombLength = 700;
		bombTimer = System.nanoTime();
		isBombing = false;
		doneBombing = false;
		c= new Color(255,0,0,222);
		cBomb1 = new Color(255,0,7,128);
		cBomb2 = Color.MAGENTA;
		
		bombColorSwitchTime = 20;
		bombColorSwitchTimer = System.nanoTime();
		colorSwitchState = false;
		this.hostile = hostile;
	}
	
	public boolean isHostile() { return hostile; }
	
	public double getx(){ return x; }
	public double gety(){ return y; }
	public double getmaxr() { return maxr; }
	public boolean getIsDoneBombing() { return doneBombing; }
	public boolean getIsBombing() { return isBombing; }
	
	public void startBombing(){
		bombTimer = System.nanoTime();
		isBombing= true;
	}
	
	public long getBombingTime(){
		return bombLength;
	}
	
	public void toggleColor(){
		if(colorSwitchState){
			colorSwitchState = false;
		} else {
			colorSwitchState = true;
		}
	}
	
	public boolean update(){
		if(doneBombing){
			return true;
		} else {
			long triggerElapsed = (System.nanoTime() - triggerTimer)/ 1000000;
			if(triggerElapsed > triggerDelay){
				if(!isBombing){
					startBombing();
				}
				long bombElapsed = (System.nanoTime() - bombTimer)/1000000;
				if(bombElapsed > bombLength){
					isBombing = false;
					doneBombing = true;
				}
			}
			
			
			
			return false;
		}
	}
	
	public void draw(Graphics2D g){
		if(!isBombing){
			long triggerElapsed = (System.nanoTime() - triggerTimer)/ 1000000;
			g.setColor(c);
			double radius = maxr * ((double)triggerElapsed / triggerDelay);
			g.fillOval((int)(x-r), (int)(y-r), (int)(r*2), (int)(r*2));
			g.drawOval((int)(x-radius), (int)(y-radius), (int)(radius*2), (int)(radius*2));
			
			
			g.drawRect((int)(x-2*r), (int)(y+r), 20, 5);
			g.fillRect((int)(x-2*r), (int)(y+r), (int)(20-20*triggerElapsed/triggerDelay), 5);
			
		} else {
			long elapsed = (System.nanoTime() - bombColorSwitchTimer)/1000000;
			if(elapsed > bombColorSwitchTime){
				bombColorSwitchTimer = System.nanoTime();
				toggleColor();
			}
			
			long bombElapsed = (System.nanoTime() - bombTimer)/1000000;
			if(colorSwitchState){
				g.setColor(cBomb2);
				g.fillOval((int)(x-r), (int)(y-r), (int)(r*2), (int)(r*2));
				g.drawOval((int)(x-maxr), (int)(y-maxr), (int)(maxr*2), (int)(maxr*2));
				
			} else {
				g.setColor(cBomb1);
				g.fillOval((int)(x-r), (int)(y-r), (int)(r*2), (int)(r*2));
				g.fillOval((int)(x-maxr), (int)(y-maxr), (int)(maxr*2), (int)(maxr*2));
			}
			g.drawRect((int)(x-2*r), (int)(y+r), 20, 5);
			g.fillRect((int)(x-2*r), (int)(y+r), (int)(20*bombElapsed/bombLength), 5);
		}
	}
}
