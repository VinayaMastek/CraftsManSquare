package com.mastek.domain;

public class YamUser {
	private int userId;
	private String fullName;
	private String jobTitle;
	private String email;
	private String activatedAt;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getActivatedAt() {
		return activatedAt;
	}
	public void setActivatedAt(String activatedAt) {
		this.activatedAt = activatedAt;
	}
	@Override
	public String toString() {
		return "YamUser [userId=" + userId + ", fullName=" + fullName
				+ ", jobTitle=" + jobTitle + ", email=" + email
				+ ", activatedAt=" + activatedAt + "]";
	}

	

}


//{"type":"user","id":8404188,"network_id":9321,"state":"active","guid":null,"job_title":"Sr. Accounts Executive","location":null,"significant_other":null,"kids_names":null,"interests":null,"summary":null,"expertise":null,"full_name":"Ajit Wadekar","activated_at":"2011/11/18 04:52:28 +0000","show_ask_for_photo":false,"first_name":"Ajit","last_name":"Wadekar","network_name":"mastek.com","network_domains":["mastek.com"],"url":"https://www.yammer.com/api/v1/users/8404188","web_url":"https://www.yammer.com/mastek.com/users/ajitwadekar","name":"ajitwadekar","mugshot_url":"https://mug0.assets-yammer.com/mugshot/images/48x48/vKRbnms8Kk49wfj6zLCh54P5hxtNWM19","mugshot_url_template":"https://mug0.assets-yammer.com/mugshot/images/{width}x{height}/vKRbnms8Kk49wfj6zLCh54P5hxtNWM19","hire_date":null,"birth_date":"","timezone":"Pacific Time (US & Canada)","external_urls":[],"admin":"false","verified_admin":"false","can_broadcast":"false","department":"Accounts & Finance","previous_companies":[],"schools":[],"contact":{"im":{"provider":"","username":""},"phone_numbers":[],"email_addresses":[{"type":"primary","address":"ajit.wadekar@mastek.com"}],"has_fake_email":false},"stats":{"following":17,"followers":20,"updates":1},"settings":{"xdr_proxy":"https://xdrproxy.yammer.com"}}
