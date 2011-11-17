package org.android.pourgame;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.GestureOverlayView.OnGesturingListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;


public abstract class DrinkActivity extends Activity implements OnGestureListener, SensorEventListener{

	protected void animate(int anim1, int anim2){
		overridePendingTransition(anim1, anim2);
	}
	protected abstract void killSensor();
	
	protected void up(){}
	
	protected void down(){}
	
	protected void right(){}
	
	protected void left(){}
	
}
