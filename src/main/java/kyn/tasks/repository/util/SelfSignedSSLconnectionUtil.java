package kyn.tasks.repository.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SelfSignedSSLconnectionUtil
{
	
	private static TrustManager[] managers =  new TrustManager[] { new X509TrustManager()
	{
		public java.security.cert.X509Certificate[] getAcceptedIssuers()
		{
			return null;
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType)
		{}

		public void checkServerTrusted(X509Certificate[] certs, String authType)
		{}

	} };
	
	public void setupDefaultSSLSocketFactory()
	{
		/*
		 * fix for Exception in thread "main" javax.net.ssl.SSLHandshakeException:
		 * sun.security.validator.ValidatorException: PKIX path building failed:
		 * sun.security.provider.certpath.SunCertPathBuilderException: unable to find
		 * valid certification path to requested target
		 */		
		try
		{
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, managers, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());			
		} catch (NoSuchAlgorithmException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (KeyManagementException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Install the all-trusting host verifier and use lambda instead of anonymous class
		HttpsURLConnection.setDefaultHostnameVerifier( (String hostname, SSLSession session) -> true );		
	}
	
	public List<String> getRecords(String uri, BiConsumer<List<String>, String> processRecord)
	{
		List<String> tmpList = new LinkedList<>();	
		try
		{
			URL url = new URL(uri);
			URLConnection conn = url.openConnection();

			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String inputLine;
			while ((inputLine = br.readLine()) != null)
			{				
				//addIP(tmpList, inputLine);
				processRecord.accept(tmpList, inputLine);
			}
			br.close();

		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return tmpList;
	}
	
	
}
