package com.geordietait.squadbuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;

/**
 * Class for reading player data from JSON
 * @author Geordie Tait
 *
 */
public class JsonReader {
	
	// where to read JSON from (either a filename or a URL)
	private String location;
	
	/**
	 * Constructor for JsonReader
	 * @param location Where to read JSON from (either a filename or a URL)
	 */
	public JsonReader(String location) {
		this.location = location;
	}
	
	/**
	 * Deserializes player data from JSON into Players object.
	 * Attempts to read location as a filename first, then as a URL if that fails
	 * @return Players object containing player data
	 */
	public Players getData() {
		Players players = null;
		
		// try to read JSON data from file
		File f = new File(location);
		if (f.exists()) {
			try {
				Gson gson = new Gson();
				players = gson.fromJson(new FileReader(location), Players.class);
			}
			catch (IOException e) {
				System.err.println("Error: Could not read JSON data from file.");
				System.exit(-1);
			}
		}
		
		// try to read JSON data from URL
		else {
			try {
				String content = getJsonFromUrl(location);
				if (content != null) {
					Gson gson = new Gson();
					players = gson.fromJson(content, Players.class);
				}
			} 
			catch (IOException e) {
				System.err.println("Error: Could not read JSON data from URL.");
				System.exit(-1);
			}
		}
		return players;
	}
	
	/**
	 * Retrieves JSON data from a given URL
	 * @param location URL where JSON data is located
	 * @return JSON data as a string
	 * @throws IOException
	 */
	private static String getJsonFromUrl(String location) throws IOException {

		// try to create connection
		URL url = new URL(location);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// get response code
		int response = conn.getResponseCode();
		
		// if OK, begin reading
		if (response == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			StringBuffer result = new StringBuffer();

			while ((line = in.readLine()) != null)
				result.append(line);
			
			// return the result
			in.close();
			return result.toString();
		}
		return null;
	}

}
