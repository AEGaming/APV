package com.smb.main.level.tile.spawn;

import com.smb.main.graphics.Screen;
import com.smb.main.graphics.Sprite;
import com.smb.main.level.tile.Tile;

public class SpawnWallTile extends Tile {

	public SpawnWallTile(Sprite sprite) {
		super(sprite);
	}

	public void render(int x, int y, Screen screen) {
		screen.renderTile(x << 4, y << 4, this);
	}
	
	public boolean solid() {
		return true;
	}

}
