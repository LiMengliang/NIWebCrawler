package com.ni.test.kmeans;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ni.kmean.KMeans;
import com.ni.kmean.KMeansCluster;
import com.ni.kmean.KMeansNode;

public class KMeansUnitTest {

	@Test
	public void kmeans_seperateNode_clusterCorrectly() {
		
		DummyKMenasNode node1 = new DummyKMenasNode(10, 10);
		DummyKMenasNode node2 = new DummyKMenasNode(12, 13);
		DummyKMenasNode node3 = new DummyKMenasNode(20, 29);
		DummyKMenasNode node4 = new DummyKMenasNode(30, 30);
		DummyKMenasNode node5 = new DummyKMenasNode(100, 100);
		DummyKMenasNode node6 = new DummyKMenasNode(105, 80);
		DummyKMenasNode node7 = new DummyKMenasNode(80, 98);
		DummyKMenasNode node8 = new DummyKMenasNode(98, 102);
		DummyKMenasNode node9 = new DummyKMenasNode(56, 55);
		DummyKMenasNode node10 = new DummyKMenasNode(78, 20);
		DummyKMenasNode node11 = new DummyKMenasNode(100, 80);
		DummyKMenasNode node12 = new DummyKMenasNode(220, 110);
		DummyKMenasNode node13 = new DummyKMenasNode(210, 116);
		DummyKMenasNode node14 = new DummyKMenasNode(70, 80);
		DummyKMenasNode node15 = new DummyKMenasNode(300, 400);
		DummyKMenasNode node16 = new DummyKMenasNode(120, 70);
		DummyKMenasNode node17 = new DummyKMenasNode(340, 320);
		DummyKMenasNode node18 = new DummyKMenasNode(9, 15);
		DummyKMenasNode node19 = new DummyKMenasNode(600, 10);
		DummyKMenasNode node20 = new DummyKMenasNode(550, 30);
		DummyKMenasNode node21= new DummyKMenasNode(76, 43);
		DummyKMenasNode node22 = new DummyKMenasNode(478, 87);
		DummyKMenasNode node23 = new DummyKMenasNode(982, 980);
		DummyKMenasNode node24 = new DummyKMenasNode(564, 32);
		DummyKMenasNode node25 = new DummyKMenasNode(10, 10);
		List<KMeansNode> nodes = new ArrayList<KMeansNode>() {
			{
				add(node1);		
				add(node2);	
				add(node3);	
				add(node4);	
				add(node5);	
				add(node6);	
				add(node7);	
				add(node8);	
				add(node9);		
				add(node10);	
				add(node11);	
				add(node12);	
				add(node13);	
				add(node14);	
				add(node15);	
				add(node16);	
				add(node17);		
				add(node18);	
				add(node19);	
				add(node20);	
				add(node21);	
				add(node22);	
				add(node23);	
				add(node24);	
				add(node25);	
			}
		};
		
		KMeans kmeans = new KMeans(4, nodes);
		List<KMeansCluster> clusters = kmeans.cluster();
	}
	
	static class DummyKMenasNode implements KMeansNode {
		
		private Map<String, Double> vector = new HashMap<>();
		private KMeansCluster cluster;
		
		public DummyKMenasNode(double x, double y) {
			vector.put("A", x);
			vector.put("B", y);
		}

		@Override
		public String getId() {
			// TODO Auto-generated method stub
			return "";
		}

		@Override
		public Map<?, Double> getVector() {
			// TODO Auto-generated method stub
			return vector;
		}

		@Override
		public KMeansCluster getCluster() {
			// TODO Auto-generated method stub
			return cluster;
		}

		@Override
		public void setCluster(KMeansCluster cluster) {
			this.cluster = cluster;
			
		}
		
	}

}
