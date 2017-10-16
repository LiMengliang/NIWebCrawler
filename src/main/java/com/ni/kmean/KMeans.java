package com.ni.kmean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ni.crawler.utils.Log;

public class KMeans {
	
	private List<KMeansCluster> clusters;
	private List<? extends KMeansNode> allNodes;

	private Map<KMeansCluster, Double> centerTfidfSquireSum = new HashMap<>();
		
	public KMeans(int n, List<? extends KMeansNode> nodes) {
		
		this.allNodes = nodes;
		this.clusters = new ArrayList<>(n);
		for(int i = 0; i < n; i++) {
			int random = (int) (Math.random() * nodes.size() + 0.5)%nodes.size();
			Log.consoleWriteLine("select " + random + " as seed");
			KMeansCluster cluster = new KMeansCluster(nodes.get(random), Integer.toString(i));
			clusters.add(cluster);
		}
		updateCenterSquareSum();
	}
	
	public List<KMeansCluster> cluster() {
		boolean changed = true;
		int iterations = 0;
		while(changed) {
			iterations++;
			Log.consoleWriteLine("Iteration " + iterations);
			changed = clusterOneIteration();
			if (changed) {
				regenerateClustersCenter();
			}
		}
		return clusters;
	}

	public static double calculateDistance(KMeansNode node, KMeansCluster cluster) {
		
		double distance = 0.0;
		KMeansNode center = cluster.getCenter();
		
		for(Map.Entry<?, Double> entry : node.getVector().entrySet()) {
			
			Object firstKey = entry.getKey();
			double nodeValue = entry.getValue();
			double centerValue = center.getVector().getOrDefault(firstKey, 0.0);
			distance += (centerValue - nodeValue) * (centerValue - nodeValue);
		}
		
		for (Map.Entry<?, Double> entry : center.getVector().entrySet()) {
			Object centerKey = entry.getKey();
			double centerValue = entry.getValue();
			if (!node.getVector().containsKey(centerKey)) {
				distance += centerValue * centerValue;
			}
		}
		return Math.sqrt(distance);
		
//		double nodeTfidfSqureSum = 0;
//		double centerTfidfSqureSum = centerTfidfSquireSum.get(cluster);
//		for(Entry<?, Double> entry : node.getVector().entrySet()) {
//			nodeTfidfSqureSum += entry.getValue() * entry.getValue();
//		}
//		nodeTfidfSqureSum = Math.sqrt(nodeTfidfSqureSum);
//		double crossMultiplySum = 0;
//		for(Map.Entry<?, Double> entry : node.getVector().entrySet()) {
//			
//			Object nodeKey = entry.getKey();
//			double nodeValue = entry.getValue();
//			double centerValue = center.getVector().getOrDefault(nodeKey, 0.0);
//			crossMultiplySum += nodeValue * centerValue;
//		}
//		return 1 - crossMultiplySum/(nodeTfidfSqureSum*centerTfidfSqureSum);
	}
	
	private boolean clustOneNode(KMeansNode node) {		
		
		boolean change = false;
		double minDistance = Double.MAX_VALUE;
		KMeansCluster bestCluster = null;
		for(KMeansCluster cluster : clusters) {
			double distance = calculateDistance(node, cluster);
			if (minDistance > distance) {
				minDistance = distance;
				bestCluster = cluster;
			}
		}
		KMeansCluster oldCluster = node.getCluster();
		if (oldCluster != bestCluster) {
			node.setCluster(bestCluster);
			bestCluster.addNode(node);
			if (oldCluster != null) {
				oldCluster.removeNode(node);
			}			
			change = true;
		}
		return change;
	}
	
	private boolean clusterOneIteration() {
		boolean anyChange = false;
		int i = 0;
		for(KMeansNode node : allNodes) {
			i++;
			 boolean change = clustOneNode(node);
			 if (!anyChange) {
				 anyChange = change;
			 }
		}
		return anyChange;
	}
	
	private KMeansNode recalculateCenter(KMeansCluster cluster) {		
		KMeansCenter newCenter = new KMeansCenter();
		for (KMeansNode node : cluster.getNodes()) {
			for (Map.Entry<?, Double> entry : node.getVector().entrySet()) {
				Object key = entry.getKey();
				double value = entry.getValue();
				double newCenterValue = newCenter.getVector().getOrDefault(key, 0.0) + value;
				newCenter.updateVector(key, newCenterValue);
			}
		}
		
		int nodeSize = cluster.getNodes().size();
		for (Map.Entry<?, Double> entry : newCenter.getVector().entrySet()) {
			newCenter.updateVector(entry.getKey(), entry.getValue()/nodeSize);
		}
		return newCenter;
	}
	
	private void regenerateClustersCenter() {
		for(KMeansCluster cluster : clusters) {
			KMeansNode center = recalculateCenter(cluster);
			cluster.setCenter(center);
			
		}		
		updateCenterSquareSum();
	}

	private void updateCenterSquareSum() {
		// update center square sum
		for(KMeansCluster cluster : clusters) {
			KMeansNode center = cluster.getCenter();
			double squareSum = 0;
			for(Entry<?, Double> entry : center.getVector().entrySet()) {
				squareSum += entry.getValue() * entry.getValue();
			}
			squareSum = Math.sqrt(squareSum);
			centerTfidfSquireSum.put(cluster, squareSum);
			
		}
	}
	
	public List<KMeansCluster> getClusters() {
		return clusters;
	}
}
