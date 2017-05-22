import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;


public class Enemy {
	private double x;
	private double y;
	private int r;
	
	private double dx;
	private double dy;
	private double rad;
	private double speed;
	
	private int maxHealth;
	private int health;
	private int type;
	private int rank;
	
	private boolean gettingBombed;
	private long gettingBombedTimer;
	private long gettingBombedInterval;
	
	private boolean targeted;
	
	
	private Color color1;
	
	private boolean ready;
	private boolean dead;
	
	//when hit
	private boolean hit;
	private long hitTimer;
	
	private boolean isPulled = false;
	
	private double dxStore;
	private double dyStore;
	private double dxa = 0;
	private double dya = 0;
	
	private String[] fnames = { "Adrian", "Humphrey", "Alex", "Pablo", "Thomas", "Ben", "Jerome", "Killian", "Yichen", "Hansdeep", "John", "Francis" };
	private String[] lnames = { "Kim", "Wong", "Chan", "Sanderson", "Sanguinetti", "Martineau", "Gano", "Rutherford", "Wu", "Singh", "Phillips","Yi" };
	
	private String name;

	
	private boolean slow;
	
	private boolean regenMode;
	private long regenTimer;
	private long regenLength;
	
	//constructor
	public Enemy(int type, int rank){
		this.type = type;
		this.rank = rank;
		regenMode = false;
		regenLength = 700;
		
		int fn = (int) (Math.random() * fnames.length);
		int ln = (int) (Math.random() * lnames.length);
		
		this.name = fnames[fn] + " " + lnames[ln];
		
		//default
		if(type ==1){
			//color1 = Color.blue;
			color1 = new Color(0,0,255,128);
			if(rank ==1){
				speed = 2;
				r=10;
				health = 1;
			}
			if(rank ==2){
				speed = 2;
				r = 20;
				health = 2;
			}
			if(rank ==3){
				speed = 1.5;
				r = 40;
				health = 3;
			}
			if(rank ==4){
				speed = 1.5;
				r = 67;
				health = 14;
			}
		}
		
		if(type ==2){
			//color1 = Color.red;
			color1 = new Color(255,0,0,128);
			if(rank == 1){
				speed = 3;
				r=12;
				health = 1;
			}
			if(rank == 2){
				speed = 3;
				r=24;
				health = 2;
			}
			if(rank == 3){
				speed = 2.6;
				r=44;
				health = 4;
			}
			if(rank == 4){
				speed = 2.4;
				r=66;
				health = 14;
			}
		}
		
		if(type ==3){
			//color1 = Color.GREEN;
			color1 = new Color(0,255,0,128);
			if(rank == 1){
				speed = 1.5;
				r=10;
				health = 5;
			}
			if(rank == 2){
				speed = 1.5;
				r=20;
				health = 6;
			}
			if(rank == 3){
				speed = 1.5;
				r=50;
				health = 7;
			}
			if(rank == 4){
				speed = 1.2;
				r=90;
				health = 19;
			}
		}
		
		if(type ==4){
			//color1 = Color.GREEN;
			color1 = new Color(0,0,0,128);
			if(rank == 1){
				speed = 3.77;
				r=15;
				health = 7;
			}
			if(rank == 2){
				speed = 3.27;
				r=30;
				health = 7;
			}
			if(rank == 3){
				speed = 2.77;
				r=70;
				health = 17;
			}
			if(rank == 4){
				speed = 2.47;
				r=100;
				health = 27;
			}
		}
		if(type ==5){
			//color1 = Color.GREEN;
			color1 = new Color(255,255,0,128);
			if(rank == 1){
				speed = 4.0;
				r=20;
				health = 7;
			}
			if(rank == 2){
				speed = 3.7;
				r=37;
				health = 12;
			}
			if(rank == 3){
				speed = 3.2;
				r=82;
				health = 20;
			}
			if(rank == 4){
				speed = 2.7;
				r=107;
				health = 75;
			}
		}
		
		if(type ==6){
			color1 = new Color(255,255,255,128);
			name = "Marcus Ken Loong Siew";
			if(rank == 1){
				speed = 1;
				r = 120;
				health = 2700;
				maxHealth = 2700;
			}
			
		}
		
		x = Math.random() * GamePanel.WIDTH /2 + GamePanel.WIDTH/4;
		y = -r;
		
		double angle = Math.random() * 140 + 20;
		rad = Math.toRadians(angle);
		
		dx = Math.cos(rad) * speed;
		dy = Math.sin(rad) * speed;
		
		if(x > r && x < GamePanel.WIDTH - r && y > r && y < GamePanel.HEIGHT - r) {
			ready = true;
		}
		dead = false;
		
		hit = false;
		hitTimer = 0;
		
		gettingBombed = false;
		gettingBombedTimer = System.nanoTime();
		gettingBombedInterval = 100;
		
	}
	
