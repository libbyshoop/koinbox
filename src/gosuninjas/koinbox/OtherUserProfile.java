package gosuninjas.koinbox;

import gosuninjas.koinbox.UserProfile.Read;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class OtherUserProfile extends Activity{
	
	TextView name,age,university,home,away;
	HttpClient client;
	JSONObject json;
	JSONArray jsonarray;
	public static String other_username;
	public static String userid;
	
	final static String URL_pre = "http://10.0.2.2:8000/api/v1/otheruserprofile/?format=json&user=";
	final static String URL_pre1 = "http://10.0.2.2:8000/api/v1/otheruserinterest/?format=json&user=";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.otheruser_profile);
	name = (TextView) findViewById(R.id.otheruser_name);
	age = (TextView) findViewById(R.id.otheruser_age);
	university = (TextView) findViewById(R.id.otheruser_university);
	home = (TextView) findViewById(R.id.otheruser_home_city);
	away = (TextView) findViewById(R.id.otheruser_away_city);
	client = new DefaultHttpClient();
	
	final TableLayout layout = (TableLayout) findViewById(R.id.otheruserprofile_layout);
	
	
	new Read().execute("name");
	TextView[] tx = new TextView[2];
	if (!Home.myfriends.contains(other_username)){
		tx[0]=new TextView(OtherUserProfile.this);
		tx[0].setText("[Add to your friend list]");
		tx[0].setOnClickListener(new TextView.OnClickListener() {  
			   public void onClick(View v) {
				   try {
						addFriend();
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent i = new Intent(OtherUserProfile.this,Home.class);
					startActivity(i);
			   }
			   });
		layout.addView(tx[0]);
	}
	else {
		tx[1]=new TextView(OtherUserProfile.this);
		tx[1].setText("[Delete from your friend list]");
		tx[1].setOnClickListener(new TextView.OnClickListener() {  
			   public void onClick(View v) {
				   try {
						deleteFriend();
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent i = new Intent(OtherUserProfile.this,Home.class);
					startActivity(i);
			   }
			   });
		layout.addView(tx[1]);
	}
	}
	
	
	public void addFriend() throws ClientProtocolException, IOException{
		final HttpPost httppost = new HttpPost("http://10.0.2.2:8000/login_page/"); 

	    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	    postParameters.add(new BasicNameValuePair("username", Koinbox.username));
	    postParameters.add(new BasicNameValuePair("password", Koinbox.password));
	    httppost.setEntity(new UrlEncodedFormEntity(postParameters));
	    HttpResponse response = client.execute(httppost);
	    
	    HttpGet get1 = new HttpGet("http://10.0.2.2:8000/friend/add/?username="+other_username);
        HttpResponse r1 = client.execute(get1);
        
	}
	
	public void deleteFriend() throws ClientProtocolException, IOException{
		final HttpPost httppost = new HttpPost("http://10.0.2.2:8000/login_page/"); 

	    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	    postParameters.add(new BasicNameValuePair("username", Koinbox.username));
	    postParameters.add(new BasicNameValuePair("password", Koinbox.password));
	    httppost.setEntity(new UrlEncodedFormEntity(postParameters));
	    HttpResponse response = client.execute(httppost);
	    
	    HttpGet get = new HttpGet("http://10.0.2.2:8000/friend/delete/?username="+other_username);
        HttpResponse r1 = client.execute(get);
        
	}
	
	public JSONObject otheruserprofile(String username) throws ClientProtocolException, IOException, JSONException{
		HttpGet get1 = new HttpGet("http://10.0.2.2:8000/api/v1/user/?format=json&username="+username);
		HttpResponse r1 = client.execute(get1);
		HttpEntity e1 = r1.getEntity();
		String data1 = EntityUtils.toString(e1);
		JSONObject input1 = new JSONObject(data1);
		JSONArray user_stream = input1.getJSONArray("objects");
		JSONObject user = user_stream.getJSONObject(0);
		userid = user.getString("resource_uri").substring(13).replace("/", "");
		
		StringBuilder url = new StringBuilder(URL_pre);
		HttpGet get = new HttpGet(url.toString()+userid);
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
			Toast.makeText(OtherUserProfile.this, "error", Toast.LENGTH_SHORT).show();
			return null;
		}
	}
	
	public JSONArray otheruserinterest(String username) throws ClientProtocolException, IOException, JSONException{
		StringBuilder url = new StringBuilder(URL_pre1);  
		HttpGet get = new HttpGet(url.toString()+userid);
		HttpResponse r = client.execute(get);
		int status = r.getStatusLine().getStatusCode();
		
		if (status == 200){
			HttpEntity e = r.getEntity();
			String data = EntityUtils.toString(e);
			JSONObject input = new JSONObject(data);
			JSONArray interest_stream = input.getJSONArray("objects");
			return interest_stream;
		}else{
			Toast.makeText(OtherUserProfile.this, "error", Toast.LENGTH_SHORT).show();
			return null;
		}
	}
	
	public class Read extends AsyncTask<String, Integer, String[]>{
		@Override
		protected String[] doInBackground(String... params) {
			try {
				json = otheruserprofile(other_username);
				jsonarray = otheruserinterest(other_username);
				String[] result_array = new String[jsonarray.length()*2+5];
				result_array[0]=json.getString("name");
				result_array[1]=json.getString("age");
				result_array[2]=json.getString("university");
				result_array[3]=json.getString("home_city");
				result_array[4]=json.getString("away_city");
				int j = 5;
				for (int i=0;i<jsonarray.length();i++){
					result_array[j]=jsonarray.getJSONObject(i).getString("type_interest");
					result_array[j+1]=jsonarray.getJSONObject(i).getString("description");
					j=j+2;
				}
				
				return result_array;
				
				
				
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
			final TableLayout layout = (TableLayout) findViewById(R.id.otheruserprofile_layout);
			TextView[] tx = new TextView[jsonarray.length()];
			int j = 5;
			for (int i=0;i<tx.length;i++){
				tx[i]=new TextView(OtherUserProfile.this);
				tx[i].setText("("+result[j]+") "+result[j+1]);
				layout.addView(tx[i]);
				j=j+2;
			}
		}
		
	}

}
