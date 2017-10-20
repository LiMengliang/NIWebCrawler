package com.ni.crawler.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ni.crawler.utils.JSONUtils;
import com.ni.kmean.KMeansCluster;
import com.ni.kmean.KMeansNode;

public class ArticleTfIdf implements KMeansNode {

	private String url;
	private String title;
	private Map<String, Double> tfidf;
	private KMeansCluster cluster;
	
	public ArticleTfIdf(String url, String title) {
		this.url = url;
		this.title = title;
		this.tfidf = new TreeMap<>();
	}
	
	public void updateTfIdf(String term, double tfidf) {
		
		this.tfidf.put(term, tfidf);
	}
	
	public String getUrl() {
		return url;
	}
	public String getTitle() {
		return title;
	}
	public Map<String, Double> getTfidf() {
		return tfidf;
	}
	
	public double getOrDefaultTfidf(String term, double defaultTfIdf) {
		return tfidf.getOrDefault(term, defaultTfIdf);
	}
	
	public JSONObject tfidfToJSON() {
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("url", getUrl());
		jsonObject.put("title", getTitle());
		JSONArray tfidfArray = mapToJson(getTfidf());
		jsonObject.put("tfidfs", tfidfArray);
		
		return jsonObject;		
	}
	
	private JSONArray mapToJson(Map<String, Double> tfidf) {
		
		JSONArray jsonArray = new JSONArray();
		for(Map.Entry<String, Double> entry : tfidf.entrySet()) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("term", entry.getKey());
			jsonObject.put("freq", entry.getValue());
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
	
	
	public static ArticleTfIdf getFromJsonFile(String path) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(path));
		String url = (String)jsonObject.get("url");
		String title = (String)jsonObject.get("title");
		JSONArray tfidfs = (JSONArray)jsonObject.get("tfidfs");
		
		ArticleTfIdf articleTfIdf = new ArticleTfIdf(url, title);
		
		for(int i = 0; i < tfidfs.size(); i++) {
			JSONObject tfidf = (JSONObject)tfidfs.get(i);
			String term = (String)tfidf.get("term");
			Double tfidfValue = (Double)tfidf.get("freq");
			articleTfIdf.updateTfIdf(term, tfidfValue);
		}
		return articleTfIdf;
	}

	@Override
	public String getId() {

		return url;
	}

	@Override
	public Map<String, Double> getVector() {

		return tfidf;
	}

	@Override
	public KMeansCluster getCluster() {
		return cluster;
	}
	@Override
	public void setCluster(KMeansCluster cluster) {
		this.cluster = cluster;
	}
	
}
