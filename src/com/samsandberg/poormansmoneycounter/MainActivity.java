package com.samsandberg.poormansmoneycounter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
        Intent intent = getIntent();
        String from[] = intent.getStringArrayExtra("from");
        String msgs[] = intent.getStringArrayExtra("msgs");
        
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        float total = settings.getFloat("total", 0);
        TextView tvtop = (TextView) this.findViewById(R.id.tvtop);
        tvtop.setText("Total: " + total);
        
        String text = "";
        if (from != null) {
            text += from.length + " New Msgs:\n\n";
            
            for (int i = 0; i < msgs.length; i++) {
            	text += from[i] + "\n" + msgs[i] + "\n\n";
            }
        }
        TextView tv = (TextView) this.findViewById(R.id.tvbody);
        tv.setText(text);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.menu_prefs:
	        	startActivity(new Intent(this, PrefsActivity.class));
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }
    }
}