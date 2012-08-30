package com.siteview.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EventXmlParse {
	private Document document = null;

	public EventXmlParse(String xmlString) throws Exception {
		this.setDocument(readXml(xmlString));
		getDocument().getDocumentElement().normalize();
	}
	public EventXmlParse(InputStream is) throws Exception {
		this.setDocument(readXml(is));
		getDocument().getDocumentElement().normalize();
	}


	public Map<String, String> getMap() {
		Map<String, String> retmap = new HashMap<String, String>();
		retmap.putAll(this.getMap("s12:Header"));
		retmap.putAll(this.getMap("dcm:Event"));
		return retmap;

	}
	

	private Map<String, String> getMap(String tagName){
		Map<String, String> retmap = new HashMap<String, String>();
		NodeList nodeLst = getDocument().getElementsByTagName(tagName);
		for (int s = 0; s < nodeLst.getLength(); s++) {
			Node fstNode = nodeLst.item(s);
			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
				NodeList fstNmElmntLst = fstNode.getChildNodes();
				for (int i = 0; i < fstNmElmntLst.getLength(); i++) {
					Node fNode = fstNmElmntLst.item(i);
					if (fNode.getNodeType() == Node.ELEMENT_NODE) {
						retmap.put(fNode.getNodeName(), fNode.getTextContent());
					}
				}
			}
		}
		return retmap;
		
	}
	
	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public static String getContents(File aFile) {
		// ...checks on aFile are elided
		StringBuilder contents = new StringBuilder();

		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			BufferedReader input = new BufferedReader(new FileReader(aFile));
			try {
				String line = null; // not declared within while loop
				/*
				 * readLine is a bit quirky : it returns the content of a line
				 * MINUS the newline. it returns null only for the END of the
				 * stream. it returns an empty String if two newlines appear in
				 * a row.
				 */
				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return contents.toString();
	}

	private static Document readXml(String contect) throws Exception {
		return readXml(new ByteArrayInputStream(contect.getBytes()));
	}
	private static Document readXml(InputStream is) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		return db.parse(is);
	}
	public static void main(String[] args) throws Exception {
		String contect = getContents(new File("D:\\eclipseJEE_Zk\\event2.xml"));
		System.out.println(contect);
		EventXmlParse xml = new EventXmlParse(contect);
		System.out.println(xml.getMap());
	}

}