	public String getName() { return name; }
	public double getx(){ return x; }
	public double gety(){ return y; }
	public void setx(double x) { this.x = x; }
	public void sety(double y) { this.y = y; }
	public int getr(){ return r; }
	public int getType(){ return type;}
	public int getRank() { return rank;}
	public boolean isGettingBombed() { return gettingBombed;}
	public void setGettingBombed(boolean b) { gettingBombed = b;}
	public void setIsPulled(boolean b) { isPulled = b; }
	public boolean getIsPulled() { return isPulled; }
	
	public void setSlow(boolean b){slow = b;}
	public void setTargeted(boolean b){targeted = b;}
	
	public void produceRandomEnemy(){
		int typeChance = (int)(Math.random() * 100);
		int rankChance = (int)(Math.random() * 100);
		int actualType;
		int actualRank;
		
		
		if(typeChance<10){
			actualType = 5;
		} else if(typeChance<20){
			actualType = 4;
		} else if(typeChance<45){
			actualType = 3;
		} else if(typeChance<70){
			actualType = 2;
		} else {
			actualType = 1;
		}
		
		if(rankChance<20){
			actualRank = 4;
		} else if(rankChance<45){
			actualRank = 3;
		} else if(rankChance<70){
			actualRank = 2;
		} else {
			actualRank = 1;
		}
		
		Enemy e = new Enemy(actualType,actualRank);
		e.setx(x);
		e.sety(y);
		GamePanel.enemies.add(e);
	}
	
	public void heal(){
		int healAmount = (int)(Math.random() * 500);
		if(health+healAmount > maxHealth){
			health = maxHealth;
		} else {
			GamePanel.texts.add(new Text(x, y, 1700, "healed " + healAmount));
			health += healAmount;
		}
	}
	
	public void changeDirectionRandomly(){
		double angle = Math.random() * 360;
		rad = Math.toRadians(angle);
		
		dx = Math.cos(rad) * speed;
		dy = Math.sin(rad) * speed;
	}
	
	public void changeSpeedRandomly(){
		speed = Math.random() * 3 + 1;
	}
	
	public void changeColorRandomly(){
		int r = (int)(Math.random() * 255);
		int g = (int)(Math.random() * 255);
		int b = (int)(Math.random() * 255);
		int a = (int)(Math.random() * 255);
		color1 = new Color(r,g,b,a);
	}
	
	public void explode(){
		if(rank > 1){
			int amount = 0;
			if(type ==1){
				amount = 3;
			}
			if(type ==2){
				amount = 3;
			}
			if(type ==3){
				amount = 3;
			}
			if(type ==4){
				amount = 2;
			}
			if(type ==5){
				amount = 4;
			}
			
			for(int i=0; i<amount; i++){
				Enemy e = new Enemy(getType(), getRank()-1);
				e.setSlow(slow);
				e.setGettingBombed(gettingBombed);
				e.x = this.x;
				e.y = this.y;
				double angle = 0;
				if(!ready){
					angle = Math.random()*140+20;
				} else {
					angle = Math.random() * 360;
				}
				e.rad = Math.toRadians(angle);
				e.dx = Math.cos(e.rad) * speed;
				e.dy = Math.sin(e.rad) * speed;
				GamePanel.enemies.add(e);
			}
		}
	}
	
	public void placeBomb(){
		GamePanel.bombs.add(new Bomb(x,y,true));
	}
	
	public void placeBlackHole(){
		GamePanel.blackholes.add(new BlackHole(x,y,5,true,Math.random()*150,3000));
	}
	
	public boolean isInRange(double bx, double by, double br){
		double xDiff = bx - x;
		double yDiff = by - y;
		double distance = Math.sqrt(xDiff*xDiff +yDiff*yDiff);
		if(distance > (br+r)){
			return false;
		} else {
			return true;
		}
	}
	
