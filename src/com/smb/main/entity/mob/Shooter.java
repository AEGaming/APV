package com.smb.main.entity.mob;

import java.util.List;

import com.smb.main.entity.Entity;
import com.smb.main.graphics.AnimatedSprite;
import com.smb.main.graphics.Screen;
import com.smb.main.graphics.Sprite;
import com.smb.main.graphics.SpriteSheet;
import com.smb.main.level.Node;
import com.smb.main.util.Debug;
import com.smb.main.util.Vector2i;

public class Shooter extends Mob {

	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.dummy_down, 32, 32, 3);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.dummy_up, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.dummy_left, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.dummy_right, 32, 32, 3);

	private AnimatedSprite animSprite = down;

	private int time = 0;
	private int xa = 0, ya = 0;
	private List<Node> path;
	private int fireRate;

	private Entity rand = null;

	public Shooter(int x, int y) {
		this.x = x << 4;
		this.y = y << 4;
		sprite = Sprite.dummy;
	}

	private void move() {
		xa = 0;
		ya = 0;
		int px = level.getPlayerAt(0).getX();
		int py = level.getPlayerAt(0).getY();
		Vector2i start = new Vector2i(getX() >> 4, getY() >> 4);
		Vector2i destination = new Vector2i(px >> 4, py >> 4);
		if (time % 3 == 0)
			path = level.findPath(start, destination);
		if (path != null) {
			if (path.size() > 0) {
				Vector2i vec = path.get(path.size() - 1).tile;
				if (x < vec.getX() << 4)
					xa++;
				if (x > vec.getX() << 4)
					xa--;
				if (y < vec.getY() << 4)
					ya++;
				if (y > vec.getY() << 4)
					ya--;
			}
		}

		if (xa != 0 || ya != 0) {
			move(xa, ya);
			walking = true;
		} else {
			walking = false;
		}
	}

	public void update() {
		time++;
		move();
		if (walking)
			animSprite.update();
		else
			animSprite.setFrame(0);
		if (ya < 0) {
			dir = Direction.UP;
			animSprite = up;
		} else if (ya > 0) {
			dir = Direction.DOWN;
			animSprite = down;
		}
		if (xa < 0) {
			dir = Direction.LEFT;
			animSprite = left;
		} else if (xa > 0) {
			dir = Direction.RIGHT;
			animSprite = right;
		}
		if (xa != 0 || ya != 0) {
			walking = true;
		} else {
			walking = false;
		}
		shootClosest();
	}
	
	/*
	 * private void shootRandom() { if (time % 60 == 0) { List<Entity> entities =
	 * level.getEntities(this, 500); entities.add(level.getClientPlayer()); if
	 * (entities.size() > 0) { int index = random.nextInt(entities.size()); rand =
	 * entities.get(index); } } if (rand != null) { double dx = rand.getX() - x;
	 * double dy = rand.getY() - y; double dir = Math.atan2(dy, dx); shoot(x, y,
	 * dir); } }
	 */

	private void shootClosest() {
		List<Entity> entities = level.getEntities(this, 50);
		entities.add(level.getClientPlayer());

		double min = 0;
		Entity closest = null;
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			double distance = Vector2i.getDistance(new Vector2i(x, y), new Vector2i(e.getX(), e.getY()));
			if (i == 0 || distance < min) {
				min = distance;
				closest = e;
			}
		}

		if (closest != null) {
			double dx = closest.getX() - x;
			double dy = closest.getY() - y;
			double dir = Math.atan2(dy, dx);
			shoot(x, y, dir, fireRate);
		}
	}

	public void render(Screen screen) {
		Debug.drawRect(screen, 17 * 16, 57 * 16, 40, 40, true);
		sprite = animSprite.getSprite();
		screen.renderMob(x - 16, y - 16, this);
	}
}