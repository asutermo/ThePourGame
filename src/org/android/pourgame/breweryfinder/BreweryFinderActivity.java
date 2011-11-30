package org.android.pourgame.breweryfinder;

import java.util.ArrayList;
import java.util.List;

import org.android.pourgame.R;
import org.android.pourgame.ThePourGameActivity;
import org.android.pourgame.util.UtilFunctions;

import com.google.android.maps.MapActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
	private ProgressDialog progressDialog;
	private String places_key;
	private List<Brewery> breweryList;
	private MapOverlay mapOverlay;
	private Resources res;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brewery_finder);
        CONTEXT = this;
        places_key = getResources().getString(R.string.places_api_key);
        Log.i(TAG, "API key: " + places_key);
        breweryList = new ArrayList<Brewery>();
        mapOverlay = new MapOverlay(res, R.drawable.user);
        res = getResources();
        
        Button backButton = (Button)findViewById(R.id.backButton);
        
        backButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				goToMainScreen();
			}
        	
        });
        
        
        //Initializes the map view to the phone's current location
        initScene();
        
        initProgressDialog();
        
        progressDialog.show();
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
        
        //TODO: Search for nearby breweries from current location
        
    }

    
    private void initProgressDialog() {
    	progressDialog = new ProgressDialog(CONTEXT);
		progressDialog.setCancelable(true);
		progressDialog.setMessage("Finding GPS Location...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		
		progressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				goToMainScreen();
			}
			
		});
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
    }
	
    
    private class GPSLocationListener implements LocationListener
    {

		@Override
		public void onLocationChanged(Location location) {
			if(location != null)
			{
				//Grabs the latitude and longitude to the updated location
				GeoPoint point = UtilFunctions.locationToPoint(location);
				Log.i(TAG, "LAT: " + location.getLatitude() +  " LONG: " + location.getLongitude());
				
				//Zooms before moving to your current location
				mapController.setZoom(16);
				//Animates the map to your current location
				mapController.animateTo(point);
				
				mapOverlay.setLocationPoint(point);
				List<Overlay> overlayList = mapView.getOverlays();
				overlayList.clear();
				overlayList.add(mapOverlay);
				
				progressDialog.dismiss();
				
				
				String jsonStringBreweries = UtilFunctions.getBreweriesJson(location, places_key);
				Log.i(TAG, jsonStringBreweries);
				
				breweryList = UtilFunctions.parseJsonObjects(jsonStringBreweries);
				
				for(Brewery brewery : breweryList)
				{
					mapOverlay = new MapOverlay(res, R.drawable.user);
					mapOverlay.setLocationPoint(UtilFunctions.locationToPoint(brewery.getLocation()));
					overlayList.add(mapOverlay);
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
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
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
		return false;
	}
	
	private void goToMainScreen()
	{
		Intent back = new Intent(getApplicationContext(), ThePourGameActivity.class);
		startActivity(back);
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}
	
	@Override
	public void onBackPressed()
	{
		goToMainScreen();
	}
}
