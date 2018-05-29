package com.sxt.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * 墙体类
 * @author HYH
 *
 */
public class Wall {

	int x, y, w, h;
	TankWar tw;
	
	public Wall(int x, int y, int w, int h, TankWar tw) {
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tw = tw;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillRect(x, y, w, h);
		g.setColor(c);
	}
	
	//碰撞检测
	public Rectangle getRect() {//碰撞检测辅助类  Rectangle  
		return new Rectangle(x, y, w, h); //返回x、y坐标位置的方块
	}
	
}
