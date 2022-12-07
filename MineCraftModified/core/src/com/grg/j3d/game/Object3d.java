package com.grg.j3d.game;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class Object3d implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4753255067376586367L;
	transient ModelInstance c;
	transient Model box;
	final String typename;
	public transient BlockType type;
	static transient ModelBuilder modelBuilder;
	 
	
	public static ModelBuilder init() {
		modelBuilder = new ModelBuilder();
		
		return modelBuilder;
	}
	public static ModelInstance[][][] toModelInstance(Object3d[][][] arr) {
		ModelInstance[][][] result = new ModelInstance[arr.length][][];
		for (int i = 0; i < arr.length; i++) {
			result[i] = new ModelInstance[arr[i].length][];
			for (int o = 0; o < arr[i].length; o++) {
				
				result[i][o] = new ModelInstance[arr[i][o].length];
				
				
			for (int p = 0; p < arr[i][o].length; p++) {
				
				
					
					if (!(arr[i][o][p] == null))
					result[i][o][p] = arr[i][o][p].getInst(new Float3(i, o, p));
					else result[i][o][p] = null;
					
					
				}
			}
		}
		return result;
		
	}
	
//	public Object3d(Float3 color) {
//		mat = new Material(ColorAttribute.createDiffuse(color.p1, color.p2, color.p3, 1));
//		box = modelBuilder.createBox(1, 1, 1, mat,  VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal | Usage.TextureCoordinates);
//		
//	}
	public Object3d(BlockType type) {
		
		box = type.getModel();
		this.type = type;
		typename = type.toString();
		
	}
	public ModelInstance getInst(Float3 loc) {
		if (type == null) {
			type = BlockType.valueOf(typename);
			box = type.getModel();
		}
		
		if (c == null) c = new ModelInstance(box, loc.p1, loc.p2, loc.p3);
		else {
			
		}
		return c;
	}
	public static void dispose() {
		List<BlockType> l = Arrays.asList(BlockType.values());
		for (BlockType block : l) {
			block.dispose();
		}
		
	}
	
	
	

}
