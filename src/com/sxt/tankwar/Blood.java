package com.sxt.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * 道具
 * 加血
 * @author HYH
 *
 */
public class Blood {
	int x, y, w, h;
	TankWar tw;
	int step = 0;
	private boolean live = true;
	
	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isLive() {
		return live;
	}

	private int[][] pos = {
			{300,250},{300,255},{300,260},{300,265},{300,270},
			{305,270},{305,270},{310,270},{315,270},{320,270},{325,270},{330,270},
			{330,265},{330,260},{330,255},{330,250},
			{325,250},{320,250},{315,250},{310,250},{305,250},{300,250}
	};
	
	public Blood() {
		x = pos[0][0];
		y = pos[1][0];
		w = h =8;
	}

	public void draw(Graphics g) {
		if(!live) {
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, w, h);
		
		g.setColor(c);
		move();
	}
	
	private void move() {
		step++;
		if(step == pos.length) {
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}
}
