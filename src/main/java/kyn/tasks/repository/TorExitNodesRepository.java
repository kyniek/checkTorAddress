package kyn.tasks.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class TorExitNodesRepository
{

	private static List<String> nodesCache;

	@Value("${tor.list}")
	String torList;
	
	@Value("${tor.update.interval}")
	long updateInterval;
	
	LocalDateTime lastChecked;

	
	
	
	
	public TorExitNodesRepository()
	{
		nodesCache = new ArrayList<String>();
	}

	public List<String> getNodesCache()
	{		
		if(lastChecked == null || lastChecked.until(LocalDateTime.now(), ChronoUnit.MINUTES) > updateInterval)
		{
			updateData();
		}
		System.out.println( lastChecked.until(LocalDateTime.now(), ChronoUnit.MINUTES) + " > " + updateInterval);
		return nodesCache;
	}

	private void updateData()
	{
		nodesCache.clear();
		/*
		 * fix for Exception in thread "main" javax.net.ssl.SSLHandshakeException:
		 * sun.security.validator.ValidatorException: PKIX path building failed:
		 * sun.security.provider.certpath.SunCertPathBuilderException: unable to find
		 * valid certification path to requested target
		 */
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
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


		try
		{
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
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

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier()
		{
			public boolean verify(String hostname, SSLSession session)
			{
				return true;
			}
		};
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		/*
		 * end of the fix
		 */

		

		try
		{
			URL url = new URL(torList);
			URLConnection conn = url.openConnection();

			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String inputLine;
			while ((inputLine = br.readLine()) != null)
			{
				// System.out.println(inputLine);
				if (inputLine.startsWith("ExitAddress"))
				{
					nodesCache.add(inputLine.split(" ")[1]);
				}
			}
			br.close();

		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}		
		lastChecked = LocalDateTime.now();
	}

}
