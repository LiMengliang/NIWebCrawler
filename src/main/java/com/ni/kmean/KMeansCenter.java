package com.ni.kmean;

import java.util.HashMap;
import java.util.Map;

public class KMeansCenter implements KMeansNode {
	
	private Map<Object, Double> vector;
	
	private KMeansCluster cluster;
	
	
	public KMeansCenter() {
		this.vector = new HashMap<>();
	}
	
	public void updateVector(Object key, double value) {
		vector.put(key, value);
	}

	@Override
	public String getId() {
		return "center";
	}

	@Override
	public Map<?, Double> getVector() {
		return vector;
	}
	
	@Override
	public KMeansCluster getCluster() {
		return cluster;
	}
	
	public void setCluster(KMeansCluster cluster) {
		this.cluster = cluster;
	}
	

}
