package org.android.pourgame;

import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.GestureOverlayView.OnGesturingListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;


public abstract class DrinkActivity implements OnGestureListener, SensorEventListener{

	protected abstract void animate(int anim1, int anim2);
	
	protected void up()
	{
		
	}
	
	protected void down()
	{
		
	}
	
	protected void right()
	{
		
	}
	
	protected void left()
	{
		
	}
}
