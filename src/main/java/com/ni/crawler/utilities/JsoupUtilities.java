package com.ni.crawler.utilities;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ni.crawler.model.Page;

public class JsoupUtilities {

	public static Elements selectElements(Page page, String query) {
		
		Document doc = Jsoup.parse(page.getPageContent());
		return doc.select(query);
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
	
	public static Elements getElementsByClass(String localPath, String clazz) {
		Document doc = null;
		try {
			doc = Jsoup.parse(new File(localPath), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (doc == null) {
			return null;
		}
		return doc.getElementsByClass(clazz);
	}
}
