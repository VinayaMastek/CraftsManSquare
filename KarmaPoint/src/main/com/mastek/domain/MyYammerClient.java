package com.mastek.domain;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.SubmitButton;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

public class MyYammerClient implements Closeable {
	private static String OAUTH_GET_AUTH_TOKEN_URL = "https://www.yammer.com/dialog/oauth?client_id=%s";
	private static String SESSIONURL = "https://www.yammer.com/session?client_id=%s";
	private static String OAUTH_GET_ACCESS_TOKEN_URL = "https://www.yammer.com/oauth2/access_token.json?client_id=%s&client_secret=%s&code=%s";
	private static String ALLUSERURL = "https://www.yammer.com/api/v1/users.json";

	private String applicationId;
	private String secretKey;
	private String authToken = "";
	private String userName;
	private String pwd;
	private String accessToken;

	private CloseableHttpClient httpclient = null;;
	private WebConversation wc = null;
	private WebResponse resp = null;

	public MyYammerClient(String appId, String appSecret, String user,
			String password) throws IOException, SAXException {

		// HttpUnitOptions.setScriptingEnabled(false);
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

	private String getEmail(JSONObject contact)
	{
		String email="Not found";
		JSONArray addresses = (JSONArray) new JSONSerializer().toJSON(contact.get("email_addresses"));
		for (Object obj : addresses)
		{
			if 	((((JSONObject) obj).getString("type")).equals("primary"))
				email = ((JSONObject) obj).getString("address");
		}
		return email;
	}
	
	public List<YamUser> getAllUsers() {
		List<YamUser> yamUsers = new ArrayList<YamUser>();
		try {
			System.out.println(ALLUSERURL);
			String qsOperator = (ALLUSERURL.indexOf("?") > -1) ? "&" : "?";
			int page = 0;

			while (!resp.getText().equals("[]")) {
				page++;
/*				if (page > 2)
					break;
*/						
				resp = makeGetRequest(ALLUSERURL + qsOperator + "page="
						+ Integer.toString(page), false, accessToken);
				new JSONSerializer();
				JSONArray users = (JSONArray) JSONSerializer.toJSON(resp
						.getText());
				YamUser user = null;
				for (Object obj : users) {
					System.out.println(((JSONObject) obj).toString());
					user = new YamUser();
					user.setUserId(((JSONObject) obj).getString("id"));
					user.setFullName(((JSONObject) obj).getString("full_name"));
					user.setJobTitle(((JSONObject) obj).getString("job_title"));
					user.setEmail(  getEmail( (JSONObject) ((JSONObject) obj).get("contact")));
					user.setActivatedAt(((JSONObject) obj).getString("activated_at"));
					yamUsers.add(user);
				}
				System.out.println("users so far...."+yamUsers.size());
				if (page % 10 == 0) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			System.out.println("DONE");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		return yamUsers;
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

	@Override
	public void close() throws IOException {
		if (httpclient != null) {
			httpclient.close();
		}
	}

}
