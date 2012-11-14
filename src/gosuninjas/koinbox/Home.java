package gosuninjas.koinbox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Home extends Activity implements OnClickListener {
	Button myprofile, mykoinbox, my_friends, aboutus,logout;
	HttpClient client;
	public static List<String> myfriends = new ArrayList<String>();
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        myprofile = (Button) findViewById(R.id.my_profile);
        
        mykoinbox = (Button) findViewById(R.id.my_koinbox);
        my_friends = (Button) findViewById(R.id.my_friends);
        aboutus = (Button) findViewById(R.id.about_us);
        logout = (Button) findViewById(R.id.log_out);
        client = new DefaultHttpClient();
        myprofile.setOnClickListener(this);
        mykoinbox.setOnClickListener(this);
        my_friends.setOnClickListener(this);
        aboutus.setOnClickListener(this);
        logout.setOnClickListener(this);
        myfriends.clear();
        try {
			friendlist();
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
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.my_profile:
			Intent i =new Intent(this, UserProfile.class);
			startActivity(i);
			break;
		case R.id.my_koinbox:
			i =new Intent(this, MyKoinbox.class);
			startActivity(i);
			break;
		case R.id.my_friends:
			i =new Intent(this, MyFriends.class);
			startActivity(i);
			break;
		case R.id.about_us:
			i =new Intent(this, AboutUs.class);
			startActivity(i);
			break;
		case R.id.log_out:
		    i =new Intent(this, Koinbox.class);
			startActivity(i);
			break;
		}
		
	}
	public void friendlist() throws ClientProtocolException, IOException, JSONException{
		HttpGet get = new HttpGet("http://myapp-gosuninjas.dotcloud.com/api/v1/friend/?format=json&username="+Koinbox.username);
		HttpResponse r = client.execute(get);
		HttpEntity e = r.getEntity();
		String data = EntityUtils.toString(e);
		JSONObject input = new JSONObject(data);
		JSONArray friendlist_stream = input.getJSONArray("objects");
		for (int i=0;i<friendlist_stream.length();i++){
			myfriends.add(friendlist_stream.getJSONObject(i).getString("friend_username"));
		}
	}
	
}
