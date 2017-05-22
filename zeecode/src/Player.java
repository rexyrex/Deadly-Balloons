import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Random;

import Audio.AudioPlayer;

public class Player {

	private int x;
	private int y;
	private int r; // radius
	
	private int dx;
	private int dy;
	private int speed;
	
	private HashMap<String, AudioPlayer> sfx;
	
	private int lives;
	
	private Color color1;
	private Color color2;
	
	//addon colors
	private Color c1 = new Color(255,0,0,164);
	private Color c2 = new Color(0,255,0,164);
	private Color c3 = new Color(0,0,255,164);
	private Color c4 = new Color(255,255,0,164);

	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	
	private boolean spazing;
	private long spazingTimer;
	private long spazingDelay;
	private long spazLength;
	private long spazActualTimer;
	
	private int fixedSpazAngle=0;
	
	private boolean bombing;
	private long bombingTimer;
	private long bombingLength;
	
	private int bombs;
	
	private int addOn;
	private boolean addOnEnable;
	private double addOnShootAngle;
	private long addOnShootTimer;
	private long addOnShootDelay;

	
	//addOnAngles
	private double spinAngle=0;
	private double spinAngle2=180;
	private double spinAngle3=90;
	private double spinAngle4=270;

	
	private boolean firing;
	private long firingTimer;
	private long firingDelay;
	
	//following missile
	private boolean firingSide;
	private long firingSideTimer;
	private long firingSideDelay;
	private long firingSideActualTimer;
	private long firingSideLength;
	
	private boolean recovering;
	private long recoveryTimer;
	private long recoveryBlinkTimer;
	
	private double pushRadius;
	private boolean isPushing;
	private long pushTimer;
	private long pushLength;
	private long pushDrawTimer;
	private long pushDrawLength;
	
	private double maxStamina;
	private double currentStamina;
	private double staminaRegen;
	private long staminaTimer;
	private long staminaGainDelay;
	
	private int score;
	
	private boolean invincible;
	private long inviTimer;
	private long inviDelay;
	private double inviStaminaCost;
	
	private Turret[] ts = new Turret[5];
	private boolean[] tsAvailability = new boolean[5];
	
	private double rad;
	
	private boolean immobalized;
	
	private int nWalls;
	
	
	private int powerLevel;
	private int power;
	private int[] requiredPower = {
			
			1,2,3,4,6,8,10,12,14,16,18,20,22,24,26,28,30
	};
	
	
	//Constructor
	public Player(){
		x = GamePanel.WIDTH/2;
		y = GamePanel.HEIGHT/2;
		r = 7;
		
		dx = 0;
		dy= 0;
		speed = 4;
		 
		lives = 3;
		color1 = Color.WHITE;
		color2 = Color.RED;
		
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("laser", new AudioPlayer("/SFX/shoot.mp3"));
		sfx.put("side", new AudioPlayer("/SFX/side_missile.mp3"));
		
		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 220;
		
		firingSide = false;
		firingSideTimer = System.nanoTime();
		firingSideDelay = 300;
		firingSideLength = 3000;
		firingSideActualTimer = System.nanoTime();
		
		recovering = false;
		recoveryTimer = 0;
		recoveryBlinkTimer = 0;
		
		spazing = false;
		spazingTimer = 0;
		spazingDelay = 7;
		spazLength = 700;
		spazActualTimer = 0;
		
		bombing = false;
		bombingTimer = System.nanoTime();
		bombingLength = 700;
		
	
		
		bombs =1;
		
		addOn = 0;
		addOnShootTimer = System.nanoTime();
		addOnShootAngle = 270;
		addOnShootDelay = 370;
		
		addOnEnable = true;
		
		pushRadius = 200;
		isPushing = false;
		pushTimer = System.nanoTime();
		pushLength = 1000;
		pushDrawLength = 300;
		
		maxStamina = 1000;
		currentStamina = 1000;
		staminaRegen = 5;
		staminaTimer = System.nanoTime();
		staminaGainDelay = 50;
		
		inviTimer = System.nanoTime();
		inviDelay = 50;
		inviStaminaCost = 27;
		
		invincible = false;
		immobalized = false;
		
		nWalls = 0;
		
		score = 0;
	}
	
