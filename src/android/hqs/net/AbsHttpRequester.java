package android.hqs.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import android.content.Context;
import android.os.Build;

/**
 * HTTP请求对象
 * <p>
 * <b>下面的一些编码习惯(这样做是为了搜索更方便)：</b><br>
 * m 开头表示是全局的；<br>
 * t 开头表示是临时的。
 * <p>
 * ///////////////////////// 创建HttpURLConnection ///////////////
 * <p>
 * 任何网络连接都需要经过socket才能连接，HttpURLConnection不需要设置socket，
 * 所以，HttpURLConnection并不是底层的连接，而是在底层连接上的一个请求。
 * 这就是为什么HttpURLConneciton只是一个抽象类，自身不能被实例化的原因。
 * HttpURLConnection只能通过URL.openConnection()方法创建具体的实例。
 * 
 * @author 胡青松
 */
public abstract class AbsHttpRequester {
	
	// 请求方法
	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";
	// private static final String METHOD_HEAD = "";// http head信息

	// 服务器返回内容的编码格式
	public static final String Encode_UTF_8 = "UTF-8";
	public static final String Encode_GBK = "GBK";

	/**
	 * 连接服务器的超时时间，15秒
	 */
	public static final int CONNECT_TIMEOUT = 15 * 1000;
	/**
	 * 读取返回数据的超时时间，20秒
	 */
	public static final int READ_TIMEOUT = 20 * 1000;
	
	/** 将参数拼接成字符串，默认请求方式 */
	public static final byte POST_PARAMS = 0;
	/** 上传的是实体，用表M，上传的是字符，将会被压缩后上传 */
	public static final byte POST_SRT = POST_PARAMS + 1;
	/** 上传的是实体，用表M，上传的是压缩包 */
	public static final byte POST_ZIP = POST_SRT + 1;
	
	/**
	 * 断开链接，子类复写
	 */
	public void disconnect() {}
	/**
	 * 如果为POST请求时，又要自定义请求格式，如：上传文件等，请复写，
	 * 且在调用该方法之前请调用{@link #setPostType(byte)}，其中byte参数为{@link #POST_ENTRY}等
	 * @param urlConn
	 * @param params
	 * 				请求参数
	 * @param entries
	 * 				实体，如：字符串，文件等，key要和服务器匹配，如file，elog等;
	 * 				value要告知服务器的文件本地路径或者其他要上传压缩的数据
	 * @throws Exception
	 */
	protected void postEntry(HttpURLConnection urlConn, Map<String, String> params, Map<String, String> entries) throws Exception {}
	
	private IConnParams mConnParams;
	private HttpResponed mCallback;
	
	private Context mContext;
	
	/**基本的，初始化后不要变*/
	private String mBaseUrl;
	
	private Map<String, String> mRequestParams;
	private Map<String, String> mEntries;
	
	private Map<String, String> mPropertys;
	
	/**
	 * 服务器返回内容的编码格式
	 */
	private String defaultContentEncoding;
	/**
	 * 请求服务器的编码格式，默认为{@link #Encode_UTF_8}
	 */
	private String requestEncode = Encode_UTF_8;
	/** 代理名 */
	private String mUserAgent;
	/** 代理IP */
	private String mProxyIp;
	
	/**
	 * 连接失败后，可重连的次数，每重连一次 -1，默认重连1次，因为HTTPS请求时可能出现地址转移，
	 * 而HttpURLConnection与HttpsURLConnection之间地址不会自动迁移。
	 */
	private int mRetryTimes = 1;
	private int mRequestType = HttpRequestType.NONE;
	private byte mPostType = POST_PARAMS;
	
	/**
	 * 一般是http请求
	 */
	private boolean isHttps = false;
	