	public void saveVector(){
		if(!isPulled){
			dxStore = dx;
			dyStore = dy;
			dxa = 0;
			dya = 0;
		}
	}
	
	public void restoreVector(){
		if(!isPulled){
			dx = dxStore;
			dy = dyStore;
			dxa = 0;
			dya = 0;
		}
	}
	
	public boolean pointCollide(double px, double py, double pr){
		double dist = Math.sqrt((px-x)*(px-x)+(py-y)*(py-y)) - pr;
		if(dist <= r){
			pushAway(px,py);
			return true;
		}
		return false;
	}
	
	public void wallCollide(double wx, double wy, double wwidth, double wheight){
		
		double leftTopdist = Math.sqrt((wx-x)*(wx-x)+(wy-y)*(wy-y));
		double rightTopdist = Math.sqrt((wx+wwidth-x)*(wx+wwidth-x) + (wy-y)*(wy-y));
		double leftBotdist = Math.sqrt((wx-x)*(wx-x)+(wy+wheight-y)*(wy+wheight-y));
		double rightBotdist = Math.sqrt((wx+wwidth-x)*(wx+wwidth-x) + (wy+wheight-y)*(wy+wheight-y));
		
		if(leftBotdist <= r){			
			//dx = -speed;
			//dy = speed;
			pushAway(wx,wy+wheight);
		}
		if(rightBotdist <= r){
			//dx = speed;
			//dy = speed;
			pushAway(wx+wwidth,wy+wheight);
		}
		
		if(leftTopdist <= r){
			//dy = -speed;
			//dx = -speed;
			pushAway(wx,wy);
		}
		
		if(rightTopdist <= r){			
			//dx = speed;
			//dy = -speed;
			pushAway(wx+wwidth,wy);
		}
		
		double wx1 = (wx + wwidth);
		double wy1 = (wy + wheight);
		
		// collide from top or bottom
		if (x <= (wx1) && x >= wx) {
			
			// top to bottom
			if (dy >= 0 && (y + r) >= wy && (y-r ) <= (wy1)) {
				System.out.println("top to bottom");
				//y = wy - r;
				dy = -Math.sin(rad) * speed;
				
			}

			// bot to top
			if (dy <= 0 && (y - r) <= (wy1) && (y+r ) >= wy) {
				System.out.println("bot to top");
				//y = wy + wheight + r;
				dy = Math.sin(rad) * speed;
				
			}
		}
				
		// collide form the sides
		if ((y <= (wy1) && y >= wy)) {
			// left to right collide
			if (dx >= 0 && (x + r) >= wx && (x-r) <= (wx1)) {
				System.out.println("left to right");
				// x = wx-r;
				dx = -Math.cos(rad) * speed;
				
			}

			// right to left collide
			if (dx <= 0 && (x - r) <= (wx1) && (x+r ) >= wx ) {
				System.out.println("right to left");
				//x = wx+wwidth+r;
				dx = Math.cos(rad) * speed;
				
			}
		}
		
		
		
		
		
	}
	
	public void startRegenMode(){
		regenMode = true;
		regenTimer = System.nanoTime();
	}
	
	public void pushAway(double px, double py){
		double xDiff = px-x;
		double yDiff = py-y;
		
			//double angle = Math.atan((y-py)/(x-px));
			if(xDiff<0)
				rad = Math.atan((yDiff)/(xDiff))+Math.toRadians(180);
			else if(yDiff<0)
				rad = Math.atan((yDiff)/(xDiff))+Math.toRadians(360);
			else
				rad = Math.atan((yDiff)/(xDiff));			
			
			dx = -Math.cos(rad) * speed;
			dy = -Math.sin(rad) * speed;
	}
	
	public void pullTowards(double px, double py){
		double xDiff = px-x;
		double yDiff = py-y;
		
			//double angle = Math.atan((y-py)/(x-px));
			if(xDiff<0)
				rad = Math.atan((yDiff)/(xDiff))+Math.toRadians(180);
			else if(yDiff<0)
				rad = Math.atan((yDiff)/(xDiff))+Math.toRadians(360);
			else
				rad = Math.atan((yDiff)/(xDiff));			
			
			dx = Math.cos(rad) * 2;
			dy = Math.sin(rad) * 2;
			
			
			isPulled = true;
			
	}
	
