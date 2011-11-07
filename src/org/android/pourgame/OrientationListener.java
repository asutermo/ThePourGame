package org.android.pourgame;

public interface OrientationListener 
{
	public void onOrientationChanged(float azimuth, float pitch, float roll);
    public void onTopUp();
    public void onBottomUp();
    public void onRightUp();
    public void onLeftUp();
 
}
