package gosuninjas.koinbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

public class MyFriends extends Menu {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.myfriends);
	final TableLayout layout = (TableLayout) findViewById(R.id.myfriends_layout);
	TextView[] tx = new TextView[Home.myfriends.size()];
	for (int i=0;i<tx.length;i++){
		tx[i]=new TextView(MyFriends.this);
		tx[i].setText(Home.myfriends.get(i));
		final String k = Home.myfriends.get(i);
		tx[i].setOnClickListener(new TextView.OnClickListener() {  
			   public void onClick(View v) {
				   OtherUserProfile.other_username=k;
				   Intent i = new Intent(MyFriends.this, OtherUserProfile.class);
				   startActivity(i);
			   }
			   });
		layout.addView(tx[i]);
	}
	}

}
