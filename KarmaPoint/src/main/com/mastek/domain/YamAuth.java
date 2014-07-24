package com.mastek.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

public class YamAuth {

	String clientId = "GOwXRzHxjaeAM2TWcqxckA";
	String SecretKey = "LcQH8Mdc5cqxBqHScw1xMVScYOeyBEbt3AqisCZowQk";
	String redirectURI = "http://localhost:8080/KarmaPoint/loginyammer.html";
	//String redirectURI = "http://23.21.189.152/KarmaPoints/sync.html";
	String authorizationCode = "CHp1Fv4GI4k0yR8VJjOt2Q";

	String AccessToken;
	URL url = null;

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		authorizationCode = authorizationCode;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		clientId = clientId;
	}

	public String getSecretKey() {
		return SecretKey;
	}

	public void setSecretKey(String secretKey) {
		SecretKey = secretKey;
	}

	public String getAuthToken() {
		HttpClient client = new HttpClient();
		url = getAuthURL();
		PostMethod post = new PostMethod(url.toString());

		try {
			int result = client.executeMethod(post);
			// Display status code
			System.out.println("Response status code: " + result);
			// Display response
			System.out.println("Response body: ");
			System.out.println(post.getResponseBodyAsString());
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Release current connection to the connection pool once you are
			// done
			post.releaseConnection();
		}

		/*
		 * URLConnection urlc = null; BufferedReader br = null;
		 * 
		 * try { urlc = url.openConnection(); urlc.setDoOutput(true);
		 * urlc.setAllowUserInteraction(false);
		 * 
		 * br = new BufferedReader( new
		 * InputStreamReader(urlc.getInputStream())); String l = null; while ((l
		 * = br.readLine()) != null) { System.out.println(l); } br.close(); }
		 * catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		return null;
	}

	public String getAccessToken() {
		URLConnection urlc = null;
		BufferedReader br = null;
		url = getAuthURL();
		try {
			urlc = url.openConnection();
			urlc.setDoOutput(true);
			urlc.setAllowUserInteraction(false);

			br = new BufferedReader(
					new InputStreamReader(urlc.getInputStream()));
			String l = null;
			while ((l = br.readLine()) != null) {
				System.out.println(l);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void setAccessToken(String accessToken) {
		AccessToken = accessToken;
	}

	public URL getAuthURL() {
		try {
			String query = "?client_id=" + clientId + "&redirect_uri="
					+ redirectURI;

			/*
			 * "&client_secret=" + SecretKey + "&code=" + authorizationCode;
			 * https
			 * ://www.yammer.com/dialog/oauth?client_id=[:client_id]&redirect_uri
			 * =[:redirect_uri]
			 */
			url = new URL("https://www.yammer.com/dialog/oauth" + query);
			System.out.println(url.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// use post mode
		return url;
	}
	// https://www.yammer.com/oauth2/access_token.json?client_id=GOwXRzHxjaeAM2TWcqxckA&client_secret=LcQH8Mdc5cqxBqHScw1xMVScYOeyBEbt3AqisCZowQk&code=CHp1Fv4GI4k0yR8VJjOt2Q
	// KZnbEudajBA5Cm8Si4nYWA

}