	@SuppressWarnings("deprecation")
	public AbsHttpRequester(Context context) {
		mContext = context;
		defaultContentEncoding = Charset.defaultCharset().name();
		// Solve calling close() on a readable InputStream could poison the connection pool. 
		// disable connection reuse if necessary
		// HTTP connection reuse which was buggy pre-froyo
		if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
	        System.setProperty("http.keepAlive", "false");
	    }
	}

	// TODO =========================================== Request mothod =======================================
	/**
	 * 发送GET请求
	 * 
	 * @throws Exception
	 */
	public void sendGet() throws Exception {
		print("Request method: " + METHOD_GET);
		
		String url = mBaseUrl;
		if (mRequestParams != null) {
			url += makeRequestParams(true, mRequestParams);
		}
		print("url = " + url);
		HttpURLConnection tUrlConn = creatHttpConn(url);
		setCommConnParams(tUrlConn);
		addRequestPropertys(tUrlConn);
		tUrlConn.connect();
		makeContent(tUrlConn);
	}

	/**
	 * 发送POST请求，如果是表单请求方式，请重写该方法。
	 * 
	 * @param params
	 *            
	 * @param entrys
	 *            
	 * @param propertys
	 *            请求链接属性，某个属性的key为空，抛NullPointerException
	 * @return 响映对象
	 * @throws Exception
	 */
	public void sendPost() throws Exception {
		print("Request method: " + METHOD_POST);
		//print("url = " + mBaseUrl);

		HttpURLConnection tUrlConn = creatHttpConn(mBaseUrl);
		setPostConnParams(tUrlConn);
		addRequestPropertys(tUrlConn);
		switch (mPostType) {
		case POST_PARAMS:
			postParams(tUrlConn, mRequestParams);
			break;
		case POST_SRT:
		case POST_ZIP:
			postEntry(tUrlConn, mRequestParams, mEntries);
			break;
		default:
			print("No params request type!");
			break;
		}
		makeContent(tUrlConn);
	}

	/**
	 * 发送请求头
	 * 
	 * @param propertys
	 *            请求链接属性，某个属性的key为空，抛NullPointerException
	 * @return 响映对象
	 * @throws Exception
	 */
	public void sendHead() throws Exception {
		print("Head request method : " + METHOD_GET);
		HttpURLConnection tUrlConn = creatHttpConn(mBaseUrl);
		setCommConnParams(tUrlConn);
		addRequestPropertys(tUrlConn);
		tUrlConn.connect();
		makeContent(tUrlConn);
	}
	
	/**
	 * 开始连接了，子类可复写
	 * @throws Exception 
	 */
	protected void onRequest() throws Exception{
		try {
			switch (mRequestType) {
			case HttpRequestType.CLIENT_GET:
			case HttpRequestType.CONNECTION_GET:
				sendGet();
				break;
			case HttpRequestType.CLIENT_POST:
			case HttpRequestType.CONNECTION_POST:
				sendPost();
				break;
			case HttpRequestType.CLIENT_HEAD:
				sendHead();
				break;
			default:
				print("Unrecognized request mode");
				break;
			}
		} catch (Exception e) {
			if (mRetryTimes > 0) {
				mRetryTimes--;
				onRequest();
			} else {
				throw e;
			}
		}
	}
	
	/**
	 * 内部出现故障时，用http的方式重连，子类可复写
	 * @param location 新地址
	 * @throws Exception 
	 */
	protected void retryHttpConnect(String location) throws Exception{
		mBaseUrl = location;
		isHttps = false;
		onRequest();
	}
	/**
	 * 内部出现故障时，用https的方式连，子类可复写
	 * @param location 新地址
	 * @throws Exception 
	 */
	protected void retryHttpsConnect(String location) throws Exception{
		mBaseUrl = location;
		isHttps = true;
		onRequest();
	}
	
	private void postParams(HttpURLConnection urlConn, Map<String, String> params) throws Exception {
		if (params != null) {
			String suffix = makeRequestParams(false, params);
			if (!isEmpty(suffix)) {
				BufferedOutputStream requestStream = null;
				try {
					requestStream = new BufferedOutputStream(urlConn.getOutputStream());
					// 向对象输出流写出请求参数数据，这些数据将存到内存缓冲区中
					requestStream.write(suffix.getBytes(Encode_UTF_8));
					requestStream.flush();
				} catch (Exception e) {
					throw e;
				} finally {
					close(requestStream);
				}
			}
		}
	}
	
	// TODO =========================================== Property mothod =======================================
	/**
	 * 添加常用请求属性<br>
	 * 注意：请求方式为POST时要在“Content-Type”后连接boundary属性，其他方式为null
	 * @param urlConn
	 * @throws Exception
	 */
	protected void addRequestPropertys(HttpURLConnection urlConn) throws Exception {
		urlConn.setRequestProperty("Accept","*/*");
		urlConn.setRequestProperty("Connection", "Keep-Alive");
		// charset=UTF-8指定POST数据的编码类型
		urlConn.setRequestProperty("Charset",Encode_UTF_8);
		urlConn.addRequestProperty("Cache-Control", "no-cache");
		urlConn.addRequestProperty("Content-Type", getContentType());
		// Gzip压缩可以在请求头部的accept encoding出进行设置以禁用
		// urlConn.setRequestProperty("Accept-Encoding", "identity");
		
		if (!isEmpty(mUserAgent)) {
			urlConn.addRequestProperty("User-Agent", mUserAgent);
		}
		if (!isEmpty(mProxyIp)) {
			urlConn.addRequestProperty("X-Online-Host", urlConn.getURL().getHost() + ":" + urlConn.getURL().getPort());
		}
		if (mPropertys != null) {
			for (Map.Entry<String, String> entry : mPropertys.entrySet()) {
				String key = entry.getKey();
				if (isBlank(key)) {
					throw new NullPointerException("key is blank!");
				}
				urlConn.addRequestProperty(key, entry.getValue());
			}
		}
	}
	
	/**
	 * 创建HttpURLConnection连接对象，注意如果是HTTPS请求，请先调用{@link #setHttps(boolean)}为true，
	 * 然后再调用该方法创建连接对象（在该方法里已自动进行证书校验），如果返回码是301/302，表示服务器将请求地址转移了，
	 * 使用{@link HttpsURLConnection#getHeaderField(String)}，其中的字符串参数是"Location"。
	 * @param urlStr 请求地址
	 * @param certificates 你的证书，可以什么都不输入
	 * @return
	 * @throws Exception
	 */
	protected HttpURLConnection creatHttpConn(String urlStr, InputStream... certificates) throws Exception {
		if (isHttps) {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			SSLContext	mSslContext = SSLContext.getInstance("TLS");
			HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
			// 载入证书
			TrustManager[] trustAllCerts = new TrustManager[] { new NullX509TrustManager() };
			mSslContext.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(mSslContext.getSocketFactory());
			
			// 方法2
			// mSslContext = SSLContext.getInstance("SSL", "SunJSSE");
			// 载入证书
			/*CertificateFactory cf = CertificateFactory.getInstance("X.509");
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null);
			trustStore.load(null, null);
			int index = 0;
			for (InputStream certificate : certificates) {
				String certificateAlias = Integer.toString(index++);
				trustStore.setCertificateEntry(certificateAlias, cf.generateCertificate(certificate));
				try {
					if (certificate != null) {
						certificate.close();
					}
				} catch (IOException e) {
					print("BBKSSLSocketFactory = " + e.getMessage());
				}
			}
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(trustStore); // i //
			mSslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
			mSslContext.init(null, new TrustManager[] { new SSLX509TrustManager() }, new SecureRandom());
			mSslContext.init(null, new TrustManager[]{tm}, null);*/
			//HttpsURLConnection.setDefaultSSLSocketFactory(new BasicSocketFactory().getSSLSocketFactory());
			//tHttpsUrlConn.setSSLSocketFactory(new BasicSocketFactory().getSSLSocketFactory());
		}
		URL tUrl = new URL(urlStr);
		return (HttpURLConnection) tUrl.openConnection();
	}
	
	/**
	 * 为GET/POST/HEAD请求方式设置常用连接参数
	 * @param urlConn
	 * @throws Exception
	 */
	protected void setCommConnParams(HttpURLConnection urlConn){
		// 设置是否从urlConnection读入，默认情况下是true;
		urlConn.setDoInput(true);
		// 超时设置，防止 网络异常的情况下，可能会导致程序僵死而不继续往下执行
		// 设置连接主机超时（单位：毫秒）
		urlConn.setConnectTimeout(CONNECT_TIMEOUT);
		// 设置从主机读取数据超时（单位：毫秒）
		urlConn.setReadTimeout(READ_TIMEOUT);
		// JDK 1.5以前的版本，只能通过设置这两个系统属性来控制网络超时
		// System.setProperty("sun.NET.client.defaultConnectTimeout", String.valueOf(CONNECT_TIMEOUT));
		// System.setProperty("sun.Net.client.defaultReadTimeout", String.valueOf(READ_TIMEOUT));
		// 或者
		/*
		 * Properties timeOut = new Properties();
		 * timeOut.put("sun.NET.client.defaultConnectTimeout", CONNECT_TIMEOUT);
		 * timeOut.put("sun.Net.client.defaultReadTimeout", READ_TIMEOUT);
		 * System.setProperties(timeOut);
		 */
		if (mConnParams != null) {
			mConnParams.addYourConnParams(urlConn);
		}
	}
	
	/**
	 * 为POST请求方式设置常用连接参数
	 * @param urlConn
	 * @throws Exception
	 */
	protected void setPostConnParams(HttpURLConnection urlConn) throws Exception {
		urlConn.setRequestMethod(METHOD_POST);
		// 请求体是可选的上传内容，一个HttpURLConnection对象要想携带请求体必须设置setDoOutput(true)
		// 设置是否向urlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true,默认情况下是false;
		urlConn.setDoOutput(true);
		// Post 请求不能使用缓存，默认情况下是true;
		urlConn.setUseCaches(false);
		urlConn.setInstanceFollowRedirects(true);
		// 为了达到最佳的性能，你应该：
		// 如果数据长度已知，设置setFixedLengthStreamingMode(int)
		// 如果数据长度未知，设置setChunkStramingMode(int)
		// 否则，HttpURLConnection会强制在传输数据前将整个请求体缓存于内存中，浪费（或有可能耗尽）堆空间并且增大延迟。
		urlConn.setChunkedStreamingMode(0);
		setCommConnParams(urlConn);
	}

	/**
	 * 子类如果要复写该接口，不能返回空，要不然服务端接收不到请求参数。
	 * application/x-java-serialized-object是设定传送的内容类型是可序列化的java对象,
	 * (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)。
	 * @return 1、application/json;application/x-www-form-urlencoded;application/x-java-serialized-object;multipart/form-data<br>
	 *         2、application/json;charset=UTF-8;application/x-java-serialized-object
	 */
	protected String getContentType() {
		return "application/json;application/x-www-form-urlencoded;application/x-java-serialized-object;multipart/form-data";
	}
	
	/**
	 * 组装请求参数，普通请求，非表单
	 * @param isGet 请求方式
	 * @param params 请求参数
	 * @return 组装后的参数
	 * @throws Exception
	 */
	protected String makeRequestParams(boolean isGet, Map<String, String> params) throws Exception{
		if (params != null && params.size() > 0) {
			StringBuffer param = new StringBuffer();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				String key = entry.getKey();
				if (isBlank(key)) {
					throw new NullPointerException("key is blank!");
				}
				String val = entry.getValue();
				if ("city".equals(key)) {
					//print("Request parameters are encoded in GBK format.");
					key = URLEncoder.encode(key, Encode_GBK);
					if (val != null) {
						val = URLEncoder.encode(val, Encode_GBK);
					}
				} else {
					//print("Request parameters are encoded in UTF-8 format.");
					key = URLEncoder.encode(key, Encode_UTF_8);
					if (val != null) {
						val = URLEncoder.encode(val, Encode_UTF_8);
					}
				}
				param.append("&").append(key).append("=").append(val);
			}
			String tParam = param.toString();
			if (!isEmpty(tParam)) {
				if (isGet) {
					/*
					 * 使用GET方法时，请求参数和对应的值附加在URL后面，利用一个问号（“?”）代表URL的结尾与请求参数的开始，
					 * 传递参数长度受限制，最多只能有1024字节。
					 */
					return tParam.replaceFirst("&", "?");
				} else {
					/* 使用POST方法时，请求参数。 */
					return tParam.replaceFirst("&", "");
				}
			}
		}
		return "";
	}
	
	
	/**
	 * TODO 用HttpURLConneciton得到响应对象
	 * 
	 * @param urlConn
	 * @throws IOException
	 */
	private void makeContent(HttpURLConnection urlConn) throws Exception {
		HttpResponser httpResponser = new HttpResponser();
		boolean success = false;
		try {
			// 封装响应信息
			String location = urlConn.getHeaderField("Location");
			httpResponser.setUrlStr(location);
			
			print("url = " + location);
			
			// getResponseCode()会隐式调用getInputStream()
			httpResponser.setCode(urlConn.getResponseCode());
			
			print("responseCode = " + httpResponser.getCode());
			
			httpResponser.setMsg(urlConn.getResponseMessage());
			httpResponser.setContentType(urlConn.getContentType());
			httpResponser.setMethod(urlConn.getRequestMethod());
			httpResponser.setConnectTimeout(urlConn.getConnectTimeout());
			httpResponser.setReadTimeout(urlConn.getReadTimeout());

			httpResponser.setDefaultPort(urlConn.getURL().getDefaultPort());
			httpResponser.setFileName(urlConn.getURL().getFile());
			httpResponser.setHost(urlConn.getURL().getHost());
			httpResponser.setPath(urlConn.getURL().getPath());
			httpResponser.setPort(urlConn.getURL().getPort());
			httpResponser.setProtocol(urlConn.getURL().getProtocol());
			httpResponser.setQuery(urlConn.getURL().getQuery());
			httpResponser.setRef(urlConn.getURL().getRef());
			httpResponser.setUserInfo(urlConn.getURL().getUserInfo());

			String ecod = urlConn.getContentEncoding();
			if (ecod == null) {
				ecod = defaultContentEncoding;
			}
			httpResponser.setContentEncoding(ecod);
			success = true;
		} catch (IOException e) {
			throw e;
		} finally {
			if (!success && urlConn != null) {
				urlConn.disconnect();
			}
		}
		
		try {
			// 读取返回信息
			switch (httpResponser.getCode()) {
			case HttpURLConnection.HTTP_OK:{
				String ecod = httpResponser.getContentEncoding();
				try {
					// 调用HttpURLConnection连接对象的getInputStream()函数得到响应对象流
					String in = readStream(ecod, urlConn.getInputStream());
					httpResponser.setContent(new String(in.getBytes(), ecod));
					if (mCallback != null) {
						mCallback.onSuccess(httpResponser);
					}
				} catch (IOException eis) {
					String in = readStream(ecod, urlConn.getErrorStream());
					httpResponser.setContent(new String(in.getBytes(), ecod));
					if (mCallback != null) {
						mCallback.onError(HttpURLConnection.HTTP_OK, httpResponser);
					}
				}
				break;
			}
			// 地址转移了
			case HttpURLConnection.HTTP_MOVED_PERM:
			case HttpURLConnection.HTTP_MOVED_TEMP:
				String location = httpResponser.getUrlStr();
				if (mRetryTimes > 0 && !isEmpty(location) && location.startsWith("https")) {
					retryHttpsConnect(location);
				} else {
					retryHttpConnect(location);
				}
				mRetryTimes--;
				break;
			default:	// 400 ETC 
				String ecod = httpResponser.getContentEncoding();
				String error = readStream(ecod, urlConn.getErrorStream());
				httpResponser.setContent(new String(error.getBytes(), ecod));
				if (mCallback != null) {
					mCallback.onError(httpResponser.getCode(), httpResponser);
				}
				break;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (urlConn != null) {
				urlConn.disconnect();
			}
		}
	}
	
	private String readStream(String ecod, InputStream is) throws IOException{
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the
		 * BufferedReader return null which means there's no more data
		 * to read. Each line will appended to a StringBuilder and
		 * returned as String.
		 */
		BufferedReader fromServer = null;
		try {
			StringBuffer temp = new StringBuffer();
			if ("gzip".equals(ecod)) {
				print("Server returns compressed file.");
				fromServer = new BufferedReader(new InputStreamReader(new GZIPInputStream(new BufferedInputStream(is))));
			} else {
				print("Server returns normal content.");
				fromServer = new BufferedReader(new InputStreamReader(is));
			}
			String line;
			// 你应该一直从流中读取数据直到读完为止，也就是当read()返回-1时
			while ((line = fromServer.readLine()) != null) {
				temp.append(line).append("\r\n");
			}
			return temp.toString();
		} catch (IOException e) {
			throw e;
		} finally {
			close(fromServer);
		}
	}
	// TODO =========================================== init params =======================================
	public Context getContext() {
		return mContext;
	}
	
	public String getUserAgent() {
		return mUserAgent;
	}

	public void setUserAgent(String userAgent) {
		this.mUserAgent = userAgent;
	}
	
	public String getProxyIp() {
		return mProxyIp;
	}

	public void setProxyIp(String proxyIp) {
		this.mProxyIp = proxyIp;
	}
	
	/**
	 * @param baseUrl 请求链接地址，不能为空
	 */
	public void setBaseUrl(String baseUrl) {
		this.mBaseUrl = baseUrl;
	}
	public String getBaseUrl() {
		return mBaseUrl;
	}
	
	public int getRequestType() {
		return mRequestType;
	}

	public void setRequestType(int type) {
		this.mRequestType = type;
	}
	
	/**
	 * 请求链接参数(不包含文件)，某个属性的key为空，将抛NullPointerException
	 * @param params
	 */
	public void setRequestParams(Map<String, String> params) {
		this.mRequestParams = params;
	}
	/**
	 * POST请求时，设置上传实体
	 * @param entries
	 */
	public void setEntries(Map<String, String> entries) {
		this.mEntries = entries;
	}
	
	/**
	 * 请求链接属性，某个属性的key为空，将抛NullPointerException
	 * @param propertys
	 */
	public void setPropertys(Map<String, String> propertys) {
		mPropertys = propertys;
	}

	public void setConnParams(IConnParams connParams) {
		this.mConnParams = connParams;
	}
	
	public void setCallback(HttpResponed callback) {
		this.mCallback = callback;
	}
	
	public boolean isHttps() {
		return isHttps;
	}
	/**
	 * 设置是否是HTTPS请求
	 * @param isHttps
	 */
	public void setHttps(boolean isHttps) {
		this.isHttps = isHttps;
	}
	
	public int getRetryTimes() {
		return mRetryTimes;
	}
	/**
	 * 设置重连次数
	 * @param retryTimes
	 */
	public void setRetryTimes(int retryTimes) {
		this.mRetryTimes = retryTimes;
	}
	
	public byte getPostType() {
		return mPostType;
	}
	/**
	 * 如果是POST请求方式，使用该方法设置请求参数类型，如：表单
	 * @param type 如果是{@link #POST_SRT}或{@link #POST_ZIP}请自己实现{@link #postEntry(HttpURLConnection, Map, Map, String)}
	 */
	public void setPostType(byte type) {
		this.mPostType = type;
	}

	// TODO ========================================== General method ======================================
	public boolean isEmpty(String data) {
		if (data == null || data.length() <= 0) {
			return true;
		}
		return false;
	}

	public boolean isBlank(String data) {
		if (data == null || data.trim().length() <= 0) {
			return true;
		}
		return false;
	}
	/**
	 * 默认的响应字符集
	 */
	public String getDefaultContentEncoding() {
		return defaultContentEncoding;
	}

	/**
	 * 设置默认的响应字符集
	 * 
	 * @param defaultContentEncoding
	 *            默认为"UTF-8"
	 */
	public void setDefaultContentEncoding(String defaultContentEncoding) {
		if (!isBlank(defaultContentEncoding)) {
			this.defaultContentEncoding = defaultContentEncoding;
		}
	}

	/**
	 * 请求服务器的编码格式，默认为{@link #Encode_UTF_8}
	 */
	public String getRequestEncode() {
		return requestEncode;
	}

	/**
	 * 设置请求服务器的编码格式
	 * 
	 * @param requestEncode
	 *            如果为空，默认为{@link #Encode_UTF_8}
	 */
	public void setRequestEncode(String requestEncode) {
		if (!isBlank(requestEncode)) {
			this.requestEncode = requestEncode;
		}
	}
	
	
	public void enableHttpResponseCache(long httpCacheSize, String cacheDir) {
	    try {
	    	if (httpCacheSize <= 0) {
	    		httpCacheSize = 10 * 1024 * 1024; // 10 MiB
			}
	        File httpCacheDir = new File(cacheDir, "http");
	        Class.forName("android.net.http.HttpResponseCache")
	            .getMethod("install", File.class, long.class)
	            .invoke(null, httpCacheDir, httpCacheSize);
	    } catch (Exception httpResponseCacheNotAvailable) {
	    }
	}
	
	public void close(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
			}
		}
	}

	public byte[] compress(byte[] data) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		compress(bais, baos);
		byte[] output = baos.toByteArray();
		baos.flush();
		baos.close();
		bais.close();
		return output;
	}

	public void compress(InputStream is, OutputStream os) throws Exception {
		GZIPOutputStream gos = new GZIPOutputStream(os);
		int count;
		byte data[] = new byte[1024];
		while ((count = is.read(data, 0, 1024)) != -1) {
			gos.write(data, 0, count);
		}
		gos.flush();
		gos.close();
	}

	/**
	 * 打印信息
	 */
	protected void print(Object obj) {
		System.out.println("lu.logsystem.AbsHttpRequester --> " + obj);
	}

}
