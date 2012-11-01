package gosuninjas.koinbox;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfile extends Activity implements OnClickListener {
	
	TextView name,age,university,home,away;
	Button myinterest,mykoinbox,logout;
	HttpClient client;
	JSONObject json;
	final static String URL_pre = "http://10.0.2.2:8000/api/v1/userprofile/?format=json";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.userprofile);
	name = (TextView) findViewById(R.id.name);
	age = (TextView) findViewById(R.id.age);
	university = (TextView) findViewById(R.id.university);
	home = (TextView) findViewById(R.id.home_city);
	away = (TextView) findViewById(R.id.away_city);
	client = new DefaultHttpClient();
	logout = (Button) findViewById(R.id.logout);
	logout.setOnClickListener(this);
	new Read().execute("name");
	}
	
	public void onClick(View v){
    	switch (v.getId()){
    	case R.id.logout:
    		Intent i =new Intent(this, Koinbox.class);
    		startActivity(i);
    		break;
    	}
    	}
	
	public JSONObject userprofile(String username, String password) throws ClientProtocolException, IOException, JSONException{
		StringBuilder url = new StringBuilder(URL_pre);
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username,password);    
		
		HttpGet get = new HttpGet(url.toString());
		get.addHeader(BasicScheme.authenticate(creds, "UTF-8", false));
		HttpResponse r = client.execute(get);
		int status = r.getStatusLine().getStatusCode();
		
		if (status == 200){
			HttpEntity e = r.getEntity();
			String data = EntityUtils.toString(e);
			JSONObject input = new JSONObject(data);
			JSONArray profile_stream = input.getJSONArray("objects");
			JSONObject profile = profile_stream.getJSONObject(0);
			return profile;
		}else{
			Toast.makeText(UserProfile.this, "error", Toast.LENGTH_SHORT).show();
			return null;
		}
	}
	
	public class Read extends AsyncTask<String, Integer, String[]>{
		@Override
		protected String[] doInBackground(String... params) {
			try {
				json = userprofile(Koinbox.username, Koinbox.password);
				String[] final_array = new String[10];
				final_array[0]=json.getString("name");
				final_array[1]=json.getString("age");
				final_array[2]=json.getString("university");
				final_array[3]=json.getString("home_city");
				final_array[4]=json.getString("away_city");
				return final_array;
				
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
			return null;
		}
		
		@Override
		protected void onPostExecute(String[] result){
			name.setText(result[0]);
			age.setText(result[1]);
			university.setText(result[2]);
			home.setText(result[3]);
			away.setText(result[4]);
		}
		
	}
}
