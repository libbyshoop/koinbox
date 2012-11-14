package gosuninjas.koinbox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfile extends Activity implements OnClickListener{
	
	TextView name,age,university,home,away;
	Button myinterest,mykoinbox,logout;
	HttpClient client;
	JSONObject json;
	JSONArray jsonarray;
	public static String myname,myuniversity,myhome,myaway,myuri,myuserid,interestpoint,type,description;
	public static int myage;

	
	final static String URL_pre = "http://10.0.2.2:8000/api/v1/userprofile/?format=json";
	final static String URL_pre1 = "http://10.0.2.2:8000/api/v1/interest/?format=json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.userprofile);
	TextView edit_profile = (TextView) findViewById(R.id.edit_profile);
	edit_profile.setOnClickListener(this);
	TextView add_interest = (TextView) findViewById(R.id.add_interest);
	add_interest.setOnClickListener(this);
	name = (TextView) findViewById(R.id.name);
	age = (TextView) findViewById(R.id.age);
	university = (TextView) findViewById(R.id.university);
	home = (TextView) findViewById(R.id.home_city);
	away = (TextView) findViewById(R.id.away_city);
	client = new DefaultHttpClient();
	new Read().execute("name");
	}
	
	public void onClick(View v){
		switch (v.getId()){
		case R.id.edit_profile:
			Intent i = new Intent(this, EditProfile.class);
			startActivity(i);
			break;
		case R.id.add_interest:
			i = new Intent(this, AddInterest.class);
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
	
	public JSONArray userinterest(String username, String password) throws ClientProtocolException, IOException, JSONException{
		StringBuilder url = new StringBuilder(URL_pre1);
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username,password);    
		
		HttpGet get = new HttpGet(url.toString());
		get.addHeader(BasicScheme.authenticate(creds, "UTF-8", false));
		HttpResponse r = client.execute(get);
		int status = r.getStatusLine().getStatusCode();
		
		if (status == 200){
			HttpEntity e = r.getEntity();
			String data = EntityUtils.toString(e);
			JSONObject input = new JSONObject(data);
			JSONArray interest_stream = input.getJSONArray("objects");
			return interest_stream;
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
				jsonarray = userinterest(Koinbox.username, Koinbox.password);
				String[] result_array = new String[jsonarray.length()*3+5];
				result_array[0]=json.getString("name");
				result_array[1]=json.getString("age");
				result_array[2]=json.getString("university");
				result_array[3]=json.getString("home_city");
				result_array[4]=json.getString("away_city");
				myuserid = json.getString("user");
				myuri = json.getString("resource_uri");
				int j = 5;
				for (int i=0;i<jsonarray.length();i++){
					result_array[j]=jsonarray.getJSONObject(i).getString("type_interest");
					result_array[j+1]=jsonarray.getJSONObject(i).getString("description");
					result_array[j+2]=jsonarray.getJSONObject(i).getString("resource_uri").substring(17).replace("/", "");
					j=j+3;
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
			myname = result[0];
			myage = Integer.parseInt(result[1]);
			myuniversity = result[2];
			myhome = result[3];
			myaway = result[4];
			name.setText(result[0]);
			age.setText(result[1]);
			university.setText(result[2]);
			home.setText(result[3]);
			away.setText(result[4]);
			final TableLayout layout = (TableLayout) findViewById(R.id.userprofile_layout);
			TextView[] tx = new TextView[jsonarray.length()];
			int j = 5;
			for (int i=0;i<tx.length;i++){
				tx[i]=new TextView(UserProfile.this);
				tx[i].setText("("+result[j]+") "+result[j+1]+"       [EDIT]");
				final String l = result[j];
				final String m = result[j+1];
				final String k = result[j+2];
				tx[i].setOnClickListener(new TextView.OnClickListener() {  
					   public void onClick(View v) {
						   type = l;
						   description = m;
						   interestpoint=k;
						   Intent i = new Intent(UserProfile.this, EditInterest.class);
						   startActivity(i);
					   }
					   });
				layout.addView(tx[i]);
				j=j+3;
			}
		}
		
	}

}
