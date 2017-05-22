import java.awt.Color;
import java.awt.Graphics2D;


public class Explosion {
	
	private double x;
	private double y;
	private int r;
	private int maxRadius;
	private int radiusChangeSpeed;
	private Color c;
	
	//constructor
	public Explosion(double x, double y, int r, int max){
		this.x = x;
		this.y = y;
		this.r = r;
		radiusChangeSpeed = 3;
		maxRadius = max;
		c=(new Color(255,255,255,147));
	}
	
	//turret explosion
	public Explosion(double x, double y, int r, int max, int speed){
		this.x = x;
		this.y = y;
		this.r = r;
		radiusChangeSpeed = speed;
		maxRadius = max;
		c=(new Color(255,0,3,177));
	}
	
	public boolean update(){
		r+=radiusChangeSpeed;
		if(r>= maxRadius){
			return true;
		}
		return false;
	}
	
	public void draw(Graphics2D g){
		g.setColor(c);
		g.drawOval((int)(x-r),(int)(y-r), 2*r, 2*r);
	}
	
}