	public void randomizeDirection(){
		dx = Math.cos(Math.toRadians(Math.random()*360))*speed;
		dy = Math.cos(Math.toRadians(Math.random()*360))*speed;

	}
	
	public void goTowards(double px, double py){
		double xDiff = px-x;
		double yDiff = py-y;
		
			//double angle = Math.atan((y-py)/(x-px));
			if(xDiff<0)
				rad = Math.atan((yDiff)/(xDiff))+Math.toRadians(180);
			else if(yDiff<0)
				rad = Math.atan((yDiff)/(xDiff))+Math.toRadians(360);
			else
				rad = Math.atan((yDiff)/(xDiff));			
			
			dx = Math.cos(rad) * speed;
			dy = Math.sin(rad) * speed;
	}
	
	public void hit(){
		if(!regenMode){
			health--;
			if(health <= 0){
				dead = true;
			}
			hit = true;
	
			hitTimer = System.nanoTime();
		} else {
			gainHealth(3);
		}
	}
	
	public void gainHealth(int gain){
		if(health+gain > maxHealth){
			health = maxHealth;
		} else {
			health += gain;
		}
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public void update(){
		if(slow){
			x+= dx*0.3;
			y+= dy *0.3;
		} else {
			x += dx;
			y += dy;
		}		
		
		x+= dxa;
		y+= dya;
		
		if(!ready){
			if(x>r&&x<GamePanel.WIDTH-r
					&& y>r && y<GamePanel.HEIGHT-r){
				ready = true;
			}
		}
		/*
		if(x<r && dx<0) dx = -dx;
		if(y<r && dy<0) dy = -dy;
		if(x> GamePanel.WIDTH-r && dx>0) dx = -dx;
		if(y> GamePanel.HEIGHT-r && dy>0) dy = -dy;
		*/
		if(x<0 && dx<0) x = GamePanel.WIDTH;
		if(y<0 && dy<0) y = GamePanel.HEIGHT;
		if(x> GamePanel.WIDTH && dx>0) x = 0;
		if(y> GamePanel.HEIGHT && dy>0) y = 0;
		
		if(gettingBombed){
			long elapsed = (System.nanoTime()-gettingBombedTimer)/1000000;
			if(elapsed>gettingBombedInterval){
				gettingBombedTimer = System.nanoTime();
				hit();
			}
		}
		
		if(hit){
			long elapsed = (System.nanoTime()-hitTimer)/1000000;
			if(elapsed > 50){

				hit = false;
				hitTimer = 0;
			}
		}
		
		if(regenMode){
			long elapsed = (System.nanoTime() - regenTimer)/1000000;
			if(elapsed > regenLength){
				regenMode = false;
				regenTimer = 0;
			}
		}
		
	}
	
	public void draw(Graphics2D g){
		
		if(type==6 || rank>3){
			g.setColor(Color.WHITE);
			g.setFont(new Font("Century Gothic",Font.PLAIN,12));
			String s = name;
			int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (int)(x-(length)/2), (int)(y));
			}
		
		if(hit){
			g.setColor(color1.brighter());
			g.fillOval((int)(x-r),(int)(y-r), 2*r, 2*r);
			
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.WHITE.darker());		
			g.drawOval((int)(x-r),(int)(y-r), 2*r, 2*r);
			g.setStroke(new BasicStroke(1));
		} else {
			if(isPulled){
				g.setColor(color1.darker());
			} else {
				g.setColor(color1);
			}
			g.fillOval((int)(x-r),(int)(y-r), 2*r, 2*r);
			
			g.setStroke(new BasicStroke(3));
			g.setColor(color1.darker());		
			g.drawOval((int)(x-r),(int)(y-r), 2*r, 2*r);
			g.setStroke(new BasicStroke(1));
		}
		
		if(targeted){
			//g.setColor(Color.red);
			//g.drawOval((int)(x-r/4),(int)(y-r/4), r/2, r/2);
		}
		
		if(regenMode){
			g.setColor(new Color(0,255,0,144));
			g.fillOval((int)(x-r),(int)(y-r), 2*r, 2*r);
			
		}
		
		if(type ==6){
			g.setColor(Color.black);
			g.drawRect((int)(x-50), (int)(y-50), 100, 10);
			g.fillRect((int)(x-50), (int)(y-50), (int)(100*(double)health/(double)maxHealth), 10);
		}
		
		
		
		
		
	}
	
}
