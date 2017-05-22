import java.awt.Color;
import java.awt.Graphics2D;


public class BlackHole {
	private double x;
	private double y;
	private double r;
	
	private double pullRadius;
	
	private long beforeHoleTimer;
	private long beforeHoleDelay;
	
	private long holeLength;
	private long holeTimer;
	private boolean isHole;
	
	//
	private long holeDelayTimer;
	private long holeDelay;
	
	private long pulseLength;
	private long pulseTimer;
	private boolean isPulse;
	
	private boolean hostile;
	
	private boolean started;
	
	public BlackHole(double x, double y, double r){
		this.x = x;
		this.y = y;
		this.r = r;
		
		pullRadius = 200;
		
		beforeHoleTimer = System.nanoTime();
		beforeHoleDelay = 5000;
		
		holeLength = 5000;
		holeTimer = System.nanoTime();
		
		holeDelayTimer = System.nanoTime();
		holeDelay = 300;
		
		pulseLength = 600;
		pulseTimer = System.nanoTime();
		
		isHole = false;
		isPulse = false;
		started = false;
	}
	
	public BlackHole(double x, double y, double r, boolean hostile, double pullR, long length){
		this.x = x;
		this.y = y;
		this.r = r;
		
		pullRadius = pullR;
		
		beforeHoleTimer = System.nanoTime();
		beforeHoleDelay = 4000;
		
		holeLength = length;
		holeTimer = System.nanoTime();
		
		holeDelayTimer = System.nanoTime();
		holeDelay = 300;
		
		pulseLength = 600;
		pulseTimer = System.nanoTime();
		
		isHole = false;
		isPulse = false;
		started = false;
		
		this.hostile = hostile;
	}
	
	public double getPullRadius(){
		return pullRadius;
	}
	
	public double getx(){ return x; }
	public double gety(){ return y; }
	public double getr(){ return r; }
	public void toggleHole(boolean b){ isHole = b; }
	public void togglePulse(boolean b){ isPulse = b; }
	public void startHole() { isHole = true; }
	public void stopHole() { isHole = false; }
	public boolean getHoleStatus() { return isHole; }
	public boolean getPulseStatus() { return isPulse; }
	public boolean isHostile() { return hostile; }
	
	
	
	public boolean update(){
		long beforeElapsed = (System.nanoTime() - beforeHoleTimer)/1000000;
		System.out.println("elapsed = " + beforeElapsed);
		System.out.println("delay = " + beforeHoleDelay);
		if(beforeElapsed > beforeHoleDelay){
			isHole = true;		
			beforeHoleTimer = 0;
			if(!started){
				holeTimer = System.nanoTime();
			}
			started = true;
		}
			if(isHole){
				long holeElapsed = (System.nanoTime() - holeTimer)/1000000;
				if(holeElapsed > holeLength){
					isHole = false;
					isPulse = false;
					
				}
				
				long holeDelayElapsed = (System.nanoTime() - holeDelayTimer)/1000000;
				if(holeDelayElapsed > holeDelay ){
					//System.out.println("hole delay");
					isPulse = true;
					pulseTimer = System.nanoTime();
					holeDelayTimer = System.nanoTime() + pulseLength*1000000;
				}
					
				if(isPulse){
					long pulseElapsed = (System.nanoTime() - pulseTimer)/1000000;
					if(pulseElapsed < pulseLength){
						isPulse = true;
						
						//System.out.println("eh");
					} else {
						//System.out.println("elapsed = " + pulseElapsed);
						//System.out.println("length = " + pulseLength);
						isPulse = false;
						pulseTimer = System.nanoTime();
						holeDelayTimer = System.nanoTime();
						holeDelay -= 27;
						pulseLength -= 2;
					}
				}
				
				
			}
			return (!isHole&&started);
			
		
		
	}
	
	public void draw(Graphics2D g){
		g.setColor(Color.BLACK.brighter());
		if(hostile){
			g.setColor(Color.MAGENTA.darker());
		}
		
		if(!started){
			long beforeElapsed = (System.nanoTime() - beforeHoleTimer)/1000000;
			double radius = pullRadius * ((double)beforeElapsed / beforeHoleDelay);
			g.fillOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
			g.drawOval((int)(x-radius), (int)(y-radius), (int)(2*radius), (int)(2*radius));
			
			
			
			g.drawRect((int)(x-2*r), (int)(y+r), 20, 5);
			g.fillRect((int)(x-2*r), (int)(y+r), (int)(20-20*beforeElapsed/beforeHoleDelay), 5);
			g.setColor(new Color(0,0,0,16));
			g.fillOval((int)(x-radius), (int)(y-radius), (int)(2*radius), (int)(2*radius));
		} else {	
			long holeElapsed = (System.nanoTime() - holeTimer)/1000000;
			
			g.fillOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
			g.drawOval((int)(x-pullRadius), (int)(y-pullRadius), (int)(2*pullRadius), (int)(2*pullRadius));
			
			
			
			g.drawRect((int)(x-2*r), (int)(y+r), 20, 5);
			g.fillRect((int)(x-2*r), (int)(y+r), (int)(20-20*holeElapsed/holeLength), 5);
			
			g.setColor(new Color(0,0,0,32));
			g.fillOval((int)(x-pullRadius), (int)(y-pullRadius), (int)(2*pullRadius), (int)(2*pullRadius));
		}
		if(isPulse){
			g.setColor(Color.BLACK);
			long pulseElapsed = (System.nanoTime() - pulseTimer)/1000000;
			g.drawOval((int)(x-(pullRadius - pullRadius*((double)pulseElapsed / pulseLength))), (int)(y-(pullRadius - pullRadius*((double)pulseElapsed / pulseLength))), (int)(2*pullRadius - 2*pullRadius*((double)pulseElapsed / pulseLength)), (int)(2*pullRadius - 2*pullRadius*((double)pulseElapsed / pulseLength)));
			// previous error : long / long is like integer division
		}		
	}
	
	
}
