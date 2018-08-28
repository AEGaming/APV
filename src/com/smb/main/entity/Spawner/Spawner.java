package com.smb.main.entity.Spawner;

import com.smb.main.entity.Entity;
import com.smb.main.level.Level;

public abstract class Spawner extends Entity{

	public enum Type {
		MOB, PARTICLE;
	}
	
	private Type type;
	
	public Spawner(int x, int y, Type type, int amount, Level level) {
		init(level);
		this.x = x;
		this.y = y;
		this.type = type;

	}
}
