package org.android.pourgame;

import com.google.android.maps.MapActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.MapController;
import com.google.android.maps.GeoPoint;

public class BreweryFinderActivity extends MapActivity implements
		 OnGestureListener {
	private static final int SWIPE_MIN = 120;
	private static final int SWIPE_MAX_OFF = 250;
	private static final int SWIPE_THRESH_VEL = 200;
	private GestureDetector gestureDetector;
	
	private static Context CONTEXT;
	private MapView mapView;
	private MapController mapController;
	private LocationManager locationManager;
	private LocationListener locationListener;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brewery_finder);
        CONTEXT = this;
        
        //Initializes the map view to the phone's current location
        initScene();
        gestureDetector = new GestureDetector(this, this);
        //TODO: Search for nearby breweries from current location
        
    }
    
    @Override 
    public boolean onTouchEvent(MotionEvent me){ 
      this.gestureDetector.onTouchEvent(me);
     return super.onTouchEvent(me); 
    }

    
    private void initScene()
    {
    	//Get the mapView object from the xml layout
    	mapView = (MapView)findViewById(R.id.mapView);
        
    	//Allow user to zoom in and out
        mapView.setBuiltInZoomControls(true);
        
        //Grabs the controller of the mapView
        mapController = mapView.getController();
        //Initially zooms in to predefined level
        mapController.setZoom(16);
        
        //Grabs the location service from the android system
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        //Initializes the location listener with listener defined below
        locationListener = new GPSLocationListener();
        
        //Ask the system to update the location on the map every time the user moves 100 meters
        //TODO: Implement so that it only updates the location once
        //TODO: Make brewery search wait on location update
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);
    }
    
    @Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// Check movement along the Y-axis. If it exceeds SWIPE_MAX_OFF_PATH, then dismiss the swipe.
        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF)
            return false;

        // Swipe from left to right.
        // The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE) and a certain velocity (SWIPE_THRESHOLD_VELOCITY).
        if (e2.getX() - e1.getX() > SWIPE_MIN
                && Math.abs(velocityX) > SWIPE_THRESH_VEL) {
            previous();
            return true;
        }

        return false;
	}
    
    public void previous()
	{
		Intent previous = new Intent(getApplicationContext(), ThePourGameActivity.class);
		startActivity(previous);
	}
	
    
    private class GPSLocationListener implements LocationListener
    {

		@Override
		public void onLocationChanged(Location location) {
			if(location != null)
			{
				//Grabs the latitude and longitude to the updated location
				GeoPoint point = new GeoPoint(
						(int)(location.getLatitude()*1E6),
						(int)(location.getLongitude()*1E6));
				Toast.makeText(CONTEXT, "LAT: "+location.getLatitude() +
						"\nLONG: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
				//Zooms before moving to your current location
				mapController.setZoom(8);
				//Animates the map to your current location
				mapController.animateTo(point);
				//Redraws the scene. I have no idea why this is here but I found it in a tutorial
				mapView.invalidate();
			}
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(CONTEXT, "Please enable GPS", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    
    @Override
    public void onConfigurationChanged(Configuration con) {
      super.onConfigurationChanged(con);
      setContentView(R.layout.brewery_finder);
    }


    @Override
    protected void onResume() {
        super.onResume();
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
 
    }
    
	public static Context getContext() {
		return CONTEXT;
	}



	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
