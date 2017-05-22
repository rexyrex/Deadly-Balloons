import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;


public class PowerUp {
	private double x;
	private double y;
	private int r;
	private int type;
	private String name;
	
	private Color color1;
	
	private long timer;
	private long lifeTime;
	
	private double staminaGain;
	
	
	
	//1 -- +1 life
	//2 -- +1 power
	//3 -- +2 power
	//4 -- slowdown time
	//5 -- player speed
	//6 -- +1 Bomb
	//7 -- fireSpeed
	//8 -- start Spazing
	//9 -- addons
	//10 - following missile
	//11 - + stamina
	//12 - + max stamina
	
	public PowerUp(int type, double x, double y){
		this.type = type;
		this.x = x;
		this.y = y;
		timer = System.nanoTime();
		lifeTime = 10000;
		
		if(type == 1){
			color1 = Color.pink;
			r=3;
			name="Life";
		}
		if(type == 2){
			color1 = Color.yellow;
			r=3;
			name="Power";
		}
		if(type ==3){
			color1 = Color.yellow;
			r=5;
			name="Power";
		}
		if(type == 4){
			color1 = Color.white;
			r = 3;
			name="Slow";
		}
		if(type == 5){
			color1 = Color.CYAN;
			r = 5;
			name="M.Speed";
		}
		if(type == 6){
			color1 = Color.BLACK;
			r = 4;
			name="BOMB";
		}
		if(type == 7){
			color1 = Color.PINK;
			r = 4;
			name="Att. Speed";
		}
		if(type == 8){
			color1 = Color.blue;
			r = 4;
			name="Spaz";
		}
		if(type == 9){
			color1 = Color.yellow.brighter();
			r = 4;
			name = "Add On";
		}
		if(type == 10){
			color1 = Color.ORANGE.brighter();
			r = 4;
			name = "Seeker Missiles";
		}
		if(type == 11){
			color1 = Color.RED.brighter();
			r = 5;
			name = "+ Stamina";
			staminaGain = Math.random() * 700;
		}
		if(type == 12){
			color1 = Color.RED.brighter();
			r = 6;
			name = "+ Max Stamina";
			staminaGain = Math.random() * 300;
		}
		if(type == 13){
			color1 = Color.PINK;
			r = 8;
			name = "WALL";
		}
	}
	
	public double getx(){ return x; }
	public double gety(){ return y; }
	public double getr(){ return r; }
	public int getType(){ return type; }
	public double getStaminaGain() { return staminaGain; }
	
	
	public boolean update(){
		
		y += 2;
		
		long elapsed = (System.nanoTime() - timer)/1000000;
		if(elapsed > lifeTime){
			return true;
		}
		
		if(y>GamePanel.HEIGHT+r){
			y = 0;
		}
		
		return false;
		
	}
	
	public void draw(Graphics2D g){
		g.setColor(color1);
		g.fillRect((int)(x-r),(int)(y-r), 2*r, 2*r);
		
		g.setStroke(new BasicStroke(3));		
		g.setColor(color1.darker());
		g.drawRect((int)(x-r),(int)(y-r), 2*r, 2*r);
		g.setStroke(new BasicStroke(1));
		
		long elapsed = (System.nanoTime() - timer)/1000000;
		g.drawRect((int)(x-8),(int)(y+2.3*r),16, 6);
		g.fillRect((int)(x-8),(int)(y+2.3*r),(int)(16-16*((double)elapsed/lifeTime)), 6);
		
		g.setFont(new Font("Century Gothic",Font.PLAIN,16));
		int length = (int) g.getFontMetrics().getStringBounds(name, g).getWidth();
		g.drawString(name, (int)(x-length/2), (int)y-7);
	}
	
	
}
