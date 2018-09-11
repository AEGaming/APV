package com.smb.main.entity.projectile;

import com.smb.main.graphics.Screen;
import com.smb.main.graphics.Sprite;

public class EnemyProjectile extends Projectile {

	public static final int FIRE_RATE = 10;
	
	public EnemyProjectile(int x, int y, double dir) {
		super(x, y, dir);
		range = random.nextInt(10) + 90;
		speed = 2;
		damage = 20;
		sprite = Sprite.enemy_projectile;
		nx = speed * Math.cos(angle);
		ny = speed * Math.sin(angle);
	}
	
	public void update() {
			if (level.tileCollision(x, y, nx, ny, 7)) remove(); 
			move();
	}
	
	protected void move() {
		x += nx;
		y += ny;
		if (distance() > range) remove();
	}
	
	private double distance() {
		double dist = 0;
		dist = Math.sqrt(Math.abs((xOrigin - x) * (xOrigin - x) + (yOrigin - y) * (yOrigin - y)));
		return dist;
	}

	public void render(Screen screen) {
		screen.renderProjectile((int) x - 12, (int) y - 2, this);
	}

}
