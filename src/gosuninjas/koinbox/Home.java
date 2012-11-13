package gosuninjas.koinbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Home extends Activity implements OnClickListener {
	Button myprofile, mykoinbox, myfriends, aboutus,logout;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        myprofile = (Button) findViewById(R.id.my_profile);
        
        mykoinbox = (Button) findViewById(R.id.my_koinbox);
        myfriends = (Button) findViewById(R.id.my_friends);
        aboutus = (Button) findViewById(R.id.about_us);
        logout = (Button) findViewById(R.id.log_out);
        
        myprofile.setOnClickListener(this);
        mykoinbox.setOnClickListener(this);
        myfriends.setOnClickListener(this);
        aboutus.setOnClickListener(this);
        logout.setOnClickListener(this);
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
			i =new Intent(this, OtherUserProfile.class);
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
}
