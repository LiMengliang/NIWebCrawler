package com.ni.kmean;

import java.util.ArrayList;
import java.util.List;

public class KMeansCluster {

	private KMeansNode center;
	
	private String id;
	
	private List<KMeansNode> nodes;
	
	private Object topTag;
	
	public KMeansCluster(KMeansNode center, String id) {
		
		this.center = center;
		this.nodes = new ArrayList<>();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void addNode(KMeansNode node) {
		nodes.add(node);
	}
	
	public void removeNode(KMeansNode node) {
		nodes.remove(node);
	}

	public KMeansNode getCenter() {
		return center;
	}
	
	public void setCenter(KMeansNode center) {
		this.center = center;
	}
	
	public List<KMeansNode> getNodes() {
		return nodes;
	}
	
	public double getAverageDistance() {
		double distance = 0.0;
		for(KMeansNode node : nodes) {
			distance += KMeans.calculateDistance(node, this);
		}
		return distance / nodes.size();
	}
	
	public double getMaxDistance() {
		double maxDistance = Double.MIN_VALUE;
		for(KMeansNode node : nodes) {
			maxDistance = Double.max(maxDistance, KMeans.calculateDistance(node, this));
		}
		return maxDistance;
	}
	
	public double getX2Distance() {
		double averageDistance = getAverageDistance();
		double x2Distance = 0;
		for(KMeansNode node : nodes) {
			double distance = KMeans.calculateDistance(node, this);
			x2Distance += (distance - averageDistance) * (distance - averageDistance);			
		}
		return x2Distance/nodes.size();
	}
	
//	public Object getTopTag() {
//		int count = 0;
//		for(KMeansNode node : nodes) {
//			if (count < node.getVector()) {
//				
//			}
//		}
//	}
}
