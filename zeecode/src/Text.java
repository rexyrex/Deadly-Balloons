import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;


public class Text {
	private double x;
	private double y;
	private long time;
	private String s;
	
	private long start;
	
	private boolean other;
	private Color c;
	
	public Text(double x, double y, long time, String s){
		this.x = x;
		this.y = y;
		this.time = time;
		this.s = s;
		start = System.nanoTime();
	}
	
	public Text(double x, double y, long time, String s, boolean other, Color c){
		this.x = x;
		this.y = y;
		this.time = time;
		this.s = s;
		start = System.nanoTime();
		this.c = c;
		this.other = other;
	}
	
	public boolean update(){
		long elapsed = (System.nanoTime()-start)/1000000;
		if(elapsed>time){
			return true;
		}
		return false;
	}
	public void draw(Graphics2D g){
		g.setFont(new Font("Century Gothic", Font.PLAIN,12));
		long elapsed = (System.nanoTime()-start)/1000000;
		int alpha = (int) ((255*Math.sin(3.14 * elapsed/time)));
		if(alpha>255) alpha = 255;
		if(alpha<0) alpha = 0;
		
		g.setColor(new Color(255,255,255,alpha) );
		if(other){
			g.setColor(c);
		}
		int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, (int)(x-(length/2)), (int)y);
	}
}
