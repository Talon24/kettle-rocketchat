package org.kettle.rocketchat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//import org.json.JSONArray;
import org.json.JSONObject;

public class RocketchatClient {
	private String charset = java.nio.charset.StandardCharsets.UTF_8.name();
	private String url;
	private String user;
	private String password;
	private Map<String, String> headers = new HashMap<>();
	private Map<String, String> contacts = new HashMap<>();
	
	public RocketchatClient(String url, String user, String password) {
		if (!url.endsWith("/")) {url = url + "/";}
		url = url + "/api/v1/";
		this.url = url;
		this.user = user;
		this.password = password;
		try {
			login();
			load_contacts();
//			System.out.println(contacts);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JSONObject send_message(String receiver, String content) throws MalformedURLException, IOException {
		HashMap<String, String> payload = new HashMap<String, String>();
		payload.put("roomId", contacts.get(receiver));
		payload.put("text", content);
		return post_message(payload);
	}
	
	public JSONObject send_message(String receiver, String content, String alias, String emoji) throws MalformedURLException, IOException {
		HashMap<String, String> payload = new HashMap<String, String>();
		payload.put("roomId", contacts.get(receiver));
		payload.put("text", content);
		payload.put("alias", alias);
		payload.put("emoji", emoji);
		return post_message(payload);
	}
	
	private JSONObject post_message(Map<String, String> payload) throws MalformedURLException, IOException {
		JSONObject result = post("chat.postMessage", new JSONObject(payload).toString());
//		System.out.println(result);
		return result;
	}
	
	private void login() throws MalformedURLException, IOException {

		
		Map<String, String> payload = new HashMap<String, String>();
		payload.put("user", user);
		payload.put("password", password);
		JSONObject result = post("login", new JSONObject(payload).toString());
		System.out.println(result.toString(4));
		System.out.println(result.getString("status") == "success");
	    String token = result.getJSONObject("data").getString("authToken");
	    String userid = result.getJSONObject("data").getString("userId");
	    headers.put("X-Auth-Token", token);
	    headers.put("X-User-Id", userid);
	}
	
	private void load_contacts() throws MalformedURLException, IOException { 
	
		JSONObject channels = get("channels.list");
		JSONObject groups = get("groups.list");
		for (Object channel : channels.getJSONArray("channels")) {
			JSONObject obj = (JSONObject) channel;
			contacts.put(obj.getString("name"), obj.getString("_id"));
		}
		for (Object channel : groups.getJSONArray("groups")) {
			JSONObject obj = (JSONObject) channel;
			contacts.put(obj.getString("name"), obj.getString("_id"));
		}
		
	}
	
	private JSONObject get(String endpoint) throws MalformedURLException, IOException {
		URLConnection connection = new URL(url + endpoint).openConnection();
		connection.setRequestProperty("X-Auth-Token", headers.get("X-Auth-Token"));
		connection.setRequestProperty("X-User-Id", headers.get("X-User-Id"));
		connection.setRequestProperty("Accept-Charset", charset);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
		
	
//			READ ANSWER
		InputStream response = connection.getInputStream();
		try (Scanner scanner = new Scanner(response)) {
		    String responseBody = scanner.useDelimiter("\\A").next();
//		    System.out.println(responseBody);
		    JSONObject json = new JSONObject(responseBody);
		    return json;
		}
	}
	
//	private JSONObject get(String endpoint, String params) throws MalformedURLException, IOException {
//		URLConnection connection = new URL(url + endpoint + "?" + params).openConnection();
//		connection.setRequestProperty("X-Auth-Token", headers.get("X-Auth-Token"));
//		connection.setRequestProperty("X-User-Id", headers.get("X-User-Id"));
//		connection.setRequestProperty("Accept-Charset", charset);
//		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
//		
//	
//		InputStream response = connection.getInputStream();
//		try (Scanner scanner = new Scanner(response)) {
//		    String responseBody = scanner.useDelimiter("\\A").next();
//		    JSONObject json = new JSONObject(responseBody);
//		    return json;
//		}
//	}

	private JSONObject post(String endpoint, String payload) throws MalformedURLException, IOException {
		URLConnection connection = new URL(url + endpoint).openConnection();
		connection.setRequestProperty("X-Auth-Token", headers.get("X-Auth-Token"));
		connection.setRequestProperty("X-User-Id", headers.get("X-User-Id"));
		connection.setRequestProperty("Accept-Charset", charset);
//		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
		connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
//		for (Map.Entry<String, String> element : payload.entrySet()) {
//			connection.setRequestProperty(element.getKey(), element.getValue());
//		}
		connection.setDoOutput(true); // Triggers POST.
		try(OutputStream os = connection.getOutputStream()) {
		    byte[] input = payload.getBytes("utf-8");
		    os.write(input, 0, input.length);     
		    os.flush();
		    os.close();
		}
		
	
		InputStream response = connection.getInputStream();
		try (Scanner scanner = new Scanner(response)) {
		    String responseBody = scanner.useDelimiter("\\A").next();
		    JSONObject json = new JSONObject(responseBody);
		    return json;
		}
	}
}
