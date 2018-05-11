package com.geordietait.squadbuilder;

import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

/**
 * Class for serving web requests for SquadBuilder.
 * 
 * A main thread listens for connections and then spawns worker
 * threads to handle each one.
 * 
 * This is not implemented with current, modern practices, but it is
 * functional and this is how I learned. A simpler, easier-to-edit
 * implementation would make use of a library or framework for
 * connecting and sending HTML.
 * 
 * @author Geordie Tait
 *
 */
public class WebServer extends Thread {

	// flag for if the WebServer should continue the listening loop or halt
	private volatile boolean shutdown = false;
		
	// port for the WebServer to listen on
	private int port;
	
	// location of JSON data (filename or URL)
	private String jsonLocation;
	
	// players object containing the list of all players
	Players players;
	
	// the tournament arrangement to display to the user
	Tournament tournament;

	/**
	 * Constructor for WebServer
	 * 
	 * @param port	Port number to listen on
	 */
	public WebServer(int port, String jsonLocation) {
		this.port = port;
		this.jsonLocation = jsonLocation;
	}
	
	/**
	 * Start server operations
	 */
	public void runServer() {
		
		System.out.println("Starting the SquadBuilder server on port " + port);
		
		// start the listening thread
		start();
		
		// attempt to read player data from JSON
		// (can be entered as a filename or a URL)
		JsonReader json = new JsonReader(jsonLocation);
		players = json.getData();
		
		// check if player data is good
		if (players == null) {
			System.err.println("Could not read player data from JSON.");
			System.exit(-1);
		}
		
		if (players.checkPlayers() == false) {
			System.err.println("Invalid player data.");
			System.exit(-1);
		}
		
		// initialize tournament
		tournament = new Tournament(players, 1);
		
		System.out.println("Server started. Type \"quit\" to stop");
		System.out.println(".....................................");

		// watch for quit command
		Scanner keyboard = new Scanner(System.in);
		while (!keyboard.next().equals("quit"));
		System.out.println();
		
		// shut down the server
		shutdown();
		keyboard.close();
		System.out.println("Server stopped.\n");
	}
		
	/* Run method for main WebServer thread which listens for incoming requests/connections
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		
		ServerSocket serverSock = null;
		try {
			// open server socket
			serverSock = new ServerSocket(port);
			
			// set socket timeout option
			serverSock.setSoTimeout(1000);

			// main loop for the listening WebServer
			while (!shutdown) {
				try {
					// accept a new connection
					Socket clientSock = serverSock.accept();
					
					// create a worker thread to handle the new connection
					WebServerWorker worker = new WebServerWorker(clientSock);
					(new Thread(worker)).start();
				}
				catch (SocketTimeoutException e) {
					// do nothing, this is OK
					// allows the process to check the shutdown flag
				}
			}
			
			// clean up (close the socket)
			serverSock.close();
		}
		catch (IOException e) {
			System.err.println("Main thread exception: " + e.toString());
		}
	}
	
	/**
	 * Signals the WebServer to shut down
	 */
	public void shutdown() {
		shutdown = true;
	}
	
	/**
	 * Class for worker threads which serve requests
	 * 
	 * @author Geordie Tait
	 *
	 */
	public class WebServerWorker implements Runnable {
		
		// socket for worker threads to connect through
		private Socket sock = null;
		
		/**
		 * Constructor for worker threads
		 * 
		 * @param sock	Socket to serve client requests over
		 */
		public WebServerWorker(Socket sock) {
			this.sock = sock;
		}

