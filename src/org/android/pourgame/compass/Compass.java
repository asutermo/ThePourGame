package org.android.pourgame.compass;

import java.util.ArrayList;
import java.util.List;

import org.android.pourgame.R;
import org.android.pourgame.ThePourGameActivity;
import org.android.pourgame.breweryfinder.Brewery;
import org.android.pourgame.util.UtilFunctions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
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
	private Location currentLocation;
	private static boolean firstPass;
	private static boolean locationChanged;

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
        locationChanged = false;
        
        breweryList = new ArrayList<Brewery>();
        
        places_key = getResources().getString(R.string.places_api_key);
        
        //Grabs the location service from the android system
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        //Initializes the location listener with listener defined below
        locationListener = new CompassLocationListener();
        
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
    }

    @Override
    protected void onStop()
    {
        if (debug) 
        	Log.d(TAG, "onStop");
        super.onStop();
        mSensorManager.unregisterListener(mListener);
        locationManager.removeUpdates(locationListener);
    }
    
    @Override
    protected void onPause()
    {
    	if(debug)
    		Log.d(TAG, "onPause");
    	locationManager.removeUpdates(locationListener);
    	super.onPause();
    	
    }

    private class CompassLayoutView extends View {
        private Paint   compassPaint = new Paint();
        private Path    compassPath = new Path();
        private Path breweriesPath = new Path();
        private Paint breweriesPaint = new Paint();
        private Paint nearestBreweryPaint = new Paint();
        private Path nearestBreweryPath = new Path();
        private Paint infoPaint = new Paint();
        private boolean mAnimate;
        private boolean initialized;
        private Brewery nearestBrewery;
        double nearestBrewMappedX;
        double nearestBrewMappedY;
        
        Integer w;
        Integer circleRadius;

        public CompassLayoutView(Context context) {
            super(context);
            
            initialized = false;
        }

        @Override 
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.BLACK);
            w = canvas.getWidth();
//            h = canvas.getHeight();
            
            circleRadius = w/2-50;
            
            if(!initialized){
	            compassPath.moveTo(0, -circleRadius);
	            compassPath.lineTo(0, circleRadius);
	            compassPath.moveTo(-circleRadius, 0);
	            compassPath.lineTo(circleRadius, 0);
	            compassPath.addCircle(0, 0, circleRadius, Direction.CW);
	            compassPath.addCircle(0, 0, circleRadius/2, Direction.CW);
	            initialized = true;
            }
            
            if(breweryList != null && breweryList.size() > 0 && locationChanged)
            {
            	breweriesPath = new Path();
            	nearestBreweryPath = new Path();
            	Double minDist = null;
	            for(Brewery brew : breweryList)
	            {
	            	double distanceTrans;
	            	if(brew.getDistance() < .5)
	            	{
	            		distanceTrans = brew.getDistance()*2*circleRadius;
	            	}else{
	            		distanceTrans = circleRadius;
	            	}
	            	List<Double> dirList = brew.getDirection();
	            	double mappedX = dirList.get(1)*distanceTrans;
	            	double mappedY = -dirList.get(0)*distanceTrans;
	            	
	            	breweriesPath.addCircle((float)mappedX,(float)mappedY, 7, Direction.CW);
	            	
	            	if(minDist == null || brew.getDistance() < minDist)
	            	{
	            		minDist = brew.getDistance();
	            		nearestBrewery = brew;
	            		nearestBrewMappedX = mappedX;
	            		nearestBrewMappedY = mappedY;
	            	}
	            }
	            
	            nearestBreweryPath.addCircle((float)nearestBrewMappedX, (float)nearestBrewMappedY, 10, Direction.CW);
	            locationChanged = false;
            }

            compassPaint.setAntiAlias(true);
            compassPaint.setColor(Color.rgb(76, 122, 255));
            compassPaint.setStyle(Paint.Style.STROKE);
            compassPaint.setTextSize(20);
            compassPaint.setStrokeWidth(3);
            
            nearestBreweryPaint.setColor(Color.RED);
            nearestBreweryPaint.setAntiAlias(true);
            nearestBreweryPaint.setStyle(Paint.Style.FILL);
            
            breweriesPaint.setAntiAlias(true);
            breweriesPaint.setColor(Color.GREEN);
            breweriesPaint.setStyle(Paint.Style.FILL);
            
            infoPaint.setAntiAlias(true);
            infoPaint.setColor(Color.WHITE);
            infoPaint.setTextSize(40);

            
            int cx = w / 2;
            int cy = circleRadius+30;
            
            canvas.drawText("Heading: " + (int)mValues[0], 10, circleRadius*2 + 100, infoPaint);
            canvas.drawText("Nearest Brewery:", 10, circleRadius*2 + 150, infoPaint);
            if(nearestBrewery != null)
            {
            	canvas.drawText(nearestBrewery.getName(), 10, circleRadius*2+200, infoPaint);
            	canvas.drawText("Distance: " + nearestBrewery.getDistance().toString().substring(0, 4) + "mi", 10, circleRadius*2 + 250, infoPaint);
            }

            canvas.translate(cx, cy);
            
            
            if (mValues != null) {
                canvas.rotate(-mValues[0]);
            }
            canvas.drawPath(compassPath, compassPaint);
            canvas.drawPath(breweriesPath, breweriesPaint);
            canvas.drawPath(nearestBreweryPath, nearestBreweryPaint);
            canvas.drawText("N", -10, -circleRadius - 20, compassPaint);
            
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
    
    private class CompassLocationListener implements LocationListener
    {

		@Override
		public void onLocationChanged(Location location) {
			if(location != null)
			{
				currentLocation = location;
				//Grabs the latitude and longitude to the updated location
				Log.i(TAG, "LAT: " + location.getLatitude() +  " LONG: " + location.getLongitude());
				
				progressDialog.dismiss();
				
				if(firstPass){
					String jsonStringBreweries = UtilFunctions.getBreweriesJson(location, places_key);
					Log.i(TAG, jsonStringBreweries);
					
					breweryList = UtilFunctions.parseJsonObjects(jsonStringBreweries);
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
					
					//set direction vector
					brewery.setDirection(directionLat, directionLong);
					
					//set distance from current location in miles
					brewery.setDistance(mileDistance);
				}
				
				locationChanged = true;
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
