package com.grg.j3d.game;

import java.util.Random;

public class BetterRandom extends Random {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BetterRandom() {
		// TODO Auto-generated constructor stub
	}

	public BetterRandom(long seed) {
		super(seed);
		// TODO Auto-generated constructor stub
	}
	public double nextBigDouble() {
				double tval = (((long)next(26) << 27) + next(27)) / (double)(512L << 53);
				double val = tval * 256;
				
			    return val;
			 
	}

}