		/* Run method for worker threads which serve requests
		 * 
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			boolean isBadRequest = false;
			boolean isNotFound = false;
			byte[] bytes = new byte[16384];
			String fileName = "";
			String output = "";
			
			try {
				// read and parse HTTP request
				BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				OutputStream out = sock.getOutputStream();
				String request = in.readLine();
				if (request == null) return;
				
				// ensure request is well formed
				String[] reqSplit = request.split("/");
				if (reqSplit.length != 3) {
					isBadRequest = true;
				}
				else {
					// check the HTTP method
					if (!reqSplit[0].trim().equals("GET") && !reqSplit[0].trim().equals("POST"))
						isBadRequest = true;
					
					// check if the request is well-formed
					String[] fileNameSplit = reqSplit[1].split(" ");
					if (fileNameSplit.length != 2) {
						isBadRequest = true;
					}
					else {
						if (!fileNameSplit[1].trim().equals("HTTP")) 
							isBadRequest = true;
						
						fileName = fileNameSplit[0];
						
						// if request is valid and not a file, generate HTML to output
						if (fileName.equals("") || fileName.equals("index.html") || fileName.startsWith("make?"))
							output = generateOutput(fileName);
					}
				}
				
				// determine if requested object exists, unless we are transmitting HTML
				File f = new File(fileName);
				if (!f.exists() && output.equals(""))
					isNotFound = true;
				
				// transmit content over existing connection
				String header = generateHTTPHeader(isBadRequest, isNotFound, f);
				out.write(header.getBytes("US-ASCII"));
				out.flush();
				
				// send file if OK and applicable
				if (!isBadRequest && !isNotFound && output.equals("")) {					
					FileInputStream fStream = new FileInputStream(f);
		            BufferedInputStream fBuffer = new BufferedInputStream(fStream);
		            int n;
		            
		            while ((n = fBuffer.read(bytes)) > 0) {
		                out.write(bytes, 0, n);
		                out.flush();
		            }
		            
		            fBuffer.close();
		            fStream.close();
				}
				
				// otherwise send output string if OK
				else if (!isBadRequest && !output.equals("")) {
					out.write(output.getBytes("US-ASCII"));
					out.flush();
				}
				
				// close connection
				in.close();
				out.close();
				sock.close();
			}
			catch (IOException e) {
				System.err.println("Worker thread exception: " + e.toString());
			}
		}
		
		/**
		 * Generate HTTP response header
		 * 
		 * @param isBadRequest	True if bad request
		 * @param isNotFound	True if file not found
		 * @param f	File object
		 * @return HTTP header string
		 */
		private String generateHTTPHeader(boolean isBadRequest, boolean isNotFound, File f) {
			String header = "HTTP/1.1 ";

			// response code
			if (isBadRequest) header += "400 Bad Request\r\n";
			else if (isNotFound) header += "404 Not Found\r\n";
			else header += "200 OK\r\n";
			
			// server name and version
			header += "Server: SquadBuilder/1.0\r\n";
			
			// file info if applicable
			if (!isBadRequest && !isNotFound && f.exists()) {
				SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss zzz");
				header += "Last-Modified: " + sdf.format(f.lastModified()) + "\r\n";
				header += "Content-Length: " + f.length() + "\r\n";
			}
			
			header += "Connection: close\r\n\r\n";
			return header;
		}
		
		/**
		 * Generate HTML output
		 * @param request Requested content from URL
		 * @return HTML as a string
		 */
		private String generateOutput(String request) {
			HtmlGenerator html = new HtmlGenerator(tournament, players);
			
			// begin html, set the title, make the top bar
			String htmlOut = html.generateTop();
			
			// make the input and buttons
			htmlOut += html.generateForm();
			
			// display the waitlist with all the players in it
			if (request.equals("") || request.equals("index.html")) {
				tournament = new Tournament(players, 1);
				html.setTournament(tournament);
				htmlOut += html.generateWaitList();
			}
			
			// display the waitlist and squads if requested
			else if (request.startsWith("make")) {
				
				// parse desired number of squads and check for bad inputs
				String makeSplit[] = request.split("=");
				if (makeSplit.length < 2)
					return html.generateError("You must enter a number.");
						
				int numSquads = 0;
				try {
					numSquads = Integer.valueOf(makeSplit[1]);
				}
				catch (NumberFormatException e) {
					return html.generateError("You must enter a number.");
				}
				
				if (numSquads < 2 || numSquads > players.getNumber())
					return html.generateError("Number of squads must be greater than 1 and less than the number of players (" + players.getNumber() + ").");
	
				// create the first generation of random individuals (tournaments)
				Population pop = new Population(players, numSquads, 500);
				
				// run the genetic algorithm for a set amount of milliseconds
				pop.evolve(2500);
				
				// pick the best individual
				tournament = pop.getIndividuals().get(0);
				html.setTournament(tournament);
				
				// display the wait list
				htmlOut += html.generateWaitList();
				
				// display the squads
				int count = 1;
				for (Squad s : tournament.getSquads()) {
					htmlOut += html.generateSquad(count, s);
					count++;
				}
			}
			
			// make the closing tags
			htmlOut += html.generateEnd();
			return htmlOut;
		}

		
	}
}
