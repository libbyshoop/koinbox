package gosuninjas.koinbox;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EditInterest extends Activity implements OnClickListener,OnItemSelectedListener {
	Button save,delete_button;
	EditText editdescription;
	String[] items = {"Music","Sports","Movie","Game"};
	String type,description;
	@Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.editinterest);
	     save = (Button) findViewById(R.id.submit_edit_interest);
	     delete_button = (Button) findViewById(R.id.delete_interest);
	     save.setOnClickListener(this);
	     delete_button.setOnClickListener(this);
	     editdescription = (EditText) findViewById(R.id.edit_interest);
	     Spinner my_spin=(Spinner)findViewById(R.id.type_spinner);
	     my_spin.setOnItemSelectedListener(this);
	     editdescription.setText(UserProfile.description);
	     ArrayAdapter aa=new ArrayAdapter(this, android.R.layout.simple_spinner_item,items);
	     aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     my_spin.setAdapter(aa);
	     my_spin.setSelection(aa.getPosition(UserProfile.type));
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
		case R.id.submit_edit_interest:
			
			description=editdescription.getText().toString();
			try {
				editInterest(type,description);
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
		case R.id.delete_interest:
			try {
				deleteInterest();
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
			i = new Intent(this, UserProfile.class);
			startActivity(i);
			break;
		}
	}
	
	public void editInterest(String type, String description) throws JSONException, ClientProtocolException, IOException{
		HttpClient client = new DefaultHttpClient();  
	    HttpPut put = new HttpPut("http://myapp-gosuninjas.dotcloud.com/api/v1/createinterest/"+UserProfile.interestpoint+"/");
	    put.setHeader("Content-type", "application/json");
	    put.setHeader("Accept", "application/json");
	    JSONObject obj = new JSONObject();
	    obj.put("type_interest", type);
	    obj.put("description", description);
	    obj.put("user", UserProfile.myuserid);
	   
	    try {
			put.setEntity(new StringEntity(obj.toString(), "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    try {
			HttpResponse response = client.execute(put);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
			
		}
	public void deleteInterest() throws JSONException, ClientProtocolException, IOException{
		HttpClient client = new DefaultHttpClient();  
	    HttpDelete del = new HttpDelete("http://myapp-gosuninjas.dotcloud.com/api/v1/createinterest/"+UserProfile.interestpoint+"/");
	    del.setHeader("Content-type", "application/json");
	    del.setHeader("Accept", "application/json");
	   
	    try {
			HttpResponse response = client.execute(del);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
			
		}
}
