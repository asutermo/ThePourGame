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

	@Override
	public void onOrientationChanged(float azimuth, float pitch, float roll) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTopUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBottomUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRightUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLeftUp() {
		// TODO Auto-generated method stub
		
	}

	public static Context getContext() {
		// TODO Auto-generated method stub
		return CONTEXT;
	}
}