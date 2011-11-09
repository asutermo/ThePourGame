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
        
        mapView = (MapView)findViewById(R.id.mapView);
        
        mapView.setBuiltInZoomControls(true);
        
        mapController = mapView.getController();
        mapController.setZoom(16);
        
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        locationListener = new GPSLocationListener();
        
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
        		0, 0, locationListener);
    }
    
    private class GPSLocationListener implements LocationListener
    {

		@Override
		public void onLocationChanged(Location location) {
			if(location != null)
			{
				GeoPoint point = new GeoPoint(
						(int)(location.getLatitude()*1E6),
						(int)(location.getLongitude()*1E6));
				Toast.makeText(CONTEXT, "LAT: "+location.getLatitude() +
						"LONG: " + location.getLongitude(), Toast.LENGTH_LONG).show();
				
				mapController.animateTo(point);
				mapController.setZoom(16);
				mapView.invalidate();
			}
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
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
