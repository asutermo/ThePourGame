package org.android.pourgame;

import com.google.android.maps.MapActivity;

import android.content.Context;
import android.os.Bundle;
import com.google.android.maps.*;

public class BreweryFinderActivity extends MapActivity implements
		OrientationListener {
	
	private static Context CONTEXT;
	private MapView mapView;
	private MapController mapController;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brewery_finder);
        CONTEXT = this;
        
        mapView = (MapView)findViewById(R.id.mapView);
        
        mapView.setBuiltInZoomControls(true);
        
        mapController = mapView.getController();
        mapController.setZoom(16);
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