	//setters
	public void setLeft(boolean b){ left = b;}
	public void setRight(boolean b){ right = b;}
	public void setUp(boolean b){ up = b;}
	public void setDown(boolean b){ down = b;}
	public void setFiring(boolean b){ firing = b;}
	public void setSpazing(boolean b){spazing = b;}
	public boolean isRecovering(){ return recovering;}
	public int getScore() { return score; }
	public boolean isPushing() { return isPushing; }
	public double getPushRadius(){ return pushRadius; }
	public void startInvincible(){ invincible = true; inviTimer = System.nanoTime();}
	public void stopInvincible(){ invincible = false; }
	public void setImmobalized(boolean b){ immobalized = b; }
	
	public void toggleInvincible(){
		if(invincible){
			stopInvincible();
		} else {
			startInvincible();
		}
	}
	
	public double getAttSpeed(){
		return (300-firingDelay)/4;
	}
	
	
	public boolean isInvincible() { return invincible; }
	
	public void gainAddOn(){ addOn++;	
		if(addOn==3){
			spinAngle=0;
			spinAngle2 = 120;
			spinAngle3 = 240;
		}
		if(addOn==4){
			spinAngle=0;
			spinAngle2=180;
			spinAngle3=90;
			spinAngle4=270;
		}
	
	}
	
	public void moveToward(double px, double py){
		double xDiff = px-x;
		double yDiff = py-y;
		
			//double angle = Math.atan((y-py)/(x-px));
			if(xDiff<0)
				rad = Math.atan((yDiff)/(xDiff))+Math.toRadians(180);
			else if(yDiff<0)
				rad = Math.atan((yDiff)/(xDiff))+Math.toRadians(360);
			else
				rad = Math.atan((yDiff)/(xDiff));			
			
			dx = (int) (Math.cos(rad) * 4);
			dy = (int) (Math.sin(rad) * 4);
	}
	
	public void moveAwayFrom(double px, double py){
		double xDiff = px-x;
		double yDiff = py-y;
		
			//double angle = Math.atan((y-py)/(x-px));
			if(xDiff<0)
				rad = Math.atan((yDiff)/(xDiff))+Math.toRadians(180);
			else if(yDiff<0)
				rad = Math.atan((yDiff)/(xDiff))+Math.toRadians(360);
			else
				rad = Math.atan((yDiff)/(xDiff));			
			
			dx = (int) (-Math.cos(rad) * speed);
			dy = (int) (-Math.sin(rad) * speed);
	}
	
	public void changeDirectionRandomly(){
		double angle = Math.random() * 360;
		rad = Math.toRadians(angle);
		
		dx = (int) (Math.cos(rad) * speed);
		dy = (int) (Math.sin(rad) * speed);
	}
	
	public void startPushing(){
		isPushing = true;
		pushTimer = System.nanoTime();
	}
	
	public void placeBlackHole(){
		GamePanel.blackholes.add(new BlackHole(x,y,5));
	}
	
	
	
	public void incFireRate(){ firingDelay -= 27; if(firingDelay<100){firingDelay=100;}}
	public long getFiringDelay() { return firingDelay; }
	
	public void incBombs(){bombs++;}
	public int getBombs(){return bombs;}
	
	public void addScore(int i) { score+=i; };
	
	public void loseLife(){
		if(!invincible){
			lives--;
			recovering = true;
			recoveryTimer = System.nanoTime();
		}
	}
	
	public void startBombing(){
		if(bombs>0){
			bombs--;
			bombing = true;
			bombingTimer = System.nanoTime();
		}
	}
	
	public void startSpazing(){
		spazing = true;
		spazActualTimer = System.nanoTime();
	}
	
	public void startFiringSide(){
		firingSide = true;
		firingSideTimer = System.nanoTime();
		firingSideActualTimer = System.nanoTime();
	}
	
	public void increasePower(int i){
		power += i;
		if(powerLevel==16){
			if(power > requiredPower[powerLevel]){
				power = requiredPower[powerLevel];
			}
		}
		if(power >= requiredPower[powerLevel]){
			power -= requiredPower[powerLevel];
			powerLevel++;			
		}
	}
	
	public int getPowerLevel() {return powerLevel;}
	public int getPower() { return power; }
	public int getRequiredPower() { return requiredPower[powerLevel];}
	
	public void toggleAddOn() { if(addOnEnable){addOnEnable=false;}else{addOnEnable=true;} }
	
	public void incSpeed(){ speed++; if(speed>8) speed=8;}
	public int getSpeed(){return speed;}
	
