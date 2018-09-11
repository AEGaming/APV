package com.smb.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.smb.main.entity.mob.Zombie;
import com.smb.main.entity.mob.Player;
import com.smb.main.graphics.Screen;
import com.smb.main.input.Keyboard;
import com.smb.main.input.Mouse;
import com.smb.main.level.Level;
import com.smb.main.level.TileCoordinate;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	private static int width = 300;
	private static int height = width / 16 * 9;
	private static int scale = 3;
	private static String title = "Scout Master Bishaw! (Beta v0.01) created by Evan Schenkman";

	private Thread thread;
	private JFrame frame;
	private Keyboard key;
	private Level level;
	private static Player player;
	private static Zombie enemy;
	private boolean running = false;

	private Screen screen;

	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public Game() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		TileCoordinate playerSpawn = new TileCoordinate(20, 67);
		TileCoordinate enemySpawn = new TileCoordinate(19, 20);

		screen = new Screen(width, height);
		frame = new JFrame();
		key = new Keyboard();
		level = Level.spawn;
		player = new Player(playerSpawn.x(), playerSpawn.y(), key);
		enemy = new Zombie(enemySpawn.x(), enemySpawn.y());
		player.init(level);
		enemy.init(level);

		addKeyListener(key);

		Mouse mouse = new Mouse();

		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}

	public static int getWindowWidth() {
		return width * scale;
	}

	public static int getWindowHeight() {
		return height * scale;
	}

	public static Player getPlayer() {
		return player;
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		requestFocus();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(title + "  |  " + frames + " FPS");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.clear();
		int xScroll = player.getX() - screen.width / 2;
		int yScroll = player.getY() - screen.height / 2;
		level.render(xScroll, yScroll, screen);
		player.render(screen);
		enemy.render(screen);

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.setColor(Color.RED);
		g.setFont(new Font("Arial", 0, 20));
		g.drawString(player.stringHealth, 5, 20);
		g.dispose();
		bs.show();
	}

	public void update() {
		key.update();
		player.update();
		enemy.update();
		level.update();
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle(Game.title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setAlwaysOnTop(true);
		game.frame.setVisible(true);

		game.start();
	}
}
