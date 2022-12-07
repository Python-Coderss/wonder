package com.grg.j3d.game;

public class IntTests {
    static BetterRandom r = new BetterRandom(2022);
	static boolean isRandom(int i, int o, int p) {
		return isRandom(p * (i*o));
		
	}
	static int isRandomint(int i, int o, int p) {
		return isRandomint(p * (i*o));
		
	}
	static boolean isRandom(int val) {
		if (val == 0) return true;
		return (getr(val)) == val;
		
	}
	
	static int getr( int val) {
		if (val == 0) return 0;
		int ar = 1000;
		int temp;
		long tempsum = 0;
		int res = 0;
		for (int i = 0; i < ar; i++) {
			temp = r.nextInt() % val * 2;
			tempsum += temp;
		}
		res = (int) (tempsum / ar);
		return res;
	}
	static int isRandomint(int val) {
		if (val == 0) return 0;
		return (getr(val)) - val;
		
	}

}
