package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import entities.Entity;
import entities.Player;
import graficos.Spritesheet;
import graficos.UI;

public class Game extends Canvas implements Runnable,KeyListener{

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 120;
	public static final int HEIGHT = 160;
	public static final int SCALE = 4;
	
	private BufferedImage image;

	public static List<Entity> entities;
	public static Spritesheet spritesheet;
	public static Player player;
	
	public static int score = 0;
	public static double life = 100;
	
	public EnemySpawn enemySpawn;
	
	public BufferedImage GAME_BACKGROUND;
	public BufferedImage GAME_BACKGROUND2;
	
	public int backY = 0;
	public int backY2 = 160;
	public int backSpeed = 1;
	
	public UI ui;
	
	public Game(){
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		spritesheet = new Spritesheet("/spritesheet.png");
		entities = new ArrayList<Entity>();
		player = new Player(WIDTH/2,HEIGHT-16,16,16,2,spritesheet.getSprite(0, 0, 16, 16));
		ui = new UI();
		enemySpawn = new EnemySpawn();
		
		try {
			GAME_BACKGROUND = ImageIO.read(getClass().getResource("/bg_game.png"));
			GAME_BACKGROUND2 = ImageIO.read(getClass().getResource("/bg_game.png"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		entities.add(player);
		
	}
	
	public void initFrame(){
		frame = new JFrame("Space Invaders");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		Game game = new Game();
		game.start();
	}
	
	public void tick(){
		enemySpawn.tick();
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
		
		ui.tick();

		
	}
	


	
	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0,WIDTH,HEIGHT);
		g.drawImage(GAME_BACKGROUND, 0, backY, null);
		g.drawImage(GAME_BACKGROUND2, 0, backY2, null);
		
		/*Renderização do jogo*/
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		ui.tick();
		
		backY-=backSpeed;
		if (backY + 160 <= 0) {
			backY = 160;
		}
		
		backY2-=backSpeed;
		if (backY2 + 160 <= 0) {
			backY2 = 160;
		}
		
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH*SCALE,HEIGHT*SCALE,null);
		ui.render(g);
		bs.show();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning){
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000){
				System.out.println("FPS: "+ frames);
				frames = 0;
				timer+=1000;
			}
			
		}
		
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.right = true;
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.left = true;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.isShooting = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.right = false;
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.left = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	
}