	public void setBombing(boolean b){bombing = b; if(b==true){bombingTimer=System.nanoTime();}}
	public boolean isBombing(){return bombing;}
	
	public double getMaxStamina() { return maxStamina; } 
	public double getCurrentStamina() { return currentStamina; }

	public boolean useStamina(double s) {
		if (s > currentStamina) {
			GamePanel.texts.add(new Text(x, y, 200, ""+s+" stamina needed!",true,new Color(255,0,0,255)));
			return false;
		} else {
			GamePanel.texts.add(new Text(x, y, 200, "-"+s+" stamina",true,new Color(255,0,0,255)));
			currentStamina -= s;
			return true;
		}
	}

	public void gainStamina(double s) {
		if ((s + currentStamina) > maxStamina) {
			currentStamina = maxStamina;
		} else {
			currentStamina += s;
		}
	}
	
	public void increaseMaxStamina(double s){
		maxStamina += s;
	}

	public boolean updateTurretAvailability(){
		for(int i=0; i<ts.length; i++){
			if(ts[i]==null){
				tsAvailability[i] = true;
			} else if(ts[i].isDead()){
				tsAvailability[i] = true;
			} else {
				tsAvailability[i] = false;
			}
		}
		for(int i=0; i<ts.length; i++){
			if(ts[i]==null){
				return true;
			} else if(ts[i].isDead()){
				return true;
			}
		}
		return false;
	}
	
	public int getTurretAvailabilityIndex(){
		for(int i=0; i<ts.length; i++){
			if(tsAvailability[i] == true){
				return i;
			}
		}
		return 0;
	}
	public void placeTurret(){
		int i =0;
		if(updateTurretAvailability()){
			i = getTurretAvailabilityIndex();
			Turret t = new Turret(x,y,i);
			GamePanel.turrets.add(t);
			ts[i] = t;
			tsAvailability[i] = false;
			//System.out.println(i+" set to " + tsAvailability[i]);
		}	
	}
	
	public void placeBomb(){
		if(bombs>0){
			bombs--;
			GamePanel.bombs.add(new Bomb(x,y));
		}
		
	}
	
	public void placeWall(){
		GamePanel.walls.add(new Wall(x-r,y-r));
	}
	
	public void placeLineWall(){
		if(nWalls>0){
			nWalls--;
			GamePanel.linewalls.add(new LineWall(x,y,17));
		}
		
	}
	
	public void gainWalls(int gain){
		nWalls += gain;
	}
	
