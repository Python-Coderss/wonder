package com.grg.j3d.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.utils.Disposable;

public enum BlockType implements Disposable {

	stone(Game.t), coal(Game.t2), emerald(Game.t3), 
	diamond(Game.t4), dirt(Game.t5), ruby(Game.t6), 
	grass(Game.t7);

	private Model box;
	private Material mat;
	public final Texture tex;

	BlockType(Texture t) {

		tex = t;
		mat = new Material(TextureAttribute.createDiffuse(t));
		box = Object3d.modelBuilder.createBox(1, 1, 1, mat, VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal |
		Usage.TextureCoordinates);
	}

	public Model getModel() {
		return box;
	}

	@Override
	public void dispose() {
		box.dispose();

	}

}
