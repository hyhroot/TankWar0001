package com.sxt.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;


/**
 * 坦克类
 * 向8个方向移动
 * 0.7
 * @author HYH
 *
 */
public class Tank {

//	enum Direction {U, D, L, R, UL, UR, DL, DR, STOP};//枚举，储存坦克的8个运动方向
	private Direction dir = Direction.STOP;//坦克运动方向
	private Direction bu_dir = Direction.R;//炮筒的指向
	Blood blood;
	
	int x , y;	//坦克的坐标
	private boolean BU = false, BD= false , BL = false, BR = false;//记录键盘上、下、左、右四个键的状态
	TankWar tw;//引用TankWar，相当于大管家，
	public static final int TANK_V = 5;	//坦克速度
	private static final int WIDHT = 40 , HIGHT = 40;//坦克的高度和宽度
	private boolean good;//坦克的好坏的标志
	private int oldX,oldY;//用于记录上一步坦克的位置
	
	private int life = 100;	//血量
	private BloodBar b = new BloodBar();
	private boolean live = true;//坦克的状态
	private static Random r = new Random();
	private int stop = r.nextInt(15)+5;//记录坦克的移动步数
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] tankImgs = null;
	
	//加载坦克图片
	static {
		tankImgs = new Image[]{
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRD.gif"))
		};
	}
	

	
//	public int getKills() {
//		return kills;
//	}
//
//
//	public void setKills(int kill) {
//		this.kills = kills;
//	}
	
	public int getLife() {
		return life;
	}


	public void setLife(int life) {
		this.life = life;
	}

	public boolean isGood() {
		return good;
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.good = good;
	}

	public Tank(int x, int y, boolean good,Direction dir, TankWar tw) {
		this(x,y,good);
		this.dir = dir;
		this.tw = tw;
	}
	
	
	
	//画自己（坦克）
	public void draw(Graphics g) {
		if(!live) {
			if(!good) {
				tw.enemyTank.remove(this);
			}
			return;
		}
		
		if(good) b.draw(g);
		
		switch (bu_dir) {
		case U:
			g.drawImage(tankImgs[0], x, y, null);
			break;
		case D:
			g.drawImage(tankImgs[1], x, y, null);
			break;
		case L:
			g.drawImage(tankImgs[2], x, y, null);
			break;
		case R:
			g.drawImage(tankImgs[3], x, y, null);
			break;
		case UL:
			g.drawImage(tankImgs[5], x, y, null);
			break;
		case UR:
			g.drawImage(tankImgs[6], x, y, null);
			break;
		case DL:
			g.drawImage(tankImgs[4], x, y, null);
			break;
		case DR:
			g.drawImage(tankImgs[7], x, y, null);
			break;
//		case STOP:
//			break;
		}
		move();
	}	
	
	
		//键盘监听控制--按下监盘
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			switch (key) {
			//上下左右
			case KeyEvent.VK_RIGHT:
				BR = true;
				break;
			case KeyEvent.VK_LEFT:
				BL = true;
				break;
			case KeyEvent.VK_UP:
				BU = true;
				break;
			case KeyEvent.VK_DOWN:
				BD = true;
				break;
			case KeyEvent.VK_F2:
				if(!this.live) {
					this.live = true;
					this.setLife(100);
					tw.kill=0;			
					System.out.println("复活了");
				}
				break;
				
			}
			locateDirection();
		}
		
		//键盘监听控制--松开
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			switch (key) {
			//上下左右
			case KeyEvent.VK_RIGHT:
				BR = false;
				break;
			case KeyEvent.VK_LEFT:
				BL = false;
				break;
			case KeyEvent.VK_UP:
				BU = false;
				break;
			case KeyEvent.VK_DOWN:
				BD = false;
				break;		
				//发射子弹
			case KeyEvent.VK_SPACE:					
				fire();     
				System.out.println("普通攻击");
				break;
			case KeyEvent.VK_A:
				superFire();
				System.out.println("发射超级炮弹");
			}
			locateDirection();
