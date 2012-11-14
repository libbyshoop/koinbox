package gosuninjas.koinbox;

import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import gosuninjas.koinbox.OtherUserProfile.Read;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyKoinbox extends Activity {
	 HttpClient client;
	 JSONArray jsonarray;
	 
	 public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mykoinbox);
        client = new DefaultHttpClient();
        new Read().execute();
        
	 }
	 public JSONArray mykoinbox(String username) throws ClientProtocolException, IOException, JSONException{
		    final HttpPost httppost = new HttpPost("http://myapp-gosuninjas.dotcloud.com/login_page/"); 

		    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		    postParameters.add(new BasicNameValuePair("username", Koinbox.username));
		    postParameters.add(new BasicNameValuePair("password", Koinbox.password));
		    httppost.setEntity(new UrlEncodedFormEntity(postParameters));
		    HttpResponse response = client.execute(httppost);
            HttpGet get1 = new HttpGet("http://myapp-gosuninjas.dotcloud.com/koinbox/");
            HttpResponse r1 = client.execute(get1);
			HttpGet get = new HttpGet("http://myapp-gosuninjas.dotcloud.com/api/v1/mykoinbox/?format=json&username="+username);
			HttpResponse r = client.execute(get);
			
			
			int status = r.getStatusLine().getStatusCode();
			
			if (status == 200){
				HttpEntity e = r.getEntity();
				String data = EntityUtils.toString(e);
				JSONObject input = new JSONObject(data);
				JSONArray mykoinbox_stream= input.getJSONArray("objects");
				return mykoinbox_stream;
			}else{
				Toast.makeText(MyKoinbox.this, "error", Toast.LENGTH_SHORT).show();
				return null;
			}
		}

	 public class Read extends AsyncTask<String, Integer, String[]>{
			@Override
			protected String[] doInBackground(String... params) {
				try {
					jsonarray = mykoinbox(Koinbox.username);
					String[] result_array = new String[jsonarray.length()];
					for (int i=0;i<jsonarray.length();i++){
						result_array[i]=jsonarray.getJSONObject(i).getString("koinbox_username");
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
				final TableLayout layout = (TableLayout) findViewById(R.id.mykoinbox_layout);
				TextView[] tx = new TextView[jsonarray.length()];
				for (int i=0;i<tx.length;i++){
					tx[i]=new TextView(MyKoinbox.this);
					final String k = result[i];
					tx[i].setText(result[i]);
					tx[i].setOnClickListener(new TextView.OnClickListener() {  
						   public void onClick(View v) {
							   OtherUserProfile.other_username=k;
							   Intent i = new Intent(MyKoinbox.this, OtherUserProfile.class);
							   startActivity(i);
						   }
						   });
					layout.addView(tx[i]);
				}
			}
			
		}
	 
	 
}
