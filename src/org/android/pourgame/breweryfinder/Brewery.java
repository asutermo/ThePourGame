package org.android.pourgame.breweryfinder;

import com.google.android.maps.GeoPoint;

public class Brewery{
	private String name;
	private GeoPoint point;
	private String address;
	
	public Brewery(String name, GeoPoint location, String address)
	{
		this.name = name;
		this.address = address;
		this.point = location;
	}

	public String getName() {
		return name;
	}

	public GeoPoint getLocation() {
		return point;
	}

	public String getAddress() {
		return address;
	}
	
	
}
