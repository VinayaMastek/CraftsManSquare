package com.mastek.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.mastek.domain.YammerClient;

public class YammerClientTest {
	String appId = "GOwXRzHxjaeAM2TWcqxckA";
	String secretKey = "LcQH8Mdc5cqxBqHScw1xMVScYOeyBEbt3AqisCZowQk";
	String user = "vinaya.salgaonkar@mastek.com";
	String pwd = "Online01";
			
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testYammerClient() {
		YammerClient client = null;
		try {
			client = new YammerClient(appId, user, pwd, secretKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertNotNull(client);
	}

}
