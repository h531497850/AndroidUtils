package android.hqs.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

public class XmlUtil {
	
	private static final String TAG = "XmlUtil";
	
	public static final void decode(String fileName){
		File f=new File("/data/bbkcore/config_daemon.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			NodeList nl = doc.getElementsByTagName("sdcard");
			
			for (int i = 0; i < nl.getLength(); i++) {
				String node = doc.getElementsByTagName("class").item(i).getFirstChild().getNodeValue();
				Log.d(TAG, node);
			}
			
			
			
		} catch (ParserConfigurationException e) {
			Log.e(TAG, "decode config file fail.", e);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
