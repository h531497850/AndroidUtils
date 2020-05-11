package android.hqs.net;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class NullHostNameVerifier implements HostnameVerifier {
	
	@Override
	public boolean verify(String hostname, SSLSession session) {
		// print("RestUtilImpl: Approving certificate for " + hostname);
		return true;
	}
	
}