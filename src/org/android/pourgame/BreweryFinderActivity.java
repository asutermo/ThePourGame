package org.android.pourgame;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class BreweryFinderActivity extends Activity implements
		OrientationListener {
	
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
	public void onBottomUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLeftUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOrientationChanged(float azimuth, float pitch, float roll) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRightUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTopUp() {
		// TODO Auto-generated method stub

	}
	
	public static Context getContext() {
		return CONTEXT;
	}
}
