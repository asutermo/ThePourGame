package org.android.pourgame.breweryfinder;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapOverlay extends Overlay {
	private GeoPoint locationPoint;
	Resources res;
	int iconID;
	
	public MapOverlay(Resources res, int iconID)
	{
		this.res = res;
		this.iconID = iconID;
	}

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
		
		Drawable icon = res.getDrawable(iconID);
		
		canvas.drawBitmap(((BitmapDrawable)icon).getBitmap(), screenPxs.x, screenPxs.y-12, null);
		return true;
	}
}
