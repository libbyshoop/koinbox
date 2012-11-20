package gosuninjas.koinbox;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Koinbox extends Activity implements OnClickListener {
	public static String username;
	public static String password;
	EditText username_box, password_box;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        View login = findViewById(R.id.login);
        View register = findViewById(R.id.register);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        username_box = (EditText) findViewById(R.id.username);
        
        password_box = (EditText) findViewById(R.id.password);
        
    }
    
    public void onClick(View v){
    	switch (v.getId()){
    	case R.id.login:
    		Intent i =new Intent(this, Home.class);
    		username = username_box.getText().toString();
    		password = password_box.getText().toString();
    		try {
				if (!username.equals("") && !password.equals("") && checkuser(username)){
					startActivity(i);
					break;
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		break;
    	case R.id.register:
    		i = new Intent(this,Register.class);
    		startActivity(i);
    		break;
    }
    }
    
    public static boolean checkuser(String username) throws ClientProtocolException, IOException, JSONException{
    	HttpClient client = new DefaultHttpClient();
    	boolean result = false;
    	HttpGet get = new HttpGet("http://myapp-gosuninjas.dotcloud.com/api/v1/user/?format=json");
		HttpResponse r = client.execute(get);
		HttpEntity e = r.getEntity();
		String data = EntityUtils.toString(e);
		JSONObject input = new JSONObject(data);
		JSONArray user_list = input.getJSONArray("objects");
		for (int i=0;i<user_list.length();i++){
			if (username.equals(user_list.getJSONObject(i).getString("username"))){
				result=true;
				break;
			}
		}
		return result;
    }
}
