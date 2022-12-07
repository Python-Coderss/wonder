package com.grg.j3d.player;

import java.io.Serializable;

public class KeyValue<T, V> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3019049409429364642L;
	private T key;
	private V value;
	public KeyValue(T key, V val) {
		// TODO Auto-generated constructor stub
		this.setKey(key);
		setValue(val);
	}
	public V getValue() {
		return value;
	}
	public void setValue(V value) {
		this.value = value;
	}
	public T getKey() {
		return key;
	}
	public void setKey(T key) {
		this.key = key;
	}
	@Override
	public String toString() {
		return key.toString() + " : " + value.toString();
		
	}

}
