package com.ni.kmean;

import java.util.Map;

public interface KMeansNode {
	
	String getId();
	Map<?, Double> getVector();
	KMeansCluster getCluster();
	void setCluster(KMeansCluster cluster);

}
