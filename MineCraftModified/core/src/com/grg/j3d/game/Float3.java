package com.grg.j3d.game;

import java.io.Serializable;

public class Float3 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7232409127752592494L;
	float p1;
	float p2;
	float p3;

	public Float3(float p1, float p2, float p3) {
		this.p1 = (float)p1;
		this.p2 = (float)p2;
		this.p3 = (float)p3;
	}
	public Float3(Float3 loc) {
		// TODO Auto-generated constructor stub
		set(loc);
	}
	public void set(float p1, float p2, float p3) {
		this.p1 = (float)p1;
		this.p2 = (float)p2;
		this.p3 = (float)p3;
	}
	public void set(Float3 loc) {
		set(loc.p1, loc.p2, loc.p3);
	}

}
