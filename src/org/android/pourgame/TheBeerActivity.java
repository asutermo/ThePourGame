package org.android.pourgame;

import java.util.ArrayList;

import org.android.pourgame.breweryfinder.BreweryFinderActivity;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
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


public class TheBeerActivity extends DrinkActivity implements OnGesturePerformedListener, OnGestureListener, SensorEventListener 
{
	protected static Context CONTEXT;
	private GestureLibrary mLibrary;
	private boolean gestureEngaged;
	private GestureOverlayView gestures;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beer);
        CONTEXT = this;
        gestureDetector = new GestureDetector(this, this);
        Log.d("Beer Game", "Beer Game Created");
        BeerView view = new BeerView(CONTEXT);
		view.setRenderer(new FluidRenderer());
        
        //initiate accelerometer
        sensor = (SensorManager)getSystemService(SENSOR_SERVICE);
        boolean orientation = sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
        //if no sensor, undo it, warn user
        if (!orientation)
        {
        	Toast.makeText(this, "Sensor invalid", Toast.LENGTH_SHORT).show();
			
        	sensor.unregisterListener(this, sensor.getDefaultSensor(Sensor.TYPE_ORIENTATION));
        }
        
        gestureEngaged = false;
        
        
    }
	public void onButtonClick(View view)
	{
		gestureEngaged = !gestureEngaged;
		if (gestureEngaged)
		{
			
			setContentView(R.layout.beergesture);
			mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
			gestures = (GestureOverlayView) findViewById(R.id.gestures);
			gestures.addOnGesturePerformedListener(this);
			if (!mLibrary.load()) {
	        	finish();
	        }
		}
		else
		{
			setContentView(R.layout.beer);
			gestures = null;
		}

	}
	
	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = mLibrary.recognize(gesture);

		// We want at least one prediction
		if (predictions.size() > 0) {
			Prediction prediction = predictions.get(0);
			// We want at least some confidence in the result
			if (prediction.score > 1.0) {
				Intent gest = null;
				if (prediction.name.equals("Champagne"))
				{
					Log.d("onGesture-Champagne Game", "Measured gesture");
					gest = new Intent(getApplicationContext(), TheChampagneActivity.class);
				}
				else if (prediction.name.equals("Map"))
				{
					Log.d("onGesture-Map", "Measured gesture");
					gest = new Intent(getApplicationContext(), BreweryFinderActivity.class);
				}
				else if (prediction.name.equals("Soda"))
				{
					Log.d("onGesture-Soda Game", "Measured gesture");
					gest = new Intent(getApplicationContext(), TheSodaGameActivity.class);
				}
				else if (prediction.name.equals("Home"))
				{
					Log.d("onGesture-Home", "Measured gesture");
					gest = new Intent(getApplicationContext(), ThePourGameActivity.class);
					
				}
				else
				{
					Log.d("onGesture-Home", "Measured gesture");
					gest = new Intent(getApplicationContext(), TheBeerActivity.class);
				}
				transition(gest);
				overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
			}
		}
	}
	
	@Override 
    public boolean onTouchEvent(MotionEvent me){ 
      this.gestureDetector.onTouchEvent(me);
     return super.onTouchEvent(me); 
    }

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		
		Log.d("onFling-Beer Game", "Measured fling");
		//from bottom to up
		if (e1.getY() - e2.getY() > SWIPE_MIN && Math.abs(velocityY) > SWIPE_THRESH_VEL)
		{
			Log.d("sodaGame", "Loading soda pouring game");
			Intent up = new Intent(getApplicationContext(), TheSodaGameActivity.class);
			transition(up);
			animate(R.anim.push_up_in, R.anim.push_up_out);
			return true;
		}
		
		//from top to bottom
		if (e2.getY()-e1.getY() > SWIPE_MIN && Math.abs(velocityY) > SWIPE_THRESH_VEL)
		{
			Log.d("champagneGame", "Loading champagne pouring game");
			Intent down = new Intent(getApplicationContext(), TheChampagneActivity.class);
			transition(down);
			animate(R.anim.push_bottom_in, R.anim.push_bottom_out);
			return true;
		}
        // Swipe from left to right.
        // The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE) and a certain velocity (SWIPE_THRESHOLD_VELOCITY).
        if (e2.getX() - e1.getX() > SWIPE_MIN
                && Math.abs(velocityX) > SWIPE_THRESH_VEL) {
            Log.d("Main Screen", "Loading main screen");
    		Intent previous = new Intent(getApplicationContext(), ThePourGameActivity.class);
    		transition(previous);
    		animate(R.anim.push_left_in, R.anim.push_left_out);
            return true;
        }

        return false;
	}
	
	@Override
	public void onBackPressed() {
		Log.d("Main Screen", "Loading main screen");
		Intent back = new Intent(getApplicationContext(), ThePourGameActivity.class);
		transition(back);
		animate(R.anim.push_left_in, R.anim.push_left_out);
	}
	
	public Context getContext() {
		return CONTEXT;
	}

	

	@Override
	public void onSensorChanged(SensorEvent event) {
		Log.d("Sensor", "Sensor Type: " + event.sensor.getType());
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
		{
			float roll = event.values[2];
			Log.d("Roll: ", "" + roll);
			if (roll > 35 && roll < 55)
				Log.d("Readout: ", "Good pour!");
			else if (roll > 55)
				Log.d("Readout: ", "Too much!");
			else if (roll < 35)
				Log.d("Readout: ", "Too little!");
			
		}
	}
}
