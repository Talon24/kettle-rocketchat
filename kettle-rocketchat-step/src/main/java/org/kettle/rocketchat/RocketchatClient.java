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

import org.json.JSONArray;
//import org.json.JSONArray;
import org.json.JSONObject;

public class RocketchatClient {
    private String charset = java.nio.charset.StandardCharsets.UTF_8.name();
    private String url;
    private String user;
    private String password;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> contacts = new HashMap<>();

    public RocketchatClient(String url, String user, String password) throws IOException {
        if (!url.endsWith("/")) url = url + "/";
        if (!url.endsWith("api/v1/")) url = url + "api/v1/";
        this.url = url;
        this.user = user;
        this.password = password;
        login();
        load_contacts();
    }

    public JSONObject send_message(String receiver, String content) throws MalformedURLException, IOException {
        HashMap<String, String> payload = new HashMap<String, String>();
//        payload.put("roomId", contacts.get(receiver));
//        payload.put("text", content);
//        return post_message(payload);
        return send_message(receiver, content, "", "");
    }

    public JSONObject send_message(String receiver, String content, String alias, String emoji)
            throws MalformedURLException, IOException {
        HashMap<String, String> payload = new HashMap<String, String>();
        payload.put("text", content);
        payload.put("alias", alias);
        if (emoji.startsWith(":") && emoji.endsWith(":")) {
            payload.put("emoji", emoji);
        } else {
            payload.put("avatar", emoji);
        }
        if (receiver.startsWith("#")) {
            payload.put("channel", receiver);
        } else {
            if (!contacts.containsKey(receiver)) {
                create_im(receiver);
            }
            payload.put("roomId", contacts.get(receiver));
        }
        JSONObject result = post_message(payload);
        return result;
    }

    private JSONObject post_message(Map<String, String> payload) throws MalformedURLException, IOException {
        JSONObject result = post("chat.postMessage", payload);
        return result;
    }

    private void create_im(String user) throws MalformedURLException, IOException {
        HashMap<String, String> payload = new HashMap<String, String>();
        payload.put("username", user);
        JSONObject result = post("im.create", payload);
        String key = result.getJSONObject("room").getString("_id");
        contacts.put(user, key);
    }

    private void login() throws MalformedURLException, IOException {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("user", user);
        payload.put("password", password);
        JSONObject result = post("login", payload);
//        System.out.println(result.toString(4));
//        System.out.println(result.getString("status") == "success");
        String token = result.getJSONObject("data").getString("authToken");
        String userid = result.getJSONObject("data").getString("userId");
        headers.put("X-Auth-Token", token);
        headers.put("X-User-Id", userid);
    }

    private void load_contacts() throws MalformedURLException, IOException {

        JSONObject channels = get("im.list");
        for (Object im : channels.getJSONArray("ims")) {
            JSONObject obj = (JSONObject) im;
            JSONArray names = obj.getJSONArray("usernames");
            String name = null;
            if (!names.get(0).equals(user)) {
                name = names.getString(0);
            } else {
                name = names.getString(1);
            }
            contacts.put(name, obj.getString("_id"));
        }

    }

    private JSONObject get(String endpoint) throws MalformedURLException, IOException {
        URLConnection connection = new URL(url + endpoint).openConnection();
        connection.setRequestProperty("X-Auth-Token", headers.get("X-Auth-Token"));
        connection.setRequestProperty("X-User-Id", headers.get("X-User-Id"));
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

//            READ ANSWER
        InputStream response = connection.getInputStream();
        try (Scanner scanner = new Scanner(response)) {
            String responseBody = scanner.useDelimiter("\\A").next();
            JSONObject json = new JSONObject(responseBody);
            return json;
        }
    }

    private JSONObject post(String endpoint, Map<String, String> payload) throws MalformedURLException, IOException {
        URLConnection connection = new URL(url + endpoint).openConnection();
        connection.setRequestProperty("X-Auth-Token", headers.get("X-Auth-Token"));
        connection.setRequestProperty("X-User-Id", headers.get("X-User-Id"));
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
        connection.setDoOutput(true); // Triggers POST.
        String payload_string = new JSONObject(payload).toString();
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload_string.getBytes("utf-8");
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
