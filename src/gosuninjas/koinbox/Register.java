package gosuninjas.koinbox;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Register extends Activity implements OnClickListener {
	Button back_button,register_button;
	String username, email, password, name, age, university, home, away;
	EditText username_box, email_box, password_box, name_box, age_box, university_box, home_box, away_box;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.register);
	back_button = (Button) findViewById(R.id.back);
	back_button.setOnClickListener(this);
	register_button = (Button) findViewById(R.id.register_submit);
	register_button.setOnClickListener(this);
	username_box = (EditText) findViewById(R.id.register_username);
	password_box = (EditText) findViewById(R.id.register_password);
	email_box = (EditText) findViewById(R.id.register_email);
	age_box = (EditText) findViewById(R.id.age_input);
	name_box = (EditText) findViewById(R.id.name_input);
	university_box = (EditText) findViewById(R.id.university_input);
	home_box = (EditText) findViewById(R.id.home_input);
	away_box = (EditText) findViewById(R.id.away_input);
	}
	
	public void onClick(View v){
		switch (v.getId()){
    	case R.id.back:
    		Intent i =new Intent(this, Koinbox.class);
    		startActivity(i);
    		break;
    	case R.id.register_submit:
    		username=username_box.getText().toString();
    		email = email_box.getText().toString();
    		password = password_box.getText().toString();
    		name = name_box.getText().toString();
    		age = age_box.getText().toString();
    		university = university_box.getText().toString();
    		home = home_box.getText().toString();
    		away = away_box.getText().toString();
    		try {
				registerUser(username,password,email);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		try {
				createProfile(username, name, age, university, home, away);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		Koinbox.password = password;
    		Koinbox.username = username;
    		
    		i =new Intent(this, Home.class);
    		startActivity(i);
			break;
    	}
		
			
	}
	
	public void registerUser(String username, String password, String email) throws JSONException{
		HttpClient client = new DefaultHttpClient();  
	    HttpPost post = new HttpPost("http://10.0.2.2:8000/api/v1/newuser/");
	    post.setHeader("Content-type", "application/json");
	    post.setHeader("Accept", "application/json");
	    JSONObject obj = new JSONObject();
	    obj.put("username", username);
	    obj.put("password", password);
	    obj.put("email", email);
	    try {
			post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    try {
			HttpResponse response = client.execute(post);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
	}
	
	public void createProfile(String username, String name, String age, String university, String home, String away) throws JSONException, ClientProtocolException, IOException{
		HttpClient client = new DefaultHttpClient();  
		HttpGet get = new HttpGet("http://10.0.2.2:8000/api/v1/user/?format=json&username="+username);
		HttpResponse r = client.execute(get);
		
		HttpEntity e = r.getEntity();
		String data = EntityUtils.toString(e);
		JSONObject input = new JSONObject(data);
		JSONArray user_stream = input.getJSONArray("objects");
		JSONObject user = user_stream.getJSONObject(0);
		String userid = user.getString("resource_uri");
	    HttpPost post = new HttpPost("http://10.0.2.2:8000/api/v1/createprofile/");
	    post.setHeader("Content-type", "application/json");
	    post.setHeader("Accept", "application/json");
	    JSONObject obj = new JSONObject();
	    obj.put("name", name);
	    obj.put("age", age);
	    obj.put("university", university);
	    obj.put("home_city", home);
	    obj.put("away_city", away);
	    obj.put("user", userid);
	   
	    try {
			post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    try {
			HttpResponse response = client.execute(post);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
	}

}
