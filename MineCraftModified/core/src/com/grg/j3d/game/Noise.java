package com.grg.j3d.game;

public class Noise {

	static double[] adjacent_min(double[] noise) {  // same as before
    double[] output = new double[noise.length];
    for (int i = 0; i< noise.length - 1; i++) {
        output[i] = min(noise[i], noise[i+1]);
    }
    return output;
	}

	private static double min(double d, double e) {
		if (d < e) return e;
		else 
		return d;
	}
}
