package org.android.pourgame.compass;

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
import org.android.pourgame.breweryfinder.Brewery;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.Path.Direction;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Compass extends Activity {

    private static final String TAG = "Compass";

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private CompassLayoutView mView;
    private float[] mValues;
    private static final boolean debug = false;
	private static Context CONTEXT;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private ProgressDialog progressDialog;
	private String places_key;
	private List<Brewery> breweryList;
	private Resources res;
	private Location currentLocation;
	private static boolean firstPass;

    private final SensorEventListener mListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            if (debug) 
            	Log.d(TAG, "sensorChanged (" + event.values[0] + ", " + event.values[1] + ", " + event.values[2] + ")");
            mValues = event.values;
            if (mView != null) {
                mView.invalidate();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mView = new CompassLayoutView(this);
        setContentView(mView);
        CONTEXT = this;
        
        firstPass = true;
        
        breweryList = new ArrayList<Brewery>();
        
        places_key = getResources().getString(R.string.places_api_key);
        
        //Grabs the location service from the android system
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        //Initializes the location listener with listener defined below
        locationListener = new GPSLocationListener();
        
        initProgressDialog();
        
        progressDialog.show();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
    }

    @Override
    protected void onResume()
    {
        if (debug) 
        	Log.d(TAG, "onResume");
        super.onResume();

        mSensorManager.registerListener(mListener, mSensor,
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop()
    {
        if (debug) 
        	Log.d(TAG, "onStop");
        mSensorManager.unregisterListener(mListener);
        super.onStop();
    }

    private class CompassLayoutView extends View {
        private Paint   compassPaint = new Paint();
        private Path    compassPath = new Path();
        private Path breweriesPath = new Path();
        private Paint breweriesPaint = new Paint();
        private boolean mAnimate;

        public CompassLayoutView(Context context) {
            super(context);

            // Construct a wedge-shaped path
            compassPath.moveTo(0, -200);
            compassPath.lineTo(0, 200);
            compassPath.moveTo(-200, 0);
            compassPath.lineTo(200, 0);
            compassPath.addCircle(0, 0, 200, Direction.CW);
            compassPath.addCircle(0, 0, 100, Direction.CW);
        }

        @Override 
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.BLACK);
            
            if(breweryList != null && breweryList.size() > 0)
            {
	            for(Brewery brew : breweryList)
	            {
	            	List<Double> brewList = brew.getDirection();
	            	double mappedX = brewList.get(1)*200;
	            	double mappedY = -brewList.get(0)*200;
	            	
	            	breweriesPath.addCircle((float)mappedX,(float)mappedY, 5, Direction.CW);
	            }
            }

            compassPaint.setAntiAlias(true);
            compassPaint.setColor(Color.rgb(76, 122, 255));
            compassPaint.setStyle(Paint.Style.STROKE);
            compassPaint.setTextSize(20);
            compassPaint.setStrokeWidth(3);
            
            breweriesPaint.setAntiAlias(true);
            breweriesPaint.setColor(Color.GREEN);
            breweriesPaint.setStyle(Paint.Style.FILL);

            int w = canvas.getWidth();
            int cx = w / 2;
            int cy = 250;

            canvas.translate(cx, cy);
            if (mValues != null) {
                canvas.rotate(-mValues[0]);
            }
            canvas.drawPath(compassPath, compassPaint);
            canvas.drawPath(breweriesPath, breweriesPaint);
            canvas.drawText("N", 0, -220, compassPaint);
            
        }

        @Override
        protected void onAttachedToWindow() {
            mAnimate = true;
            if (debug) 
            	Log.d(TAG, "onAttachedToWindow. mAnimate=" + mAnimate);
            super.onAttachedToWindow();
        }

        @Override
        protected void onDetachedFromWindow() {
            mAnimate = false;
            if (debug) 
            	Log.d(TAG, "onDetachedFromWindow. mAnimate=" + mAnimate);
            super.onDetachedFromWindow();
        }
    }
    
    private class GPSLocationListener implements LocationListener
    {

		@Override
		public void onLocationChanged(Location location) {
			if(location != null)
			{
				currentLocation = location;
				//Grabs the latitude and longitude to the updated location
				GeoPoint point = new GeoPoint(
						(int)(location.getLatitude()*1E6),
						(int)(location.getLongitude()*1E6));
				Log.i(TAG, "LAT: " + location.getLatitude() +  " LONG: " + location.getLongitude());
				
				progressDialog.dismiss();
				
				if(firstPass){
					String jsonStringBreweries = getBreweriesJson(point);
					Log.i(TAG, jsonStringBreweries);
					
					parseJsonObjects(jsonStringBreweries);
					firstPass = false;
				}
				
				for(Brewery brewery : breweryList)
				{
					//Calculate the direction in which the brewery lies from current location
					Double directionLat = brewery.getLocation().getLatitude() - currentLocation.getLatitude();
					Double directionLong = brewery.getLocation().getLongitude() - currentLocation.getLongitude();
					
					//Convert degrees to miles
					Double distanceLat = directionLat*68.71;
					Double distanceLong = directionLong*69.17;
					
					//Calculate the distance to a brewery in miles
					Double mileDistance = Math.sqrt(distanceLat*distanceLat + distanceLong*distanceLong);
					
					Log.i(TAG, "Distance to brewery in Miles: " + mileDistance);
					
					//calculate the distance in degrees
					Double degreeDistance = Math.sqrt(directionLat*directionLat + directionLong*directionLong);
					
					//make this a unit direction vector
					directionLat /= degreeDistance;
					directionLong /= degreeDistance;
					
					Log.i(TAG, "Directon LONG: " + directionLong + " Directon LAT: " + directionLat);
					Log.i(TAG, "Making sure this is a unit vector: " + Math.sqrt(directionLat*directionLat + directionLong*directionLong));
					
					//set direction vector
					brewery.setDirection(directionLat, directionLong);
					
					//set distance from current location in miles
					brewery.setDistance(mileDistance);
				}
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
				
				Location tmpLoc = new Location(LocationManager.GPS_PROVIDER);
				tmpLoc.setLatitude(latitude);
				tmpLoc.setLongitude(longitude);
				
				Brewery tmpBrew = new Brewery(name, tmpLoc, address);
				
				breweryList.add(tmpBrew);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
    
    private void goToMainScreen()
	{
		Intent back = new Intent(getApplicationContext(), ThePourGameActivity.class);
		startActivity(back);
		locationManager.removeUpdates(locationListener);
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}
	
	@Override
	public void onBackPressed()
	{
		goToMainScreen();
	}
}
