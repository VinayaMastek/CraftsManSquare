package com.mastek.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.entity.ContentType;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.SubmitButton;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

public class MyYammerClient {
	private static String OAUTH_GET_AUTH_TOKEN_URL = "https://www.yammer.com/dialog/oauth?client_id=%s";
	private static String SESSIONURL = "https://www.yammer.com/session?client_id=%s";
	private static String OAUTH_GET_ACCESS_TOKEN_URL = "https://www.yammer.com/oauth2/access_token.json?client_id=%s&client_secret=%s&code=%s";
	private static String ALLUSERURL = "https://www.yammer.com/api/v1/users.json";
	private static String ALLMSGURL = "https://www.yammer.com/api/v1/messages.json";
	

	private String applicationId;
	private String secretKey;
	private String authToken = "";
	private String userName;
	private String pwd;
	private String accessToken;
	private YamParam param;

	private WebConversation wc = null;
	private WebResponse resp = null;

	public MyYammerClient(String appId, String appSecret, String user,
			String password) throws IOException, SAXException {

		HttpUnitOptions.setExceptionsThrownOnScriptError(false);
		applicationId = appId;
		secretKey = appSecret;
		userName = user;
		pwd = password;
		wc = new WebConversation();

		// Step one - use application id, fire the authentication request and
		// get the auth token from the response
		resp = makeGetRequest(OAUTH_GET_AUTH_TOKEN_URL, false, "");
		authToken = getAuthToken(resp);
		System.out.println("Authentication Code = " + authToken);

		String postBody = "utf8=" + URLEncoder.encode("&#x2713", "UTF-8")
				+ "&authenticity_token="
				+ URLEncoder.encode(authToken, "UTF-8")
				+ "&network_permalink=mastek.com&login=" + userName
				+ "&password=" + pwd + "&remember_me=on";

		// Make a POST with the auth token and the user credentials
		resp = makePostRequest(String.format(SESSIONURL, applicationId),
				postBody, "", "");
		String accessCode = null;

		if (resp != null) {
			if (resp.getURL().getQuery().contains("code=")) {
				String[] queries = resp.getURL().getQuery().split(";");
				for (String query : queries) {
					if (query.contains("code=")) {
						accessCode = query.substring(5);
						break;
					}
				}
			} else {
				authToken = getAuthToken(resp);
				// Make a POST to emulate Allow button click;
				WebForm form = getAllowForm();
				form.setAttribute("utf8", "&#x2713");
				form.setAttribute("authenticity_token",
						URLEncoder.encode(authToken, "UTF-8"));
				SubmitButton btn = form.getSubmitButton("allow");
				form.submit(btn);
				resp = wc.getCurrentPage();
				accessCode = resp.getURL().getQuery().substring(5);
			}
		}

		// Request to get JSON object using the accessCode;
		resp = makeGetRequest(String.format(OAUTH_GET_ACCESS_TOKEN_URL,
				applicationId, secretKey, accessCode), false, "");

		// Get the AccessToke from JSON Object for further calls.
		JSONObject json = (JSONObject) JSONSerializer.toJSON(resp.getText());
		accessToken = ((JSONObject) json.get("access_token"))
				.getString("token");
	}

	private WebResponse makeGetRequest(String url, Boolean addCookies,
			String authHeader) throws IOException, SAXException {
		if (addCookies) {
			wc.putCookie("yamtrak_id", resp.getNewCookieValue("yamtrak_id"));
			wc.putCookie("_workfeed_session_id",
					resp.getNewCookieValue("_workfeed_session_id"));
		}

		if (!authHeader.isEmpty()) {
			wc.setHeaderField("Authorization", "Bearer " + authHeader);
		}

		resp = null;
		GetMethodWebRequest wr = new GetMethodWebRequest(String.format(url,
				applicationId));
		resp = wc.getResponse(wr);
		return resp;
	}

