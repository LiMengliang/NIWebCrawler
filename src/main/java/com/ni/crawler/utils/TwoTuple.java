package com.ni.crawler.utils;

public class TwoTuple<T, V> {

	private T first;
	private V second;
	
	public T getFirst() {
		return first;
	}
	public void setFirst(T first) {
		this.first = first;
	}
	public V getSecond() {
		return second;
	}
	public void setSecond(V second) {
		this.second = second;
	}
	
	public TwoTuple(T first, V second) {
		this.first = first;
		this.second = second;
	}
	
}
