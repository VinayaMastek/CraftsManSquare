package com.mastek.test;

import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Test;

import com.mastek.domain.YamAuth;

public class AuthenticationTest {
	YamAuth auth = new YamAuth();

	@Test
	public void testAuthorizationCode() {
		assertNotNull(auth.getAuthorizationCode());
	}


	@Test
	public void testClientId() {
		assertNotNull(auth.getClientId());
	}

	
	@Test
	public void testSecretKey() {
		assertNotNull(auth.getSecretKey());
	}

	@Test
	public void testgetURL() {
		URL url = auth.getAuthURL();
		assertNotNull(url);
	}


	@Test
	public void testgetAuthCode() {
		String authcode = auth.getAuthToken();
		assertNotNull(authcode);
	}

	
	@Test
	public void testAccessCode() {
		String accesstoken = auth.getAccessToken();
		assertNotNull(accesstoken);
	}

	
}
