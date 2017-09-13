package com.ni.crawler.utilities;

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
}
