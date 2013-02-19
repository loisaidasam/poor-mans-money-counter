
package com.samsandberg.poormansmoneycounter;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class PrefsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
	protected static final String TAG = "PrefsActivity";

	protected static final String PREF_KEY_TOTAL = "totalString";
	
	protected SharedPreferences settings;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	addPreferencesFromResource(R.xml.prefs);
    	settings = PreferenceManager.getDefaultSharedPreferences(this);
    }

	@Override
	protected void onResume(){
		super.onResume();
		
		Preference preference = findPreference(PREF_KEY_TOTAL);
		EditTextPreference editTextPreference = (EditTextPreference) preference;
		String totalString = "" + settings.getFloat("total", 0.0f);
		editTextPreference.setSummary("Current amount: " + totalString);
		editTextPreference.setText(totalString);
		
		// Set up a listener whenever a key changes
		settings.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister the listener whenever a key changes
		settings.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    	Log.d(TAG, "onSharedPreferenceChanged(" + key + ")");

		Preference preference = findPreference(key);
		if (key.equals(PREF_KEY_TOTAL)) {
			EditTextPreference editTextPreference = (EditTextPreference) preference;
			
			// First trim the text
			// Also, temporarily disable the listener while we change the value
			settings.unregisterOnSharedPreferenceChangeListener(this);
			String totalString = editTextPreference.getText().trim();
    		SharedPreferences.Editor editor = settings.edit();
    		editor.putFloat("total", Float.parseFloat(totalString));
    		editor.commit();
    		editTextPreference.setSummary("Current amount: " + totalString);
			settings.registerOnSharedPreferenceChangeListener(this);
		}
	}
}