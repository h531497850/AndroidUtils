package android.hqs.net;

public interface HttpRequestType {
	
	public static final int NONE = 0;
	
	// for Apache HttpClient
	public static final int CLIENT_GET = 1;
	public static final int CLIENT_POST = CLIENT_GET + 1;
	public static final int CONNECTION_GET = CLIENT_POST + 1;
	
	// for java HttpUrlConnection
	public static final int CONNECTION_POST = CONNECTION_GET + 1;
	public static final int CLIENT_HEAD = CONNECTION_POST + 1;
	
}