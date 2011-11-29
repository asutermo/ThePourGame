package org.android.pourgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.MapActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brewery_finder);
        CONTEXT = this;
        places_key = getResources().getString(R.string.places_api_key);
        Log.i(TAG, "API key: " + places_key);
        
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
				GeoPoint point = new GeoPoint(
						(int)(location.getLatitude()*1E6),
						(int)(location.getLongitude()*1E6));
				Log.i(TAG, "LAT: " + location.getLatitude() +  " LONG: " + location.getLongitude());
//				Toast.makeText(CONTEXT, "LAT: "+location.getLatitude() +
//						"\nLONG: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
				//Zooms before moving to your current location
				mapController.setZoom(16);
				//Animates the map to your current location
				mapController.animateTo(point);
				
				MapOverlay mapOverlay = new MapOverlay();
				mapOverlay.setLocationPoint(point);
				List<Overlay> overlayList = mapView.getOverlays();
				overlayList.clear();
				overlayList.add(mapOverlay);
				
				progressDialog.dismiss();
				
				Geocoder geoCoder = new Geocoder(getBaseContext());
				List<Address> nearestBreweries;
				
				String info = getNearestBreweries(point);
				Log.i(TAG, info);
				try {
					JSONObject information = new JSONObject(getNearestBreweries(point));
				} catch (JSONException e) {
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
    
    protected String getNearestBreweries(GeoPoint point)
	{
		StringBuilder sb = new StringBuilder();
		Double latitude = point.getLatitudeE6()/1E6;
		Double longitude = point.getLongitudeE6()/1E6;
		
		try {
			URI uri = new URI("https://maps.googleapis.com/maps/api/place/search/json?" +
					"location=" + latitude + "," + longitude + "&" +
					"radius=500&" +
					"types=food&" +
					"name=brewery&sensor=true&" +
					"key=" + places_key);
			Log.i(TAG, uri.toString());
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(uri);
			HttpResponse response = client.execute(request);
			InputStream ips = response.getEntity().getContent();
			BufferedReader buf = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
			
			String s;
			while(true)
			{
				s = buf.readLine();
				if(s == null)
				{
					break;
				}
				sb.append(s);
			}
			
			buf.close();
			ips.close();
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
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
