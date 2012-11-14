package gosuninjas.koinbox;


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
    		if (!username.equals("") && !password.equals("")){
    			startActivity(i);
    			break;
    		}
    		break;
    	case R.id.register:
    		i = new Intent(this,Register.class);
    		startActivity(i);
    		break;
    }
    }
}
