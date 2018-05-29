package com.sxt.tankwar;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.renderable.RenderableImage;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sxt.tankwar.Direction;

/**
 * JDK版本：1.6
 * 
 * 功能：
 * 版本0.3
 * 画出简单的模型并实现运动
 * 双缓冲消除闪烁现象
 * 版本0.4
 * 实现键盘控制;键盘监听
 * 版本0.5
 * 实现敌方坦克
 * 版本0.6
 * 0.7
 * 0.9
 * 克能显示方向，炮弹沿着坦克的方法发射
 * 1.0
 * 炮弹连续发射
 * 1.2
 * 出界问题
 * 1.3
 * 增加敌人
 * 1.4
 * 判断子弹打坦克
 *1.5 
 * 爆炸效果实现
 * 1.6
 * 多个敌人
 * 1.7
 * 让敌方自己动
 * 1.8
 * 让敌方自动随机移动
 * 1.9
 * 敌方自动随机移动并能发出攻击
 * 2.0
 * 添加障碍物（墙）
 * 2.1
 * 坦克与坦克碰撞
 * 2.2
 * 给主站坦克增加生命值
 * 2.3
 * 超级炮弹
 * 2.4
 * 从新开始
 * @author HYH
 *
 */
public class TankWar extends Frame{
	
	public static int sum = 10;
	public static int ENEMY_SUM = 5;//敌方坦克的数量
	public static final int WIDHT_SIZE = 800 , HIGH_SIZE = 600;//窗口长、宽
	public static final int width_size = WIDHT_SIZE , high_size = HIGH_SIZE;
	public int kill;//杀敌数
	
	
	Tank myTank = new Tank(30,300,true,Direction.STOP, this);//己方坦克对象
	
	//创建障碍物
	Wall w1 = new Wall(50, 150, 20, 300, this), w2 = new Wall(200, 500, 400, 20, this);
	Wall /*w3 = new Wall(200, 120, 400, 20, this),*/ w4 = new Wall(700, 150, 20, 300, this);

	
	Random random = new Random(100);
	
	List<Tank> enemyTank = new ArrayList<Tank>();//用List数组存放敌方坦克对象
	List<Explode> explode = new ArrayList<Explode>();//用List数组存放爆炸对象
	List<Bullet> bullet  = new ArrayList<Bullet>();	//用List数组存放子弹对象
	
	Blood blood = new Blood();//道具：加血
	
	Image offScreenImage = null;
	

	//重写update方法	利用双缓冲解决闪烁问题
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(WIDHT_SIZE,HIGH_SIZE);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = g.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, WIDHT_SIZE, HIGH_SIZE);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	//重写paint方法，加载游戏内容（画坦克）
	public void paint(Graphics g) {
//		g.drawString("发射子弹："+bullet.size(), 700, 50);
		for(int i=0;i<bullet.size();i++) {
			Bullet b = bullet.get(i);		
			b.bullet_hitTanks(enemyTank);
			b.bullet_hitTank(myTank);
			b.bullet_HitWall(w1);
			b.bullet_HitWall(w2);
//			b.bullet_HitWall(w3);
			b.bullet_HitWall(w4);
			
			b.draw(g);
		}
		
		g.drawString("剩余敌人："+enemyTank.size(), 700, 50);
		g.drawString("击毁坦克："+(kill+-enemyTank.size()), 700, 70);
		if(kill+-enemyTank.size()==20) {
			g.setFont(new Font("宋体",Font.BOLD,35));
			g.drawString("恭喜过关：", 300, 270);
			g.drawString("共杀敌："+(kill+-enemyTank.size()), 300, 320);
			myTank.setLive(false);
			return;
		}
//		System.out.println(myTank.getKills());
		g.drawString("血量："+myTank.getLife(), 700, 90);
		if(enemyTank.size()<=0) {
			for(int i=0; i<5;i++) {
				enemyTank.add(new Tank(50+(60)*i+1, 80, false, Direction.D, this));	
			}	
			kill=kill+ENEMY_SUM;
		}
		
		for(int i=0;i<explode.size();i++){
			Explode e = explode.get(i);
			e.draw(g);
		}
		
		for(int i=0;i<enemyTank.size();i++) {
			Tank t = enemyTank.get(i);
			t.draw(g);
			t.tank_HitWall(w1);
			t.tank_HitWall(w2);
//			t.tank_HitWall(w3);
			t.tank_HitWall(w4);
			t.tank_HitTank(enemyTank);
//			t.tank_HitTank(myTank);
		}
		if(!myTank.isLive()) {
			g.setFont(new Font("宋体",Font.BOLD,35));
			g.drawString("结束,请按F2复活", 300, 270);
			g.drawString("本局得分："+(kill+-enemyTank.size()), 300, 320);
			return ;
		}
		myTank.draw(g);
		myTank.eat(blood);
		w1.draw(g);
		w2.draw(g);
//		w3.draw(g);
		w4.draw(g);
		blood.draw(g);
	}
	
	//添加敌方坦克
	public void addEnemy() {
		if((kill+(ENEMY_SUM-enemyTank.size()))<20) {
			for(int i=0;i<ENEMY_SUM;i++) {
				enemyTank.add(new Tank(50+(60)*i+1, 80, false, Direction.D, this));
			}
		}else {
			for(int i=0;i<10;i++) {
				enemyTank.add(new Tank(50+(60)*i+1, 80, false, Direction.D, this));
			}
		}
		
	}
	//加载窗口
	@Test
	public void win() {
		
		
			
		this.setLocation(200, 100);
		this.setSize(width_size, high_size);
//		this.setResizable(false); 	//大小不可变
		this.setTitle("坦克大战          班级：软工java升          姓名：何永辉          学号：1715924013");	
		//关闭窗口
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {	
				System.exit(0);
			}
		});
		
		this.addKeyListener(new KeyMonitor());
		this.setBackground(Color.GREEN);		
		this.setVisible(true);		
		
		//启动线程
		new Thread(new PaintThread()).start();
		
	}	
		
	//窗口重画内部类
	private class PaintThread implements Runnable{
		public void run() {
			while(true) {
				repaint();
				try {
//					System.out.println("画出窗口"+y);
					Thread.sleep(40);	//休眠？毫秒
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	//键盘监听内部类
	private  class KeyMonitor extends KeyAdapter{
		
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);	
//			System.out.println("按下了监盘");
		}
		
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
	}

	public static void main(String[] args) {
		TankWar tkw = new TankWar();
		tkw.win();
	}
	
	
}





