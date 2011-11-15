package org.android.pourgame;

import java.io.IOException;
import java.util.List;

import com.google.android.maps.MapActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.MapController;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;

public class BreweryFinderActivity extends MapActivity{
	public static final String TAG = "BreweryFinderActivity";
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
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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
				mapController.setZoom(16);
				//Animates the map to your current location
				mapController.animateTo(point);
				
				MapOverlay mapOverlay = new MapOverlay();
				mapOverlay.setLocationPoint(point);
				List<Overlay> overlayList = mapView.getOverlays();
				overlayList.clear();
				overlayList.add(mapOverlay);
				
				Geocoder geoCoder = new Geocoder(getBaseContext());
				List<Address> nearestBreweries;
				
				try {
					nearestBreweries = geoCoder.getFromLocationName("Liquor", 10);
					if(nearestBreweries.size() > 0){
						//TODO: I don't know if this is the right way to go about this
						point = new GeoPoint((int)(nearestBreweries.get(0).getLatitude()*1E6), (int)(nearestBreweries.get(0).getLongitude()*1E6));
						mapController.animateTo(point);
						mapOverlay = new MapOverlay();
						mapOverlay.setLocationPoint(point);
						overlayList.add(mapOverlay);
					}else{
						Log.i(TAG, "Could not find breweries for some reason");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
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
    
    private class MapOverlay extends Overlay {
    	private GeoPoint locationPoint;

		public GeoPoint getLocationPoint() {
			return locationPoint;
		}

		public void setLocationPoint(GeoPoint locationPoint) {
			this.locationPoint = locationPoint;
		}
		
		@Override
		public boolean draw(Canvas canvas, MapView mv, boolean shadow, long when)
		{
			super.draw(canvas, mv, shadow);
			Point screenPxs = new Point();
			
			mv.getProjection().toPixels(locationPoint, screenPxs);
			
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
			canvas.drawBitmap(bmp, screenPxs.x, screenPxs.y-12, null);
			
			return true;
		}
    }
    
    @Override
    public void onConfigurationChanged(Configuration con) {
      super.onConfigurationChanged(con);
      //Do Nothing. This makes sure the layout does not reload on screen rotations.
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
	public void onBackPressed()
	{
		Intent back = new Intent(getApplicationContext(), ThePourGameActivity.class);
		startActivity(back);
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}
}
