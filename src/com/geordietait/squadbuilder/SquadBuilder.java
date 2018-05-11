package com.geordietait.squadbuilder;

/**
 * 
 * @author taitg
 *
 */
public class SquadBuilder {

	/**
	 * Main program function (entry point)
	 * @param args
	 */
	public static void main(String[] args) {

		// start the web server
		WebServer server = new WebServer(8083); // TODO cmd line args
		server.runServer();
		
	}

}
