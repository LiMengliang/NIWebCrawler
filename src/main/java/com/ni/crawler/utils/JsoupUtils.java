package com.ni.crawler.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.ni.crawler.model.Page;

public class JsoupUtils {

	public static Elements selectElements(Page page, String query) {
		
		Document doc = Jsoup.parse(page.getPageContent());
		return doc.select(query);
	}
	
	public static Elements selectElements(Element element, String query) {
		return element.select(query);
	}
	
	public static String getAttributeValue(Element element, String attributeName) {
		
		return element.attr(attributeName);
	}
	
	public static Elements selectElements(String localPath, String query) {
		Document doc = null;
		try {
			doc = Jsoup.parse(new File(localPath), "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (doc == null) {
			return null;
		}
		return doc.select(query);
	}
	
	public static Elements getElementsByClass(Element element, String clazz) {
		if (element == null) {
			return null;
		}
		return element.getElementsByClass(clazz);
	}
	
	public static Element getElementByClass(Element element, String clazz) {
		return getElementsByClass(element, clazz).first();
	}
	
	public static Element getFirstHitElementByClass(Element element, String[] clazzes) {
		for(String clazz : clazzes) {
			Element result = getElementByClass(element, clazz);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	public static List<Element> getFlatternedComponents(Element element) {
		List<Element> flatternedChildren = new ArrayList<>();
		flatternTreeToList(element, flatternedChildren);
		return flatternedChildren;
	}
		
	private static void flatternTreeToList(Element element, List<Element> flatternedList) {
		List<Element> children = new ArrayList<>();
		for (Node child : element.childNodes()) {
			if (child instanceof Element) {
				children.add((Element)child);
				flatternedList.add((Element)child);
			}
		}
		for (Element child : children) {
			flatternTreeToList(child, flatternedList);
		}
	}
	
	public static Document parseLocalFile(String localPath) {
		Document doc = null;
		try {
			doc = Jsoup.parse(new File(localPath), "UTF-8");
		} catch(IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	public static boolean docContainsString(Document doc, String targetString) {
		return doc.body().text().contains(targetString);
	}
}
