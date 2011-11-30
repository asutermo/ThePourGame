package org.android.pourgame.breweryfinder;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;

import com.google.android.maps.GeoPoint;

public class Brewery{
	private String name;
	private Location point;
	private String address;
	private double distance;
	private List<Double> direction;
	
	public Brewery(String name, Location location, String address)
	{
		this.name = name;
		this.address = address;
		this.point = location;
		direction = new ArrayList<Double>();
	}

	public String getName() {
		return name;
	}

	public Location getLocation() {
		return point;
	}

	public String getAddress() {
		return address;
	}
	
	public double getDistance()
	{
		return distance;
	}
	
	public void setDistance(double distance)
	{
		this.distance = distance;
	}
	
	public void setDirection(Double latitude, Double longitude)
	{
		direction.clear();
		direction.add(latitude);
		direction.add(longitude);
	}
	
	public List<Double> getDirection()
	{
		return direction;
	}
}
