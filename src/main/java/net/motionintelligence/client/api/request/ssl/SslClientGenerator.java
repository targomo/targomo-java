package net.motionintelligence.client.api.request.ssl;

import net.motionintelligence.client.api.exception.Route360ClientRuntimeException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Generates the default client. Uses {@link SSLContext} with trust-all policy.
 */
public class SslClientGenerator {

	/**
	 * Generate the default SSL client
	 * @return Client
	 */
    public static Client initClient() {
        SSLContext ctx;
        
		try {
			
			ctx = SSLContext.getInstance("SSL");
			ctx.init(null, certs, new SecureRandom());
			
			return ClientBuilder.newBuilder()
					.hostnameVerifier(new TrustAllHostNameVerifier())
					.sslContext(ctx)
					.build();
		}
		catch (NoSuchAlgorithmException | KeyManagementException e) {
			
			throw new Route360ClientRuntimeException("Exception generating SSL context.", e);
		}
    }

    private static TrustManager[] certs = new TrustManager[]{
        new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }
        }
    };

    public static class TrustAllHostNameVerifier implements HostnameVerifier {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}