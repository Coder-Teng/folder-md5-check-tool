package com.mine.md5tool.util;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * XML 读写工具：只处理root节点下第一层节点
 *
 */
public class XmlUtil {

	/**
	 * 在root节点下添加节点
	 * 
	 * @param xmlStr
	 * @param nodeName
	 * @param nodeText
	 * @return 新生成的xml 字符串
	 */
	public static String addNode(String xmlStr, String nodeName, String nodeText) {
		String newXmlStr = null;
		try {
			Document doc = DocumentHelper.parseText(xmlStr);
			Element root = doc.getRootElement();
			Element newNode = root.addElement(nodeName);
			newNode.setText(nodeText);
			newXmlStr = root.asXML();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newXmlStr;
	}

	/**
	 * 读取root节点下属性值
	 * 
	 * @param xmlStr
	 * @param nodeName
	 * @return nodeText
	 */
	public static String readNodeText(String xmlStr, String nodeName) {
		String nodeText = null;
		try {
			Document doc = DocumentHelper.parseText(xmlStr);
			Element root = doc.getRootElement();
			@SuppressWarnings("unchecked")
			List<Node> nodeList = root.selectNodes("//" + nodeName);
			if (null != nodeList && !nodeList.isEmpty()) {
				nodeText = nodeList.get(0).getText();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nodeText;
	}

	/**
	 * 更改root节点下节点值
	 * 
	 * @param xmlStr
	 * @param nodeName
	 * @param nodeText
	 * @return 新生成的xml 字符串
	 */
	public static String setNode(String xmlStr, String nodeName, String nodeText) {
		String newXmlStr = null;
		try {
			Document doc = DocumentHelper.parseText(xmlStr);
			Element root = doc.getRootElement();
			Element newNode = root.element(nodeName);
			newNode.setText(nodeText);
			newXmlStr = root.asXML();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newXmlStr;
	}

}
