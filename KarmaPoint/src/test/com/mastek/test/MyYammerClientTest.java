
package com.mastek.test;

import java.io.IOException;
import java.util.List;

import org.xml.sax.SAXException;

import com.mastek.domain.MyYammerClient;
import com.mastek.domain.YamUser;

public class MyYammerClientTest {
	public static void main(String[] args) throws IOException, SAXException {
		String appId = "GOwXRzHxjaeAM2TWcqxckA";
		String secretKey = "LcQH8Mdc5cqxBqHScw1xMVScYOeyBEbt3AqisCZowQk";
		String userName = "vinaya.salgaonkar@mastek.com";
		String pwd = "xxxx";
		
		MyYammerClient client = new MyYammerClient(appId, secretKey,userName, pwd);
		//testMsgDownload(client);
	}

	
	private static void testMsgDownload(MyYammerClient client) {
		client.msgDownload();
	}

	private static void testUserDownload(MyYammerClient client)
	{
		List<YamUser> users = client.getAllUsers();	
		System.out.println(users.size());
	}	
	
}
