package com.mastek.domain;

public class MsgSummary {
	private int userid;
	private String username;
	private int threads;      // Threads started by the person
	private int avgresponses; // Avg Responses received on the threads started by them 
	private int responses;    // Responses made by the users
	private int postreplied;  // Responses made to the posts other than their own
	private int avglikes;	  // Avg likes received total likes / threads +responses
	private int noresponsethreads;  // threads started but got no response
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getThreads() {
		return threads;
	}
	public void setThreads(int threads) {
		this.threads = threads;
	}
	public int getAvgresponses() {
		return avgresponses;
	}
	public void setAvgresponses(int avgresponses) {
		this.avgresponses = avgresponses;
	}
	public int getResponses() {
		return responses;
	}
	public void setResponses(int responses) {
		this.responses = responses;
	}
	public int getPostreplied() {
		return postreplied;
	}
	public void setPostreplied(int postreplied) {
		this.postreplied = postreplied;
	}
	public int getAvglikes() {
		return avglikes;
	}
	public void setAvglikes(int avglikes) {
		this.avglikes = avglikes;
	}
	public int getNoresponsethreads() {
		return noresponsethreads;
	}
	public void setNoresponsethreads(int noresponsethreads) {
		this.noresponsethreads = noresponsethreads;
	}
	
	
}
