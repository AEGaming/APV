package com.smb.main.level.tile;

import com.smb.main.graphics.Screen;
import com.smb.main.graphics.Sprite;
import com.smb.main.level.tile.spawn_level.SpawnFloorTile;
import com.smb.main.level.tile.spawn_level.SpawnGrassTile;
import com.smb.main.level.tile.spawn_level.SpawnWallTile;

public class Tile {
	
	public Sprite sprite;
	
	public static Tile voidTile = new VoidTile(Sprite.voidSprite);
	
	public static Tile spawn_grass = new SpawnGrassTile(Sprite.spawn_grass);
	public static Tile spawn_wall1 = new SpawnWallTile(Sprite.spawn_wall1);
	public static Tile spawn_wall2 = new SpawnWallTile(Sprite.spawn_wall2);
	public static Tile spawn_floor = new SpawnFloorTile(Sprite.spawn_floor);
	
	public final static int col_spawn_grass = 0xFF00FF00;
	public final static int col_spawn_wall1 = 0xFF808080;
	public final static int col_spawn_wall2 = 0xFF000000;
	public final static int col_spawn_floor = 0xFFFF8019;
	
	public Tile(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public void render(int x, int y, Screen screen) {
	}
	
	public boolean solid() {
		return false;
	}	
}