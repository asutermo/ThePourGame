package org.android.pourgame;

import com.google.android.maps.MapActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class BreweryFinderActivity extends MapActivity implements
		OrientationListener {
	
	private static Context CONTEXT;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brewery_finder);
        CONTEXT = this;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (OrientationManager.isSupported()) {
            OrientationManager.startListening(this);
        }
    }
    
    @Override
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



	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
