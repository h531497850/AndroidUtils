package android.hqs.net;
import java.io.Serializable;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 响应对象
 */
public class HttpResponser implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String urlStr;
 
	private int defaultPort;
 
	private String fileName;
 
	private String path;
	
	private String host;
 
	private int port;
 
	private String protocol;
 
	private String query;
 
	private String ref;
 
	private String userInfo;
 
	private String contentEncoding;
 
	private String content;
 
	private String contentType;
 
	private int code;
 
	private String msg;
 
	private String method;
 
	private int connectTimeout;
 
	private int readTimeout;
 
	private Vector<String> contentCollection;
	
	private boolean isEmpty(String text){
		if (text == null || text.trim().length() <= 0) {
			return true;
		}
		return false;
	}
 
	public String getUrlStr() {
		return urlStr;
	}
	public void setUrlStr(String urlStr) {
		this.urlStr = urlStr;
	}

	public int getDefaultPort() {
		return defaultPort;
	}
	public void setDefaultPort(int defaultPort) {
		this.defaultPort = defaultPort;
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}

	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}
	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public Vector<String> getContentCollection() {
		return contentCollection;
	}
	public void setContentCollection(Vector<String> contentCollection) {
		this.contentCollection = contentCollection;
	}
	public void addLine(String line) {
		if (contentCollection == null) {
			contentCollection = new Vector<String>();
		}
		contentCollection.add(line);
	}

	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("urlString", urlStr);
			jo.put("defaultPort", defaultPort);
			jo.put("file", fileName);
			jo.put("host", host);
			jo.put("path", path);
			jo.put("port", port);
			jo.put("protocol", protocol);
			jo.put("query", query);
			jo.put("ref", ref);
			jo.put("userInfo", userInfo);
			jo.put("contentEncoding", contentEncoding);
			jo.put("content", content);
			jo.put("contentType", contentType);
			jo.put("code", code);
			jo.put("message", msg);
			jo.put("method", method);
			jo.put("connectTimeout", connectTimeout);
			jo.put("readTimeout", readTimeout);
			// 连接list字符串
			String allLine = "";
			if (contentCollection != null && contentCollection.size() > 0) {
				StringBuilder sb = new StringBuilder();
				for (String line : contentCollection) {
					if (!isEmpty(line)) {
						sb.append("&").append(line);
					}
				}
				allLine = sb.toString();
				if (!isEmpty(allLine)) {
					allLine = allLine.replaceFirst("&", "");
				}
			}
			jo.put("contentCollection", allLine);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo.toString();
	}
	
}