//			bu_dir = dir;
		}
		
		//返回上一步
		public void stey () {
			x = oldX;
			y = oldY;
		}
		
		//坦克移动
		void move() {
			
			this.oldX = x;
			this.oldY = y;
			switch (dir) {
			case U:
				y-=TANK_V;
				break;
			case D:
				y+=TANK_V;
				break;
			case L:
				x-=TANK_V;
				break;
			case R:
				x+=TANK_V;
				break;
			case UL:
				x-=TANK_V;
				y-=TANK_V;
				break;
			case UR:
				x+=TANK_V;
				y-=TANK_V;
				break;
			case DL:
				x-=TANK_V;
				y+=TANK_V;
				break;
			case DR:
				x+=TANK_V;
				y+=TANK_V;
				break;
			case STOP:
				break;
			}
			if(this.dir != Direction.STOP) {//设置炮筒的方向(跟随键盘操作的方向)
				bu_dir = this.dir;
			}
			
			//设置边界。（坦克不能出界）
			if(x<10) x=10;
			if(x>TankWar.WIDHT_SIZE-40) x = TankWar.WIDHT_SIZE-40;
			if(y<40) y = 40;
			if(y>TankWar.HIGH_SIZE-40) y = TankWar.high_size-40 ;
			
			if(!good) {
//				fire();//敌方坦克发射攻击
				Direction[] dirs = Direction.values();					
				if(stop==0) {
					locateDirection();
					stop = r.nextInt(15)+5;
					int rn = r.nextInt(dirs.length);										
					dir = dirs[rn];		
				}
				stop--;
				
				//设置敌方坦克发射攻击的条件（炮弹密度）
				if(r.nextInt(40)>36) this.fire();
			}
		}
		
		//获取键盘指向的方向		
		void locateDirection() {				
			if(BU && !BD && BL && !BR) {dir = Direction.UL;}
			else if(BU && !BD && !BL && BR) {dir = Direction.UR;}
			else if(!BU && BD && BL && !BR) { dir = Direction.DL;}
			else if(!BU && BD && !BL && BR) {dir = Direction.DR;}
			else if(BU && !BD && !BL && !BR) {dir = Direction.U;}
			else if(!BU && BD && !BL && !BR) {dir = Direction.D;}
			else if(!BU && !BD && BL && !BR) {dir = Direction.L;}
			else if(!BU && !BD && !BL && BR) {dir = Direction.R;}
			else if(!BU && !BD && !BL && !BR) {dir = Direction.STOP;}
				
		}	
		//发射子弹
		public Bullet fire() {
			if(!live) return null;
			int x = (Tank.WIDHT/2+this.x-(Bullet.getWidht()/2));
			int y = (Tank.HIGHT/2+this.y-(Bullet.getHight()/2));
			Bullet r_bullet = new Bullet(x, y, good, dir,bu_dir,this.tw);
			tw.bullet.add(r_bullet);
			return r_bullet;
		}
		
		//发射子弹
		public Bullet fire(Direction dirs) {
			if(!live) return null;
			int x = (Tank.WIDHT/2+this.x-(Bullet.getWidht()/2));
			int y = (Tank.HIGHT/2+this.y-(Bullet.getHight()/2));
			Bullet r_bullet = new Bullet(x, y, good, dir,dirs,this.tw);
			tw.bullet.add(r_bullet);
			return r_bullet;
		}
		
		//碰撞检测,得到自己的外切矩形
		public Rectangle getRect() {//碰撞检测辅助类  Rectangle  
			return new Rectangle(x, y, WIDHT, HIGHT); //返回x、y坐标位置的方块
		}
		//坦克与墙的碰撞
		public boolean tank_HitWall(Wall w) {
			if(this.live && this.getRect().intersects(w.getRect())) {
				stey();
				return true;
			}
			return false;
		}
		//坦克碰撞坦克（敌方坦克）
		public boolean tank_HitTank(List<Tank> lt) {
			for(int i=0; i<lt.size(); i++) {
				Tank t = lt.get(i);
				if(this !=t) {
					if(this.live &&t.isLive() && this.getRect().intersects(t.getRect())) {
						this.stey();
						t.stey();
						return true;
						}		
					}				
				}
			return false;
			}
		//超级炮弹
		public void superFire() { 
			Direction[] dirs = Direction.values();
			for(int i=0; i<8; i++) {
				fire(dirs[i]);
				
			}
		}
		//生命值
		private class BloodBar{
			public void draw(Graphics g) {
				Color c = g.getColor();
				g.setColor(Color.RED);
				g.drawRect(x, y-10, WIDHT, 5);
				int l = WIDHT * life/100;
				g.fillRect(x, y-10, l, 5);
				g.setColor(c);
						
			}
		}		
		//吃血量（道具）增加血量 
		public boolean eat(Blood b) {
			if(this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
				this.life = 100;
				b.setLive(false);
				return true;
			}
			return false;
		}
}