	public int getWalls(){
		return nWalls;
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
	
	
	public void tpToTurret(int i){ // removal of turret happens in gamePanel
		updateTurretAvailability();
		//System.out.println(i+" is " + tsAvailability[i]);
		if(tsAvailability[i] == false){
			Turret t = ts[i];
			x = (int) t.getx();
			y = (int) t.gety();
			t.setDead(true);			
			tsAvailability[i] = true;		
			GamePanel.explosions.add(new Explosion(x,y,r,r+20,1));
			GamePanel.explosions.add(new Explosion(x,y,r,r+50,2));
			GamePanel.explosions.add(new Explosion(x,y,r,r+70,3));
		}
		
	}
	
	public void updateEnemyAngle(double ex, double ey){
		double xDiff = ex - x;
		double yDiff = ey - y;
		
		if(xDiff <0)
			addOnShootAngle =  Math.toDegrees(Math.atan((y-ey)/(x-ex)))+180;
		else if(yDiff <0)
			addOnShootAngle =  Math.toDegrees(Math.atan((y-ey)/(x-ex)))+360;
		else 
			addOnShootAngle =  Math.toDegrees(Math.atan((y-ey)/(x-ex)));
	}
	
	public void update(){
		if(!immobalized){
			if(left){
				dx = -speed;
			}
			if(right){
				dx = speed;
			}
			if(up){
				dy = -speed;
			}
			if(down){
				dy = speed;
			}
		}
		
		
		x+= dx;
		y += dy;
		
		if(x<0) x=GamePanel.WIDTH;
		if(y<0) y=GamePanel.HEIGHT;
		if(x>GamePanel.WIDTH) x = 0;
		if(y>GamePanel.HEIGHT) y = 0;
		
		dx=0;
		dy=0;
		
		//invincible
		if(invincible){
			long inviElapsed = (System.nanoTime() - inviTimer)/1000000;
			if(inviElapsed > inviDelay){
				inviTimer = System.nanoTime();
				if(useStamina(inviStaminaCost)){
					invincible = true;
				} else {
					invincible = false;
					
				}
			}
		}
		
		//stamina
		long staminaElapsed = (System.nanoTime() - staminaTimer)/1000000;
		if(staminaElapsed > staminaGainDelay){
			staminaTimer = System.nanoTime();
			double futureStamina = currentStamina + staminaRegen;
			if(futureStamina > maxStamina){
				currentStamina = maxStamina;
			} else {
				currentStamina += staminaRegen;
			}
		}
		
		//side missiles
		if(firingSide){
			long elapsedActual = (System.nanoTime() - firingSideActualTimer)/1000000;
			if(elapsedActual > firingSideLength){
				//Longer fire side length every time u fire side activate
				firingSideLength += 100;
				firingSide = false;
				firingSideActualTimer = 0;
			}
			long elapsed = (System.nanoTime()-firingSideTimer)/1000000;
			if(elapsed > firingSideDelay){
				sfx.get("side").play();
				firingSideTimer = System.nanoTime();
				Random rn = new Random();
				int cR = (int) (rn.nextDouble()*100);
				int cG = (int) (rn.nextDouble()*100);
				int cB = (int) (rn.nextDouble()*255);
				GamePanel.bullets.add(new Bullet("left",x-r/2,y+r/2,5,Color.cyan));
				cR = (int) (rn.nextDouble()*100);
				cG = (int) (rn.nextDouble()*255);
				cB = (int) (rn.nextDouble()*255);
				GamePanel.bullets.add(new Bullet("right",x+2*r,y+r/2,5,new Color(cR,cR,cR,255)));
				cR = (int) (rn.nextDouble()*255);
				cG = (int) (rn.nextDouble()*100);
				cB = (int) (rn.nextDouble()*100);
				GamePanel.bullets.add(new Bullet("topLeft",x-r/2,y+r/2,5,new Color(cR,cR,cR,255)));
				cR = (int) (rn.nextDouble()*255);
				cG = (int) (rn.nextDouble()*100);
				cB = (int) (rn.nextDouble()*100);
				GamePanel.bullets.add(new Bullet("topRight",x+2*r,y+r/2,5,new Color(cR,cR,cR,255)));
			}
			
		}
		
		//pushing
		if(isPushing){
			long elapsed = (System.nanoTime() - pushTimer)/1000000;
			if(elapsed > pushLength){
				isPushing = false;
				pushTimer = System.nanoTime();
			}
		}
		
	
		
		//firing
		if(firing){
			long elapsed = (System.nanoTime() - firingTimer) / 1000000;
			if(elapsed > firingDelay){
				sfx.get("laser").play();
				firingTimer = System.nanoTime();
				
				if(powerLevel < 2){
					
					
					
					GamePanel.bullets.add(new Bullet(270, x,y));
				}
				else if(powerLevel < 4){
					GamePanel.bullets.add(new Bullet(270, x+5,y));
					GamePanel.bullets.add(new Bullet(270, x-5,y));
					
				} else if(powerLevel < 6) {
					GamePanel.bullets.add(new Bullet(270, x+0,y));
					GamePanel.bullets.add(new Bullet(265, x-5,y));
					GamePanel.bullets.add(new Bullet(275, x+5,y));
					
				} else if(powerLevel < 8) {
					GamePanel.bullets.add(new Bullet(270, x+0,y));
					GamePanel.bullets.add(new Bullet(265, x-5,y));
					GamePanel.bullets.add(new Bullet(275, x+5,y));
					GamePanel.bullets.add(new Bullet(257, x-8,y));
					GamePanel.bullets.add(new Bullet(283, x+8,y));
					
				} else if(powerLevel < 10){
					GamePanel.bullets.add(new Bullet(270, x+0,y));
					GamePanel.bullets.add(new Bullet(265, x-5,y));
					GamePanel.bullets.add(new Bullet(275, x+5,y));
					GamePanel.bullets.add(new Bullet(257, x-8,y));
					GamePanel.bullets.add(new Bullet(283, x+8,y));
					//
					GamePanel.bullets.add(new Bullet(200, x-10, y));
					GamePanel.bullets.add(new Bullet(340, x+10, y));
					GamePanel.bullets.add(new Bullet(90, x, y));
					
				} else if(powerLevel < 12){
					GamePanel.bullets.add(new Bullet(270, x+0,y));
					GamePanel.bullets.add(new Bullet(265, x-5,y));
					GamePanel.bullets.add(new Bullet(275, x+5,y));
					GamePanel.bullets.add(new Bullet(257, x-8,y));
					GamePanel.bullets.add(new Bullet(283, x+8,y));
					
					GamePanel.bullets.add(new Bullet(200, x-10, y));
					GamePanel.bullets.add(new Bullet(340, x+10, y));
					
					GamePanel.bullets.add(new Bullet(90, x, y));
					GamePanel.bullets.add(new Bullet(80, x+7, y));
					GamePanel.bullets.add(new Bullet(100, x-7, y));
					
				} else {
					GamePanel.bullets.add(new Bullet(270, x+0,y));
					GamePanel.bullets.add(new Bullet(265, x-5,y));
					GamePanel.bullets.add(new Bullet(275, x+5,y));
					GamePanel.bullets.add(new Bullet(257, x-8,y));
					GamePanel.bullets.add(new Bullet(283, x+8,y));
					
					GamePanel.bullets.add(new Bullet(200, x-10, y));
					GamePanel.bullets.add(new Bullet(340, x+10, y));
					
					GamePanel.bullets.add(new Bullet(160, x-10, y));
					GamePanel.bullets.add(new Bullet(20, x+10, y));
					
					GamePanel.bullets.add(new Bullet(90, x, y));
					GamePanel.bullets.add(new Bullet(80, x+7, y));
					GamePanel.bullets.add(new Bullet(100, x-7, y));
					
				}
			}
			
			
		}
		
		
		if(addOn>0 && addOnEnable){
			double radA = Math.toRadians(spinAngle);
			double radB = Math.toRadians(spinAngle2);
			double radC = Math.toRadians(spinAngle3);
			double radD = Math.toRadians(spinAngle4);
			
			//timer
			long elapsed = (System.nanoTime() - addOnShootTimer)/1000000;
			
			if(elapsed>addOnShootDelay){
				addOnShootTimer = System.nanoTime();
				
					GamePanel.bullets.add(new Bullet(addOnShootAngle, (int)(x-r/2+5*r*Math.cos(radA)),(int)(y-r/2+5*r*Math.sin(radA)),4,c1));
				if(addOn>1)
					GamePanel.bullets.add(new Bullet(addOnShootAngle, (int)(x-r/2+5*r*Math.cos(radB)),(int)(y-r/2+5*r*Math.sin(radB)),4,c2));
				if(addOn>2)
					GamePanel.bullets.add(new Bullet(addOnShootAngle, (int)(x-r/2+5*r*Math.cos(radC)),(int)(y-r/2+5*r*Math.sin(radC)),4,c3));
				if(addOn>3)
					GamePanel.bullets.add(new Bullet(addOnShootAngle, (int)(x-r/2+5*r*Math.cos(radD)),(int)(y-r/2+5*r*Math.sin(radD)),4,c4));
				if(addOn>4)
					GamePanel.bullets.add(new Bullet(addOnShootAngle, (int)(x-r/2+10*r*Math.cos(radA)),(int)(y-r/2+10*r*Math.sin(radA)),6,c1));
				if(addOn>5)
					GamePanel.bullets.add(new Bullet(addOnShootAngle, (int)(x-r/2+10*r*Math.cos(radB)),(int)(y-r/2+10*r*Math.sin(radB)),6,c2));
				if(addOn>6)
					GamePanel.bullets.add(new Bullet(addOnShootAngle, (int)(x-r/2+10*r*Math.cos(radC)),(int)(y-r/2+10*r*Math.sin(radC)),6,c3));
				if(addOn>7)
					GamePanel.bullets.add(new Bullet(addOnShootAngle, (int)(x-r/2+10*r*Math.cos(radD)),(int)(y-r/2+10*r*Math.sin(radD)),6,c4));
				
				
				
			}
			addOnShootAngle = Math.random()*360;
			int rotateSpeed = 7;
			if(addOn>4){
				rotateSpeed = 10;
			}
			spinAngle2+=rotateSpeed;
			spinAngle+=rotateSpeed;
			spinAngle3+=rotateSpeed;
			spinAngle4+=rotateSpeed;
			if(spinAngle>360){spinAngle=0;}
			if(spinAngle2>360){spinAngle2=0;}
			if(spinAngle3>360){spinAngle3=0;}
			if(spinAngle4>360){spinAngle4=0;}
			
		}
		
		if(spazing){
			
			long actualElapsed = (System.nanoTime() - spazActualTimer)/1000000;
				if(actualElapsed>spazLength){
					spazing= false;
					spazActualTimer = System.nanoTime();
				}
				
				//for(int i=0; i<47; i++){
					long sElapsed = (System.nanoTime() - spazingTimer)/1000000;
					if(sElapsed>spazingDelay){
						spazingTimer = System.nanoTime();
						
						
						fixedSpazAngle+=12;
						int secondAngle = fixedSpazAngle +180;
						int thirdAngle = fixedSpazAngle + 90;
						int fourthAngle = fixedSpazAngle + 270;
						
						if(thirdAngle>360){
							thirdAngle = fixedSpazAngle - 270;
						}
						if(fourthAngle>360){
							fourthAngle = fixedSpazAngle - 90;
						}
						
						if(secondAngle>360){
							secondAngle = fixedSpazAngle - 180;
						}
						if(fixedSpazAngle>360){
							fixedSpazAngle = 1;
						}
						GamePanel.bullets.add(new Bullet(fixedSpazAngle, x, y));
						GamePanel.bullets.add(new Bullet(secondAngle, x, y));
						GamePanel.bullets.add(new Bullet(thirdAngle, x, y));
						GamePanel.bullets.add(new Bullet(fourthAngle, x, y));
					}
				
				//}
		}
		
		if(bombing){
			long bElapsed = (System.nanoTime()-bombingTimer)/1000000;
			if(bElapsed>bombingLength){
				bombingTimer = 0;
				bombing = false;
			}		
		}
		
	
		
		if(recovering){
			long elapsed = (System.nanoTime() - recoveryTimer)/1000000;
			if(elapsed > 2000){
				recovering = false;
				recoveryTimer = 0;
			}
		}
	}
	
	public void draw(Graphics2D g){
		long rElapsed = (System.nanoTime() - recoveryTimer)/1000000;
		long elapsed = (System.nanoTime() - recoveryBlinkTimer)/1000000;
		
		if(recovering && elapsed >77){
			g.setColor(color2);
			g.fillOval(x-r,y-r,2*r,2*r);
			
			g.setStroke(new BasicStroke(3));
			g.setColor(color2.darker());
			g.drawOval(x-r, y-r, r*2, r*2);
			g.setStroke(new BasicStroke(1));
			recoveryBlinkTimer = System.nanoTime();
			
		} else {		
			g.setColor(color1);
			g.fillOval(x-r,y-r,2*r,2*r);
			
			g.setStroke(new BasicStroke(3));
			g.setColor(color1.darker());
			g.drawOval(x-r, y-r, r*2, r*2);
			g.setStroke(new BasicStroke(1));
		}
		
		if(recovering){
			g.drawRect((int)(x-2*r), (int)(y+2*r), (int)(4*r), (int)(r));
			g.fillRect((int)(x-2*r), (int)(y+2*r), (int)(4*r*((double)rElapsed/2000)), (int)(r));
			String s = "recovering";
			int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (int)(x-length/2), (int)(y-2*r));
		}
		
		if(invincible){
			g.setColor(Color.CYAN);
			g.fillOval((int)(x-r), (int)(y-r), 2*r, 2*r);
		}
		
		if(!isPushing){
			g.setColor(new Color(255,255,255,32));
			g.drawOval((int)(x-pushRadius), (int)(y-pushRadius), (int)(2*pushRadius), (int)(2*pushRadius));
		} else {
			long dElapsed = (System.nanoTime() - pushDrawTimer)/1000000;
			if(dElapsed > pushDrawLength){
				pushDrawTimer = (System.nanoTime());
				
			
			}
			double radius = pushRadius*((double)dElapsed / (double)pushDrawLength);
			
			g.setColor(new Color(255,255,255,128));
			g.drawOval((int)(x-radius), (int)(y-radius), (int)(2*radius), (int)(2*radius));
		}
		
		if(addOn>0 && addOnEnable){		
			double radA = Math.toRadians(spinAngle);
			double radB = Math.toRadians(spinAngle2);
			double radC = Math.toRadians(spinAngle3);
			double radD = Math.toRadians(spinAngle4);
				
			
				g.setColor(Color.RED);
				g.fillOval((int)(x-r/2+5*r*Math.cos(radA)), (int)(y-r/2+5*r*Math.sin(radA)), r, r);
				g.setStroke(new BasicStroke(3));
				g.setColor(Color.RED.darker());
				g.drawOval((int)(x-r/2+5*r*Math.cos(radA)), (int)(y-r/2+5*r*Math.sin(radA)), r, r);
				g.setStroke(new BasicStroke(1));
				
			if(addOn>1){	
				g.setColor(Color.GREEN);
				g.fillOval((int)(x-r/2+5*r*Math.cos(radB)), (int)(y-r/2+5*r*Math.sin(radB)), r, r);
				g.setStroke(new BasicStroke(3));
				g.setColor(Color.GREEN.darker());
				g.drawOval((int)(x-r/2+5*r*Math.cos(radB)), (int)(y-r/2+5*r*Math.sin(radB)), r, r);
				g.setStroke(new BasicStroke(1));
			}
			
			if(addOn>2){
				g.setColor(Color.BLUE);
				g.fillOval((int)(x-r/2+5*r*Math.cos(radC)), (int)(y-r/2+5*r*Math.sin(radC)), r, r);
				g.setStroke(new BasicStroke(3));
				g.setColor(Color.BLUE.darker());
				g.drawOval((int)(x-r/2+5*r*Math.cos(radC)), (int)(y-r/2+5*r*Math.sin(radC)), r, r);
				g.setStroke(new BasicStroke(1));
			}
			
			if(addOn>3){
				g.setColor(Color.YELLOW);
				g.fillOval((int)(x-r/2+5*r*Math.cos(radD)), (int)(y-r/2+5*r*Math.sin(radD)), r, r);
				g.setStroke(new BasicStroke(3));
				g.setColor(Color.YELLOW.darker());
				g.drawOval((int)(x-r/2+5*r*Math.cos(radD)), (int)(y-r/2+5*r*Math.sin(radD)), r, r);
				g.setStroke(new BasicStroke(1));
			}
			
			if(addOn>4){
				g.setColor(Color.GREEN);
				g.fillOval((int)(x-r/2+10*r*Math.cos(radA)), (int)(y-r/2+10*r*Math.sin(radA)), 2*r, 2*r);
				g.setStroke(new BasicStroke(3));
				g.setColor(Color.GREEN.darker());
				g.drawOval((int)(x-r/2+10*r*Math.cos(radA)), (int)(y-r/2+10*r*Math.sin(radA)), 2*r, 2*r);
				g.setStroke(new BasicStroke(1));
			}
			
			if(addOn>5){
				g.setColor(Color.BLUE);
				g.fillOval((int)(x-r/2+10*r*Math.cos(radB)), (int)(y-r/2+10*r*Math.sin(radB)), 2*r, 2*r);
				g.setStroke(new BasicStroke(3));
				g.setColor(Color.BLUE.darker());
				g.drawOval((int)(x-r/2+10*r*Math.cos(radB)), (int)(y-r/2+10*r*Math.sin(radB)), 2*r, 2*r);
				g.setStroke(new BasicStroke(1));
			}
			
			if(addOn>6){
				g.setColor(Color.YELLOW);
				g.fillOval((int)(x-r/2+10*r*Math.cos(radC)), (int)(y-r/2+10*r*Math.sin(radC)), 2*r, 2*r);
				g.setStroke(new BasicStroke(3));
				g.setColor(Color.YELLOW.darker());
				g.drawOval((int)(x-r/2+10*r*Math.cos(radC)), (int)(y-r/2+10*r*Math.sin(radC)), 2*r, 2*r);
				g.setStroke(new BasicStroke(1));
			}
			
			if(addOn>7){
				g.setColor(Color.RED);
				g.fillOval((int)(x-r/2+10*r*Math.cos(radD)), (int)(y-r/2+10*r*Math.sin(radD)), 2*r, 2*r);
				g.setStroke(new BasicStroke(3));
				g.setColor(Color.RED.darker());
				g.drawOval((int)(x-r/2+10*r*Math.cos(radD)), (int)(y-r/2+10*r*Math.sin(radD)), 2*r, 2*r);
				g.setStroke(new BasicStroke(1));
			}
			
		}
	}

	
	
	public int getx() { return x;}
	public int gety() { return y;}
	public int getr() { return r;}
	public int getLives() { return lives;}

	
	public void gainLife() {
		lives++;
		
	}

	public boolean isDead() {
		return lives<=0;
	}
	
}
