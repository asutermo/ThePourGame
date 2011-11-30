package org.android.pourgame.breweryfinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.android.pourgame.R;
import org.android.pourgame.ThePourGameActivity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.MapActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
        mapOverlay = new MapOverlay();
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
				
				mapOverlay.setLocationPoint(point);
				List<Overlay> overlayList = mapView.getOverlays();
				overlayList.clear();
				overlayList.add(mapOverlay);
				
				progressDialog.dismiss();
				
				
				String jsonStringBreweries = getBreweriesJson(point);
				Log.i(TAG, jsonStringBreweries);
				
				parseJsonObjects(jsonStringBreweries);
				
				for(Brewery brewery : breweryList)
				{
					mapOverlay = new MapOverlay();
					mapOverlay.setLocationPoint(brewery.getLocation());
					mapOverlay.onTap(brewery.getLocation(), mapView);
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
			
			Drawable icon = res.getDrawable(R.drawable.user);
			
			canvas.drawBitmap(((BitmapDrawable)icon).getBitmap(), screenPxs.x, screenPxs.y-12, null);
			return true;
		}
    }
    
    protected String getBreweriesJson(GeoPoint point)
	{
		StringBuilder sb = new StringBuilder();
		Double latitude = point.getLatitudeE6()/1E6;
		Double longitude = point.getLongitudeE6()/1E6;
		
		try {
			//Builds the URL that we will use to communicate with google
			URI uri = new URI("https://maps.googleapis.com/maps/api/place/search/json?" +
					"location=" + latitude + "," + longitude + "&" +
					"radius=10000&" +
					"name=brewery&" +
					"sensor=true&" +
					"key=" + places_key);
			Log.i(TAG, uri.toString());
			
			//HTTP client that will talk to the server through the URI
			HttpClient client = new DefaultHttpClient();
			
			//Creates a GET request for the HTTP server
			HttpGet request = new HttpGet();
			
			//Tells the request the URI that it is supposed to communcate with
			request.setURI(uri);
			
			//Captures the response from the HTTP server
			HttpResponse response = client.execute(request);
			
			//Directs the resposne through an input string
			InputStream ips = response.getEntity().getContent();
			
			//Reads the input stream through a buffered reader until there is nothing left to read
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
			
			//close streams
			buf.close();
			ips.close();
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Return the string builder
		return sb.toString();
	}
    
    private void parseJsonObjects(String jsonStringBreweries)
    {
    	try {
    		//creates a JSON Object assuming that the string passed to it is of a valid syntax
			JSONObject jsonBreweries = new JSONObject(jsonStringBreweries);
			//Creates an array of JSON objects for each individual result
			JSONArray jsonArrayBreweries = jsonBreweries.getJSONArray("results");
			
			//Extracts the necessary data such as latitude, longitude, address, and name of each result and stores it
			//in a Brewery object that resides in a global brewery list
			for(int i = 0; i < jsonArrayBreweries.length(); i++)
			{
				JSONObject brewery = jsonArrayBreweries.getJSONObject(i);
				Double longitude = brewery.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
				Double latitude = brewery.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
				String address = brewery.getString("vicinity");
				String name = brewery.getString("name");
				
				Log.i(TAG, "Found Name: " + name + " Addresss: " + address + " Lat: " + latitude + " Long: " + longitude);
				
				GeoPoint tmpPoint = new GeoPoint((int)(latitude*1E6), (int)(longitude*1E6));
				
				breweryList.add(new Brewery(name, tmpPoint, address));
			}
		} catch (JSONException e) {
			e.printStackTrace();
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
	
	private class Brewery{
		private String name;
		private GeoPoint point;
		private String address;
		
		public Brewery(String name, GeoPoint location, String address)
		{
			this.name = name;
			this.address = address;
			this.point = location;
		}

		public String getName() {
			return name;
		}

		public GeoPoint getLocation() {
			return point;
		}

		public String getAddress() {
			return address;
		}
		
		
	}
}
