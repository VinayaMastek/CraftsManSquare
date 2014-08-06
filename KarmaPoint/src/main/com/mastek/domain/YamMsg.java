package com.mastek.domain;

public class YamMsg {
	private int msgId;
	private int senderId;
	private int repliedToId;
	private String msgText;
	private int likedBycount;
	private String createdAt;
	private int threadId;
	
	public int getMsgId() {
		return msgId;
	}
	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}
	public int getSenderId() {
		return senderId;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	public int getRepliedToId() {
		return repliedToId;
	}
	public void setRepliedToId(int repliedToId) {
		this.repliedToId = repliedToId;
	}
	public String getMsgText() {
		return msgText;
	}
	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}
	public int getLikedBycount() {
		return likedBycount;
	}
	public void setLikedBycount(int likedBycount) {
		this.likedBycount = likedBycount;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public int getThreadId() {
		return threadId;
	}
	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}
	@Override
	public String toString() {
		return "YamMsg [msgId=" + msgId + ", senderId=" + senderId
				+ ", repliedToId=" + repliedToId + ", msgText=" + msgText
				+ ", likedBycount=" + likedBycount + ", createdAt=" + createdAt
				+ ", threadId=" + threadId + "]";
	}
		
}
