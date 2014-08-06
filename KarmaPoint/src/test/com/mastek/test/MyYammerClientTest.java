
package com.mastek.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.xerces.impl.dv.xs.FullDVFactory;
import org.xml.sax.SAXException;

import com.mastek.domain.MyYammerClient;
import com.mastek.domain.PostgresImpl;
import com.mastek.domain.YamMsg;
import com.mastek.domain.YamParam;
import com.mastek.domain.YamUser;

public class MyYammerClientTest {
	public static void main(String[] args) throws IOException, SAXException {
		String appId = "GOwXRzHxjaeAM2TWcqxckA";
		String secretKey = "LcQH8Mdc5cqxBqHScw1xMVScYOeyBEbt3AqisCZowQk";
		String userName = "vinaya.salgaonkar@mastek.com";
		String pwd = "Online01";
		
		MyYammerClient client = new MyYammerClient(appId, secretKey,userName, pwd);
		testMsgDownload(client);
	}

	
	private static void testMsgDownload(MyYammerClient client) {
		client.msgDownload();
	}


	private static void testIncrementalMsgDownload(MyYammerClient client)
	{
		PostgresImpl pg = new PostgresImpl();
		pg.connect();
		YamParam param = pg.getParameters();
		pg.close();
		
		List<YamMsg> msgs = new ArrayList<YamMsg>();
		//client.incrementalMsgDownload();
	}
	
	
	private static void testInterMsgDownload(MyYammerClient client)
	{
		PostgresImpl pg = new PostgresImpl();
		pg.connect();
		YamParam param = pg.getParameters();
		pg.close();
		
		List<YamMsg> msgs = new ArrayList<YamMsg>();
		//client.interMsgDownload(msgs, param);
	}
	

	private static void testUserDownload(MyYammerClient client)
	{
		List<YamUser> users = client.getAllUsers();	
		System.out.println(users.size());
	}	
	
}
