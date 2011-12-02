package org.android.pourgame;

import org.android.pourgame.breweryfinder.BreweryFinderActivity;
import org.android.pourgame.compass.BeerdarView;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class ThePourGameActivity extends Activity implements OnGestureListener, SensorEventListener {
    
	
	private static Context CONTEXT;
	
	//set up gesture constants, these are used for shake and swipe/fling
	private static final int SWIPE_MIN = 75;
	private static final int SWIPE_MAX_OFF = 250;
	private static final int SWIPE_THRESH_VEL = 200;
	private static final int SHAKE = 800;
	private static final int UPDATE = 100;
	private static final int SHAKE_EXIT = 7;
	
	//detect current gesture
	private GestureDetector gestureDetector;
	
	//handle shaking with acceleration. 
	private SensorManager sensor;
	private long last = -1;
	private float xData, xLast;
	private float yData, yLast;
	private float zData, zLast;
	private int shakeCount;
	private PaintCoasterView paintCoasterView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//main screen, set layout
        super.onCreate(savedInstanceState);
        paintCoasterView = new PaintCoasterView(this);
        setContentView(paintCoasterView);
        //setContentView(R.layout.main);
        CONTEXT = this;
        shakeCount = 0;
        gestureDetector = new GestureDetector(this, this);
        
        //initiate accelerometer
        sensor = (SensorManager)getSystemService(SENSOR_SERVICE);
        boolean accelerometer = sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        //if no sensor, undo it, warn user
        if (!accelerometer)
        {
        	Toast.makeText(this, "Sensor invalid", Toast.LENGTH_SHORT).show();
			
        	sensor.unregisterListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        }
        
        //imgView = (ImageView)findViewById(R.id.ImageView01);
        
        //imgView.setImageResource(R.drawable.logo);
        
    }
    
    @Override 
    public boolean onTouchEvent(MotionEvent me){ 
     this.gestureDetector.onTouchEvent(me);
     return super.onTouchEvent(me); 
    }

    @Override
    protected void onResume() {
    	shakeCount = 0;
        super.onResume();
    }
    
    @Override
    protected void onPause() {
    	paintCoasterView.setVisibility(View.GONE);
    	killSensor();
    	super.onPause();
    }
    
    @Override
    protected void onDestroy() {
    	paintCoasterView.setVisibility(View.GONE);
        killSensor();
    	super.onDestroy();
 
    }

	public static Context getContext() {
		return CONTEXT;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// Check movement along the Y-axis. If it exceeds SWIPE_MAX_OFF_PATH, then dismiss the swipe.
        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF)
            return false;

        // Swipe from right to left.
        // The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE) and a certain velocity (SWIPE_THRESHOLD_VELOCITY).
        if (e1.getX() - e2.getX() > SWIPE_MIN
                && Math.abs(velocityX) > SWIPE_THRESH_VEL) {
            left();
            return true;
        }

        // Swipe from left to right.
        // The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE) and a certain velocity (SWIPE_THRESHOLD_VELOCITY).
        if (e2.getX() - e1.getX() > SWIPE_MIN
                && Math.abs(velocityX) > SWIPE_THRESH_VEL) {
            right();
            return true;
        }

        return false;
	}

	public void left()
	{
		Log.d("beerGame", "Loading beer pouring game");
		//paintCoasterView.setVisibility(View.GONE);
		killSensor();
		Intent previous = new Intent(getApplicationContext(), TheBeerActivity.class);
		startActivity(previous);
		finish();

		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}
	
	public void right()
	{
		Log.d("breweryFinder", "Loading brewery finder");
		Intent next = new Intent(getApplicationContext(), BreweryFinderActivity.class);
		//paintCoasterView.setVisibility(View.GONE);
		killSensor();
		startActivity(next);
		finish();

		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	
	public void up()
	{
		Log.d("Compass", "Loading brewery finder");
		Intent up = new Intent(getApplicationContext(), BeerdarView.class);
		//paintCoasterView.setVisibility(View.GONE);
		killSensor();
		startActivity(up);
		finish();
		
		overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
	}
	
	public void down()
	{
		Log.d("Gestures", "Loading gesture helper");
		Intent down = new Intent(getApplicationContext(), GestureActivity.class);
		//paintCoasterView.setVisibility(View.GONE);
		killSensor();
		startActivity(down);
		finish();
		
		overridePendingTransition(R.anim.push_bottom_in, R.anim.push_bottom_out);
	}
	
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBackPressed() {
		paintCoasterView.setVisibility(View.GONE);
		killSensor();
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	private void killSensor() 
	{
		Log.d("sensor", "Killing \"shake sensor\"");
		if (sensor != null)
			sensor.unregisterListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
	    sensor = null;  
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		Log.d("sensor", "onSensorChanged: " + event.sensor.getType());
		//compare what sensor made the call
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
		{
			long inTime = System.currentTimeMillis();
			
			if ((inTime-last) > UPDATE)
			{
				long diffTime = inTime - last;
				last = inTime;
				
				//get data
				xData = event.values[SensorManager.DATA_X];
				yData = event.values[SensorManager.DATA_Y];
				zData = event.values[SensorManager.DATA_Z];
				
				//calculate speed
				float speed = Math.abs(xData + yData + zData - xLast - yLast - zLast) / diffTime * 10000;
				
				if (speed > SHAKE)
				{
					shakeCount++;
					
					if (shakeCount > SHAKE_EXIT)
						this.onBackPressed();
				}
				
				
				xLast = xData;
				yLast = yData;
				zLast = zData;
			}
		}
	}
}