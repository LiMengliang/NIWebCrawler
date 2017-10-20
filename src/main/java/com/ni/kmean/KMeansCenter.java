package com.ni.kmean;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class KMeansCenter implements KMeansNode {
	
	private Map<Object, Double> vector;
	
	private KMeansCluster cluster;
	
	
	public KMeansCenter() {
		this.vector = new HashMap<>();
	}
	
	public void updateVector(Object key, double value) {
		vector.put(key, value);
	}
	
	
	public List<Object> getTopKeywords(int topN) {
		Collection<Double> tfidfValues =  vector.values();
		TreeSet<Double> treeSet = new TreeSet<>();
		for(Double value : tfidfValues) {
			treeSet.add(value);
		}
		List<Object> result = new ArrayList<>();
		try {
			
		double max = Double.MAX_VALUE;
		for(int i = 0; i < topN; i++) {
			if (treeSet.size() > i + 1) {				
				double value = treeSet.lower(max);
				max = value;
				for(Map.Entry<Object, Double> entry : vector.entrySet()) {
					if(entry.getValue().equals(value)) {
						result.add(entry.getKey());
					}
				}
			}
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
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
