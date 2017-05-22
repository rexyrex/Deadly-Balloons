import java.awt.Color;
import java.awt.Graphics2D;


public class Wall {
	private double x;
	private double y;
	private double width;
	private double height;
	
	private long timer;
	private long lifeTime;
	
	private Color c;
	
	public Wall(double x, double y){
		this.x = x;
		this.y = y;
		this.width = 3;
		this.height = 3;
		
		this.timer = System.nanoTime();
		this.lifeTime = 30000;
		this.c = new Color(0,0,0,128);
	}
	
	public double getx(){ return x;}
	public double gety(){ return y;}
	public double getHeight(){ return height;}
	public double getWidth(){ return width;}
	
	
	public boolean update(){
		long elapsed = (System.nanoTime() - timer)/1000000;
		if(elapsed > lifeTime){
			return true;
		}
		
		return false;
	}
	
	public void draw(Graphics2D g){
		g.setColor(c);
		g.drawRect((int)x,(int)y,(int)width,(int)height);
		long elapsed = (System.nanoTime() - timer)/1000000;
		g.setColor(new Color(0,0,0,(int)(255-255*((double)elapsed/lifeTime))));
		g.fillRect((int)x,(int)y,(int)width,(int)height);
	}
}
