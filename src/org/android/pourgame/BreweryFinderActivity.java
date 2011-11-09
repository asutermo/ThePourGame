package org.android.pourgame;

import com.google.android.maps.MapActivity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.MapController;
import com.google.android.maps.GeoPoint;

public class BreweryFinderActivity extends MapActivity implements
		OrientationListener {
	
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
        
        //Grabs the location service from the android system
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        //Initializes the location listener with listener defined below
        locationListener = new GPSLocationListener();
        
        //Ask the system to update the location on the map every time the user moves 100 meters
        //TODO: Implement so that it only updates the location once
        //TODO: Make brewery search wait on location update
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);
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
				
				mapController.animateTo(point);
				mapController.setZoom(16);
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
