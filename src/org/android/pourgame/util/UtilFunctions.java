package org.android.pourgame.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.android.pourgame.breweryfinder.Brewery;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class UtilFunctions {
	public static final String TAG = "UtilFunctions";
	
	public static GeoPoint locationToPoint(Location l)
	{
		return new GeoPoint((int)(l.getLatitude()*1E6), (int)(l.getLongitude()*1E6));
	}
	
	public static Location pointToLocation(GeoPoint g)
	{
		Location l = new Location(LocationManager.GPS_PROVIDER);
		l.setLatitude((double)g.getLatitudeE6()/1E6);
		l.setLongitude((double)g.getLongitudeE6()/1E6);
		return l;
	}
	
	public static String getBreweriesJson(Location location, String places_key)
	{
		StringBuilder sb = new StringBuilder();
		
		try {
			//Builds the URL that we will use to communicate with google
			URI uri = new URI("https://maps.googleapis.com/maps/api/place/search/json?" +
					"location=" + location.getLatitude() + "," + location.getLongitude() + "&" +
					"radius=10000&" +
					"name=brewery&" +
					"sensor=true&" +
					"key=" + places_key);
			Log.i(TAG, uri.toString());
			
			//HTTP client that will talk to the server through the URI
			HttpClient client = new DefaultHttpClient();
			
			//Creates a GET request for the HTTP server
			HttpGet request = new HttpGet();
			
			//Tells the request the URI that it is supposed to communcate with
			request.setURI(uri);
			
			//Captures the response from the HTTP server
			HttpResponse response = client.execute(request);
			
			//Directs the resposne through an input string
			InputStream ips = response.getEntity().getContent();
			
			//Reads the input stream through a buffered reader until there is nothing left to read
			BufferedReader buf = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
			
			String s;
			while(true)
			{
				s = buf.readLine();
				if(s == null)
				{
					break;
				}
				sb.append(s);
			}
			
			//close streams
			buf.close();
			ips.close();
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Return the string builder
		return sb.toString();
	}
	
	public static List<Brewery> parseJsonObjects(String jsonStringBreweries)
    {
		List<Brewery> breweryList = new ArrayList<Brewery>();
    	try {
    		//creates a JSON Object assuming that the string passed to it is of a valid syntax
			JSONObject jsonBreweries = new JSONObject(jsonStringBreweries);
			//Creates an array of JSON objects for each individual result
			JSONArray jsonArrayBreweries = jsonBreweries.getJSONArray("results");
			
			//Extracts the necessary data such as latitude, longitude, address, and name of each result and stores it
			//in a Brewery object that resides in a global brewery list
			for(int i = 0; i < jsonArrayBreweries.length(); i++)
			{
				JSONObject brewery = jsonArrayBreweries.getJSONObject(i);
				Double longitude = brewery.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
				Double latitude = brewery.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
				String address = brewery.getString("vicinity");
				String name = brewery.getString("name");
				
				Log.i(TAG, "Found Name: " + name + " Addresss: " + address + " Lat: " + latitude + " Long: " + longitude);
				
				Location tmpLoc = new Location(LocationManager.GPS_PROVIDER);
				tmpLoc.setLatitude(latitude);
				tmpLoc.setLongitude(longitude);
				
				breweryList.add(new Brewery(name, tmpLoc, address));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return breweryList;
    }
}
