
package com.samsandberg.poormansmoneycounter;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Broadcast receiver for receiving SMSs
 * 
 * Resources:
 * http://www.androidcompetencycenter.com/2008/12/android-api-sms-handling/
 */
public class SMSReceiver extends BroadcastReceiver {
    /** TAG used for Debug-Logging */
    private static final String TAG = "SMSReceiver";
 
    /** The Action fired by the Android-System when a SMS was received.
     * We are using the Default Package-Visibility */
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    
    public float parseAmount(String text) {
    	Pattern p = Pattern.compile(" (\\d+),(\\d\\d) EUR, ");
    	Matcher m = p.matcher(text);
    	if(! m.find()){
    		return 0;
    	}

	    MatchResult mr = m.toMatchResult();
	    String result = mr.group(1) + "." + mr.group(2);
	    return Float.parseFloat(result);
    }

	@Override
	public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            float total = settings.getFloat("total", 0);
        	
            // The SMS-Messages are 'hiding' within the extras of the Intent.
            Bundle bundle = intent.getExtras();
            
    		Object messages[] = (Object[]) bundle.get("pdus");
    		SmsMessage smsMessage[] = new SmsMessage[messages.length];
    		String from[] = new String[messages.length];
    		String msgs[] = new String[messages.length];
    		
    		for (int n = 0; n < messages.length; n++) {
    			smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
    			
    			String msgFrom = smsMessage[n].getDisplayOriginatingAddress();
    			String msgBody = smsMessage[n].getDisplayMessageBody();
    			
    			total -= parseAmount(msgBody);
    			
    			Log.d(TAG, msgFrom);
    			Log.d(TAG, msgBody);
    			
    			String display = msgFrom + " : " + msgBody;
    			Toast.makeText(context, display, Toast.LENGTH_SHORT).show();
    			
    			from[n] = msgFrom;
    			msgs[n] = msgBody;
    		}
    		
    		SharedPreferences.Editor editor = settings.edit();
    		editor.putFloat("total", total);
    		editor.commit();

			Intent newIntent = new Intent(context, MainActivity.class);
			newIntent.putExtra("from", from);
			newIntent.putExtra("msgs", msgs);
			newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(newIntent);
        }
	}
}