package com.sxt.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * 版本0.6
 * 子弹类
 * @author HYH
 *
 */
public class Bullet {

	private static final int WIDHT = 10 , HIGHT = 10;//子弹大小
	private static final int BULLET_V = 10;		//子弹的速度
	private boolean live = true;//子弹是否出界或发生撞击
	private boolean good;

	int x,y;
	Direction dir;
	Direction bu_dir;
	TankWar tw;
	Tank wk;
	
	
	
	public static int getHight() {
		return HIGHT;
	}

	public static int getWidht() {
		return WIDHT;
	}



	public Bullet(int x, int y,Direction dir, Direction bu_dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.bu_dir = bu_dir;	
	}
	
	public Bullet(int x, int y,boolean good,Direction dir, Direction bu_dir,TankWar tw) {
		this(x,y,dir,bu_dir);
		this.good = good;
		this.tw = tw;

		
	}

	public void draw(Graphics g) {
		if(!live) {
			tw.bullet.remove(this);
			return;
		}
		Color c = g.getColor();
		//不同阵营的坦克的炮弹颜色不同
		if(this.good) {
			g.setColor(Color.RED);
		}
		g.fillOval(x, y, WIDHT, HIGHT);
		g.setColor(c);
		move();
	}

//	根据坦克的移动方向选择发射炮弹的方向
	private void move() {
		switch (bu_dir) {
		case U:
			y-=BULLET_V;
			break;
		case D:
			y+=BULLET_V;
			break;
		case L:
			x-=BULLET_V;
			break;
		case R:
			x+=BULLET_V;
			break;
		case UL:
			x-=BULLET_V;
			y-=BULLET_V;
			break;
		case UR:
			x+=BULLET_V;
			y-=BULLET_V;
			break;
		case DL:
			x-=BULLET_V;
			y+=BULLET_V;
			break;
		case DR:
			x+=BULLET_V;
			y+=BULLET_V;
			break;
		}
		//当炮弹跑出窗口边界后就清除
		if(x<0||y<0||x>=TankWar.WIDHT_SIZE||y>=TankWar.HIGH_SIZE) {
			live = false;
		}
	}	
	
	public boolean isLive() {
		return live;
	}

	//碰撞检测方法
	public Rectangle getRect() {//碰撞检测辅助类  Rectangle  
		return new Rectangle(x, y, WIDHT, HIGHT); //返回x、y所在的方块
	}
	
	//判断单个子弹与坦克是否发生碰撞
	public boolean bullet_hitTank(Tank t) {
		
		//判断子弹与坦克是否发生碰撞、坦克是否还存在（live）
		if(this.live && this.getRect().intersects(t.getRect())&&t.isLive()&&this.good != t.isGood()) {
			if(t.isGood()) {
				t.setLife(t.getLife()-20);
				if(t.getLife()<=0) t.setLive(false);
			}else {
				t.setLive(false);			
			}
			this.live = false;
			Explode e = new Explode(x, y, tw);
			tw.explode.add(e);
			return true;
		}
			return false;
		}
	
	//多个子弹
	public boolean bullet_hitTanks(List<Tank> enemyTanks) {
		for(int i=0;i<enemyTanks.size();i++) {
			if(bullet_hitTank(enemyTanks.get(i)) ) {
				return true;				
			}
			
		}
		return false;
	}
	
	//判断炮弹与墙是否相撞
	public boolean bullet_HitWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			return true;
		}
		return false;
	}
}
