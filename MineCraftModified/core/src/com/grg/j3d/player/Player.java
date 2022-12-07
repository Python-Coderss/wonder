package com.grg.j3d.player;

import com.grg.j3d.game.BlockType;
import com.grg.j3d.game.Game;

public class Player {
	public float x,y,z;
	private Game game;
	public Inventory i;
	public Player(Game g) {
		// TODO Auto-generated constructor stub
		x = g.x;
		y = g.y;
		z = g.z;
		game = g;
		i = new Inventory();
	}
	public void breakBlock(BlockType type) {
		x = game.x;
		y = game.y;
		z = game.z;
		i.add(type);
		
	}
	public boolean placeBlock(BlockType type) {
		x = game.x;
		y = game.y;
		z = game.z;
		return i.remove(type);
		
	}
	public String dump() {
		return i.dump();
	}

}
