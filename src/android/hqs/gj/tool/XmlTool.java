package android.hqs.gj.tool;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class XmlTool {
	private static final String TAG = LogTool.makeTag(XmlTool.class);
	
	/**
	 * 解析xml文件
	 * @param fileName 文件路径，如："/data/bbkcore/config_daemon.xml"
	 * @return
	 */
	public static final String decodeFile(String fileName){
		String cycle = null;
		File f = new File(fileName);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			NodeList sdcardList = doc.getElementsByTagName("sdcard");
			if (sdcardList.getLength() > 0) {
				// 获取提示时间间隔
				NodeList timeList = doc.getElementsByTagName("TipTimeInterval");
				if (timeList.getLength() > 0) {
					try {
						cycle = timeList.item(0).getFirstChild().getNodeValue();
						Log.i(TAG, "local set " + cycle + " days to separate the tips.");
					} catch (NumberFormatException e) {
						Log.v(TAG, "", e);
					} catch (DOMException e) {
						Log.v(TAG, "", e);
					}
				}
			}
		} catch (ParserConfigurationException e) {
			Log.d(TAG, "decode config file fail.", e);
		} catch (SAXException e) {
			Log.d(TAG, "decode config file fail.", e);
		} catch (IOException e) {
			Log.d(TAG, "read file fail.", e);
		}
		return cycle;
	}

	/**
	 * 解析xml文件
	 * @param protocolXML xml类型的字符串
	 * @return
	 */
	public static String parseStr(String protocolXML) {
		String cycle = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(protocolXML)));
			NodeList sdcardList = doc.getElementsByTagName("sdcard");
			if (sdcardList.getLength() > 0) {
				// 获取提示时间间隔
				NodeList timeList = doc.getElementsByTagName("TipTimeInterval");
				if (timeList.getLength() > 0) {
					try {
						cycle = timeList.item(0).getFirstChild().getNodeValue();
						Log.i(TAG, "local set " + cycle + " days to separate the tips.");
					} catch (NumberFormatException e) {
						Log.v(TAG, "", e);
					} catch (DOMException e) {
						Log.v(TAG, "", e);
					}
				}
			}
		} catch (ParserConfigurationException e) {
			Log.d(TAG, "decode config file fail.", e);
		} catch (SAXException e) {
			Log.d(TAG, "decode config file fail.", e);
		} catch (IOException e) {
			Log.d(TAG, "read file fail.", e);
		}
		return cycle;
	}
	
}
