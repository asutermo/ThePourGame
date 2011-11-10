package org.android.pourgame;

import com.google.android.maps.MapActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.MapController;
import com.google.android.maps.GeoPoint;

public class BreweryFinderActivity extends MapActivity{
	
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
        
        Button backButton = (Button)findViewById(R.id.backButton);
        
        backButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent back = new Intent(getApplicationContext(), ThePourGameActivity.class);
				startActivity(back);
				finish();
			}
        	
        });
        
        //Initializes the map view to the phone's current location
        initScene();
        
        //TODO: Search for nearby breweries from current location
        
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
        
        
//        Location lastKnown = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        GeoPoint lastKnownGeo = new GeoPoint((int)(lastKnown.getLatitude()*1E6), (int)(lastKnown.getLongitude()*1E6));
//        mapController.animateTo(lastKnownGeo);
        
        //Grabs the location service from the android system
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        //Initializes the location listener with listener defined below
        locationListener = new GPSLocationListener();
        
        //Ask the system to update the location on the map every time the user moves 100 meters
        //TODO: Implement so that it only updates the location once
        //TODO: Make brewery search wait on location update
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	Intent back = new Intent(getApplicationContext(), ThePourGameActivity.class);
			startActivity(back);
			finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
