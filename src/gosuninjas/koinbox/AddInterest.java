package gosuninjas.koinbox;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddInterest extends Activity implements OnClickListener,OnItemSelectedListener {
	Button save;
	EditText editdescription;
	String[] items = {"Music","Sports","Movie","Game"};
	String type,description;
	@Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.addinterest);
	     save = (Button) findViewById(R.id.submit_add_interest);
	     save.setOnClickListener(this);
	     editdescription = (EditText) findViewById(R.id.add_interest);
	     Spinner my_spin=(Spinner)findViewById(R.id.add_type_spinner);
	     my_spin.setOnItemSelectedListener(this);
	     ArrayAdapter aa=new ArrayAdapter(this, android.R.layout.simple_spinner_item,items);
	     aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     my_spin.setAdapter(aa);
	}
	public void onItemSelected(AdapterView arg0, View arg1, int pos, long arg3) {
		type=items[pos];		
	}

	public void onNothingSelected(AdapterView arg0) {
		// TODO Auto-generated method stub
		type="Music";
		
	}
	
	public void onClick(View v){
		switch (v.getId()){
		case R.id.submit_add_interest:
			description = editdescription.getText().toString();
			try {
				addInterest(type,description);
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
			Intent i = new Intent(this, UserProfile.class);
			startActivity(i);
			break;
		}
	}
	
	public void addInterest(String type, String description) throws JSONException, ClientProtocolException, IOException{
		HttpClient client = new DefaultHttpClient();  
	    HttpPost post = new HttpPost("http://10.0.2.2:8000/api/v1/createinterest/");
	    post.setHeader("Content-type", "application/json");
	    post.setHeader("Accept", "application/json");
	    JSONObject obj = new JSONObject();
	    obj.put("type_interest", type);
	    obj.put("description", description);
	    obj.put("user", UserProfile.myuserid);
	   
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
	
