package com.geordietait.squadbuilder;

/**
 * Main class for the SquadBuilder program
 * @author Geordie Tait
 *
 */
public class SquadBuilder {

	/**
	 * Main program function (entry point)
	 * @param args Program arguments array
	 */
	public static void main(String[] args) {
		
		// check command line arguments
		if (args.length != 0 && args.length > 2) 
			usage();
		
		// check if given a valid port, otherwise use default 8080
		int port = 8092;
		if (args.length == 1 || args.length == 2) {
			try {
				port = Integer.valueOf(args[0]);
			}
			catch (NumberFormatException e) {
				usage();
			}
		}
		
		// check if given a valid JSON location, otherwise use default
		String jsonLocation = "players.json";
		if (args.length == 3 && !args[1].isEmpty()) {
			jsonLocation = args[1];
		}

		// start the web server
		WebServer server = new WebServer(port, jsonLocation);
		server.runServer();
	}
	
	/**
	 * Print usage statement
	 */
	private static void usage() {
		System.err.println("Usage: squadbuilder [port] [JSON_location]");
		System.exit(-1);
	}

}
