package org.android.pourgame;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

public class ThePourGameActivity extends Activity implements OrientationListener {
    /** Called when the activity is first created. */
	private static Context CONTEXT;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        CONTEXT = this;
    }



    protected void onResume() {
        super.onResume();
        if (OrientationManager.isSupported()) {
            OrientationManager.startListening(this);
        }
    }
 
    protected void onDestroy() {
        super.onDestroy();
        if (OrientationManager.isListening()) {
            OrientationManager.stopListening();
        }
 
    }
    
	@Override
    public void onOrientationChanged(float azimuth, float pitch, float roll)
	{
        
    }
 
    
    @Override
    public void onBottomUp() {
    }
 
    @Override
    public void onLeftUp() {
    }
 
    @Override
    public void onRightUp() {
    }
 
    @Override
    public void onTopUp() {
    }

	public static Context getContext() {
		return CONTEXT;
	}
}