package org.android.pourgame;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.List;

public class OrientationManager {
	private static Sensor sensor;
    private static SensorManager sensorManager;
    private static OrientationListener listener;
 
    private static Boolean supported;
    private static boolean running = false;
 
    enum Side {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT;
    }
 
    public static boolean isListening() {
        return running;
    }
 
    public static void stopListening() {
        running = false;
        try {
            if (sensorManager != null && sensorEventListener != null) {
                sensorManager.unregisterListener(sensorEventListener);
            }
        } catch (Exception e) {}
    }
 
    public static boolean isSupported() {
        if (supported == null) {
            if ( ThePourGameActivity.getContext() != null) {
                sensorManager = (SensorManager)  ThePourGameActivity.getContext()
                        .getSystemService(Context.SENSOR_SERVICE);
                List<Sensor> sensors = sensorManager.getSensorList(
                        Sensor.TYPE_ORIENTATION);
                supported = new Boolean(sensors.size() > 0);
            } else {
                supported = Boolean.FALSE;
            }
        }
        return supported;
    }

    public static void startListening(
            OrientationListener orientationListener) {
        sensorManager = (SensorManager) ThePourGameActivity.getContext()
                .getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(
                Sensor.TYPE_ORIENTATION);
        if (sensors.size() > 0) {
            sensor = sensors.get(0);
            running = sensorManager.registerListener(
                    sensorEventListener, sensor, 
                    SensorManager.SENSOR_DELAY_NORMAL);
            listener = orientationListener;
        }
    }
 
    private static SensorEventListener sensorEventListener = new SensorEventListener() 
    {

        private Side currentSide = null;
        private Side oldSide = null;
        private float azimuth;
        private float pitch;
        private float roll;
 
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
 
        public void onSensorChanged(SensorEvent event) {
 
            azimuth = event.values[0];     // azimuth
            pitch = event.values[1];     // pitch
            roll = event.values[2];        // roll
 
            if (pitch < -45 && pitch > -135) {
                // top side up
                currentSide = Side.TOP;
            } else if (pitch > 45 && pitch < 135) {
                // bottom side up
                currentSide = Side.BOTTOM;
            } else if (roll > 45) {
                // right side up
                currentSide = Side.RIGHT;
            } else if (roll < -45) {
                // left side up
                currentSide = Side.LEFT;
            }
 
            if (currentSide != null && !currentSide.equals(oldSide)) {
                switch (currentSide) {
                    case TOP : 
                        listener.onTopUp();
                        break;
                    case BOTTOM : 
                        listener.onBottomUp();
                        break;
                    case LEFT: 
                        listener.onLeftUp();
                        break;
                    case RIGHT: 
                        listener.onRightUp();
                        break;
                }
                oldSide = currentSide;
            }
 
            // forwards orientation to the OrientationListener
            listener.onOrientationChanged(azimuth, pitch, roll);
        }
 
    };
 
}
