package org.android.pourgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Toast;


public abstract class DrinkActivity extends Activity implements OnGestureListener, SensorEventListener{

	protected static final int SWIPE_MIN = 120;
	protected static final int SWIPE_THRESH_VEL = 200;
	protected static final float SLOW_POUR = 0.005f;
	protected static final float PERFECT_POUR = 0.01f;
	protected static final float FAST_POUR = 0.02f;
	protected GestureDetector gestureDetector;
	protected SensorManager sensor;
	protected enum GameStatus {GAMEOVER, POURING, STARTING};
	protected GameStatus status;
	protected FluidView view;
	protected static Context CONTEXT;
	
	protected void animate(int anim1, int anim2){
		overridePendingTransition(anim1, anim2);
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	
	@Override
    protected void onResume() {
        super.onResume();
    }
    
    @Override
    protected void onPause() {
    	killSensor();
    	super.onPause();
    }
    
    @Override
    protected void onDestroy() {
        killSensor();
    	super.onDestroy();
    }
    
	protected void killSensor() {
		Log.d("sensor", "Killing \"orientation sensor\"");
		if (sensor != null)
			sensor.unregisterListener(this, sensor.getDefaultSensor(Sensor.TYPE_ORIENTATION));
	    sensor = null;  
	}
	
	protected void transition(Intent i) {
		Log.d("transition", "Transitioning to new screen");
		startActivity(i);
		killSensor();
		finish();
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
