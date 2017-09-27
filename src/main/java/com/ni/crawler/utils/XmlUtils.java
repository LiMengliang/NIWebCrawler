package com.ni.crawler.utils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XmlUtils {

	public static Document createDocument() {
		return DocumentHelper.createDocument();		
	}
	
	public static Element addToElement(Element parent, Element child) {
		parent.add(child);
		return parent;
	}
	
	public static Element createElement(String name) {
		return DocumentHelper.createElement(name);	
	}
	
	public static Element createElement(String name, String value, TwoTuple<QName, String>...attrAndValues) {
		Element element = DocumentHelper.createElement(name);
		for(TwoTuple<QName, String> attrAndValue : attrAndValues) {
			element.addAttribute(attrAndValue.getFirst(), attrAndValue.getSecond());
		}
		element.addText(value);
		return element;
	}
	
	public static Element createElement(String name, Map<String, String> attrAndValues) {
		Element element = DocumentHelper.createElement(name);
		for(Map.Entry<String, String> attrAndValue : attrAndValues.entrySet()) {
			element.addAttribute(attrAndValue.getKey(), attrAndValue.getValue());
		}
		return element;		
	}
	
	public static void save(Document document, String path) throws IOException {
		OutputFormat format = OutputFormat.createPrettyPrint();
		FileWriter out =  new FileWriter(path);

		XMLWriter writer = new XMLWriter(out, format);
		writer.write(document);
		out.close();		
	}
}
