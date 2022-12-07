package com.grg.j3d.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Consumer;

import com.grg.j3d.game.BlockType;

public class Inventory implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = -4121265021799867952L;
	ArrayList<KeyValue<String, Integer>> list;
	 public Inventory() {
		 list = new ArrayList<>();
		 for (BlockType t : BlockType.values()) {
			 list.add(new KeyValue<String, Integer>(t.toString(), 0));
		 }
	 }
	 public void add(final BlockType t) {
		 list.forEach(new Consumer<KeyValue<String, Integer>>() {

			@Override
			public void accept(KeyValue<String, Integer> ti) {
				// TODO Auto-generated method stub
				if (BlockType.valueOf(ti.getKey()) == t) {
					ti.setValue(ti.getValue() + 1);
				}
			}
			 
		 });
		 
	 }
	 private boolean status = false;
	 public synchronized boolean remove(final BlockType t) {
		 status = false;
		 list.forEach(new Consumer<KeyValue<String, Integer>>() {

			@Override
			public void accept(KeyValue<String, Integer> ti) {
				// TODO Auto-generated method stub
				if (BlockType.valueOf(ti.getKey()) == t) {
					int g = ti.getValue();
					status = g > 0;
					System.out.println(g);
					if (status) ti.setValue(g - 1);
				}
			}
			 
		 });
		 return status;
	 }
	 public String dump() {
		 String s = "";
		 for (KeyValue<String, Integer> i : list) {
			 s += i + ", ";
		 }
		 return s;
	 }
	 public int query(BlockType b) {
		 for (KeyValue<String, Integer> i : list) {
			 if (BlockType.valueOf(i.getKey()) == b) return i.getValue();
		 }
		return -1;// Unreachable
	 }
}
