package com.ni.crawler.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONUtils {

	public static <K, V> JSONArray mapToJson(Map<K, V> map) {
		
		JSONArray jsonArray = new JSONArray();
		for(Map.Entry<K, V> entry : map.entrySet()) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("term", entry.getKey());
			jsonObject.put("freq", entry.getValue());
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
	
	public static JSONArray readFromFile(String path) throws ParseException, FileNotFoundException, IOException {
		JSONParser parser = new JSONParser();
		JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(path));	
		return jsonArray;
	}
	
	public static Map<String, Double> jsonToMap(JSONArray jsonArray) {
		Map<String, Double> map = new TreeMap<>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject object = (JSONObject)jsonArray.get(i);
			map.put((String)object.get("term"), ((Double)object.get("freq")));
		}
		return map;
		
	}
	
	public static void toFile(String path, JSONArray jsonArray) {
		try (FileWriter file = new FileWriter(path)) {

            file.write(jsonArray.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

	}
}
