package com.mastek.test;

import java.io.IOException;

import com.mastek.domain.YammerClient;

public class YammerClientMain {

	static String appId = "GOwXRzHxjaeAM2TWcqxckA";
	static String secretKey = "LcQH8Mdc5cqxBqHScw1xMVScYOeyBEbt3AqisCZowQk";
	static String user = "vinaya.salgaonkar@mastek.com";
	static String pwd = "Online01";
	
	public static void main(String[] args) {
		try {
			YammerClient client = new YammerClient(appId, user, pwd, secretKey);
			System.out.println(client.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
