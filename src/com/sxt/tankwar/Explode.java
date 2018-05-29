package com.sxt.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.security.auth.login.FailedLoginException;


/**
 * 爆炸类
 * @author HYH
 *
 */

public class Explode {

	int x, y;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	//加载爆炸图片
	private static Image[] imgs = {
			tk.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/9.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/10.gif"))
	};
	int step = 0;
	TankWar tw;
	
	private boolean live = true;
	
	public Explode(int x, int y, TankWar tw) {
		this.x = x;
		this.y = y;
		this.tw = tw;
	}
	
	public void draw(Graphics g) {
		if(!live) {
//			tw.explode.remove(this);
			return;
			
		}
		
		if(step==imgs.length) {
			this.live = false;
			step = 0;
			return;
		}
		//画图（爆炸）
		g.drawImage(imgs[step], x, y, null);
		step++;
		
	}
}