	private WebResponse makePostRequest(String url, String postBody,
			String header, String contentType) {

		WebResponse postResp = null;
		ByteArrayInputStream postbodyIs = new ByteArrayInputStream(
				postBody.getBytes());
		PostMethodWebRequest wr = new PostMethodWebRequest(
				url,
				postbodyIs,
				(contentType.isEmpty() ? ContentType.APPLICATION_FORM_URLENCODED
						.getMimeType() : contentType));

		if (!header.isEmpty())
			wr.setHeaderField("Authorization", String.format("Bearer", header));

		try {
			postResp = wc.getResponse(wr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return postResp;
	}

	private WebForm getAllowForm() {
		WebForm allowForm = null;
		try {
			for (WebForm form : resp.getForms()) {
				allowForm = form;
			}
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return allowForm;
	}

	private String getAuthToken(WebResponse resp) throws IOException,
			SAXException {
		String authCd = null;
		for (String code : resp.getMetaTagContent("name", "authenticity_token")) {
			authCd = code;
		}
		return authCd;
	}

	public List<YamUser> getAllUsers() {
		List<YamUser> yamUsers = new ArrayList<YamUser>();
		try {
			System.out.println(ALLUSERURL);
			String qsOperator = (ALLUSERURL.indexOf("?") > -1) ? "&" : "?";
			int page = 0;

			while (!resp.getText().equals("[]")) {
				page++;
				// if (page > 2) break;
				resp = makeGetRequest(ALLUSERURL + qsOperator + "page="
						+ Integer.toString(page), false, accessToken);
				new JSONSerializer();
				JSONArray users = (JSONArray) JSONSerializer.toJSON(resp
						.getText());

				YamUser user = null;
				for (Object obj : users) {
					user = new YamUser();
					JSONObject json = (JSONObject) obj;
					user.setUserId(json.getInt("id"));
					user.setFullName(json.getString("full_name"));
					user.setJobTitle(json.getString("job_title"));
					user.setEmail(getEmail(json.getJSONObject("contact")));
					user.setActivatedAt(json.getString("activated_at"));

					yamUsers.add(user);
				}

				// System.out.println("users so far...." + yamUsers.size());
				if (page % 10 == 0)
					sleep(1000);
			}
			System.out.println("Users DONE...." + yamUsers.size());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		return yamUsers;
	}

	private String getEmail(JSONObject contact) {
		String email = "Not found";
		JSONArray addresses = (JSONArray) JSONSerializer.toJSON(contact
				.get("email_addresses"));
		for (Object obj : addresses) {
			if ((((JSONObject) obj).getString("type")).equals("primary"))
				email = ((JSONObject) obj).getString("address");
		}
		return email;
	}

	private List<YamMsg> getYammerMsgs() {
		List<YamMsg> yamMsgs = new ArrayList<YamMsg>();
		String qsOperator = (ALLMSGURL.indexOf("?") > -1) ? "&" : "?";
		int page = 0;
		String url = null;
		Boolean first = false;
		try {
			while (true) {
				page++;

				// For full download break after the first page and get into
				// full inter mode;
				if (param.getDownload().equals("Full")) {
					if (page > 1)
						break;

					url = ALLMSGURL + qsOperator + "page="
							+ Integer.toString(page);
					first = true;
				}

				// For both inter /incremental download break after the number
				// of pages set in the parameter, commit
				// messages to the database and continue.
				if (page > param.getPages())
					commitMsgs(yamMsgs);

				// For inter mode set the url to the older than what was last
				// retrieved
				if (param.getDownload().equals("FullInter")) {
					url = ALLMSGURL + qsOperator + "page="
							+ Integer.toString(page) + "&older_than="
							+ param.getOldestid();
				}

				// For incremental mode set the url to the newer than what was
				// last retrieved
				// if (param.getDownload().equals("Incremental")) {
				if (param.getDownload().startsWith("Increment")) {
					if (param.getDownload().equals("IncrementalFirst"))
						first = true;

					url = ALLMSGURL + qsOperator + "page="
							+ Integer.toString(page) + "&newer_than="
							+ param.getLatestid();
				}

				System.out.println(url);
				resp = makeGetRequest(url, false, accessToken);
				JSONObject response = (JSONObject) JSONSerializer.toJSON(resp
						.getText());
				JSONArray messages = response.getJSONArray("messages");

				// If no messages
				if (messages.isEmpty()) {
					if (param.getDownload().equals("FullInter"))
						param.setDownload("IncrementalFirst");

					if (!yamMsgs.isEmpty())
						commitMsgs(yamMsgs);

					sleep(30000);
					break;
				}

				YamMsg msg = null;
				for (Object obj : messages) {
					JSONObject json = (JSONObject) obj;

					if (first || param.getDownload().equals("Incremental")) {
						first = false;
						param.setLatestid(json.getInt("id"));

						if (param.getDownload().equals("IncrementalFirst"))
							param.setDownload("Incremental");
					}

					if (!param.getDownload().startsWith("Incremental"))
						param.setOldestid(json.getInt("id"));

					msg = new YamMsg();
					msg.setMsgId(json.getInt("id"));
					msg.setSenderId(json.getInt("sender_id"));
					if (!json.get("replied_to_id").equals(null))
						msg.setRepliedToId(json.getInt("replied_to_id"));
					msg.setThreadId(json.getInt("thread_id"));
					msg.setCreatedAt(json.getString("created_at"));
					msg.setLikedBycount(json.getJSONObject("liked_by").getInt(
							"count"));
					yamMsgs.add(msg);
				}
				System.out.println("Pages Done so far - " + page
						+ " Msgs done so far - " + yamMsgs.size());

				if ((page % 8) == 0) {
					sleep(30000);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		System.out.println("Messages DONE...." + yamMsgs.size());
		return yamMsgs;

	}

	private void sleep(int sec) {
		try {
			Thread.sleep(sec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void commitMsgs(List<YamMsg> msgs) {
		if (msgs != null) {
			if (!msgs.isEmpty()) {
				PostgresImpl pg = new PostgresImpl();
				pg.updateParam(this.param);
				pg.syncYammerData("messages", msgs);
				pg.close();
				msgs = null;
			}
		}
		getYammerMsgs();
	}

	public void msgDownload() {
		PostgresImpl pg = new PostgresImpl();
		this.param = pg.getParameters();
		List<YamMsg> msgs = null;

		if (param.getDownload().equals("Full")) {
			msgs = getYammerMsgs();
			pg.syncYammerData("messages", msgs);
			param.setDownload("FullInter");
		}
		pg.updateParam(param);
		msgs = null;
		commitMsgs(msgs);
	}

}
