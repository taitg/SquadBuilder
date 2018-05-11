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
		if (args.length > 2) 
			usage();
		
		// check if given a port, otherwise use default 8080
		int port = 8080;
		if (args.length == 2) {
			try {
				port = Integer.valueOf(args[1]);
			}
			catch (NumberFormatException e) {
				usage();
			}
		}

		// start the web server
		WebServer server = new WebServer(port);
		server.runServer();
		
	}
	
	/**
	 * Print usage statement
	 */
	private static void usage() {
		System.err.println("Usage: squadbuilder [port]");
		System.exit(-1);
	}

}
