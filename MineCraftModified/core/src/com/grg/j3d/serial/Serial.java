package com.grg.j3d.serial;

import java.io.Serializable;

import com.grg.j3d.game.Float3;
import com.grg.j3d.game.Object3d;
import com.grg.j3d.player.Inventory;

public class Serial implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9054639280163362625L;
	public final Inventory i;
	public final Object3d[][][] obj3d;
	public final Float3 loc;
	public Serial(Inventory i, Object3d[][][] obj3d, Float3 loca) {
		this.i = i;
		this.obj3d = obj3d;
		loc = new Float3(loca);
	}

}
