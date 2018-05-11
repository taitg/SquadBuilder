package com.geordietait.squadbuilder;

import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import com.google.gson.Gson;

/**
 * Class for serving web requests
 * @author taitg
 *
 */
public class WebServer extends Thread {

	// flag for if the WebServer should continue the listening loop or halt
	private volatile boolean shutdown = false;
		
	// port for the WebServer to listen on
	private int port;
	
	Players players;
	Tournament tournament;

	/**
	 * Constructor for WebServer
	 * 
	 * @param port	Port number to listen on
	 */
	public WebServer(int port) {
		// initialization
		this.port = port;
	}
	
	/**
	 * Start server operations
	 */
	public void runServer() {
		
		System.out.println("Starting the SquadBuilder server on port " + port);
		
		// start the listening thread
		start();
		
		// read player data from JSON
		try {
			Gson gson = new Gson(); // TODO prepare for API
			players = gson.fromJson(new FileReader("players.json"), Players.class);
			tournament = new Tournament(players, 1);
		}
		catch (IOException e) { // TODO more descriptive
			System.err.println("IOException: " + e.toString());
		}
		
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
		public void run() { // TODO refactor
			boolean badRequest = false;
			boolean notFound = false;
			byte[] bytes = new byte[16384];
			String fileName = "";
			String output = "";
			String method = "";
			
			try {
				// read and parse HTTP request
				BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				OutputStream out = sock.getOutputStream();
				String request = in.readLine();
				
				if (request == null) return;
				
				// ensure request is well formed
				String[] reqSplit = request.split("/");
				if (reqSplit.length != 3) {
					badRequest = true;
				}
				else {
					// check the HTTP method TODO this is not used
					if (reqSplit[0].trim().equals("GET"))
						method = "GET";
					else if (reqSplit[0].trim().equals("POST")) {
						method = "POST";
					}
					else
						badRequest = true;
					
					// check if the request is well-formed
					String[] fileNameSplit = reqSplit[1].split(" ");
					if (fileNameSplit.length != 2) {
						badRequest = true;
					}
					else {
						if (!fileNameSplit[1].trim().equals("HTTP")) 
							badRequest = true;
						
						fileName = fileNameSplit[0];
						
						if (fileName.equals("") || fileName.equals("index.html") || fileName.startsWith("make?"))
							output = generateOutput(fileName);
					}
				}
				
				// determine if requested object exists
				File f = new File(fileName);
				if (!f.exists() && output.equals("")) {
					notFound = true;
				}
				
				// transmit content over existing connection
				String header = generateHeader(badRequest, notFound, f);
				out.write(header.getBytes("US-ASCII"));
				out.flush();
				
				// send file if OK
				if (!badRequest && !notFound && output.equals("")) {					
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
				else if (!badRequest && !output.equals("")) {
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
		 * @param badRequest	True if bad request
		 * @param notFound	True if file not found
		 * @param f	File object
		 * @return
		 */
		private String generateHeader(boolean badRequest, boolean notFound, File f) {
			String header = "HTTP/1.1 ";

			// response code
			if (badRequest) header += "400 Bad Request\r\n";
			else if (notFound) header += "404 Not Found\r\n";
			else header += "200 OK\r\n";
			
			// server name and version
			header += "Server: SquadBuilder/1.0\r\n";
			
			// file info
			if (!badRequest && !notFound && f.exists()) {
				SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss zzz");
				header += "Last-Modified: " + sdf.format(f.lastModified()) + "\r\n";
				header += "Content-Length: " + f.length() + "\r\n";
			}
			
			header += "Connection: close\r\n\r\n";
			return header;
		}
		
		/**
		 * Generate HTML output
		 * @param request
		 * @return
		 */
		private String generateOutput(String request) {
			System.out.println("Req: '"+request+"'");
			
			// begin html, set the title, make the top bar
			String htmlOut = generateTopHtml();
			
			// make the input and buttons
			htmlOut += generateFormHtml();
			
			// make the waitlist
			if (request.equals("")) {
				tournament = new Tournament(players, 1);
				htmlOut += generateWaitListHtml();
			}
			
			// make the squads if requested
			else if (request.startsWith("make")) {
				
				// parse desired number of squads and check for bad inputs
				String makeSplit[] = request.split("=");
				if (makeSplit.length < 2)
					return generateErrorHtml("You must enter a number.");
						
				int numSquads = 0;
				try {
					numSquads = Integer.valueOf(makeSplit[1]);
				}
				catch (NumberFormatException e) {
					return generateErrorHtml("You must enter a number.");
				}
				
				if (numSquads < 2 || numSquads > players.getNumber())
					return generateErrorHtml("Number of squads must be greater than 1 and less than the number of players (" + players.getNumber() + ").");
	
				// create the first generation of random individuals (tournaments)
				Population pop = new Population(players, numSquads, 500);
				
				// run the genetic algorithm for a set amount of milliseconds
				pop.evolve(2500);
				
				// pick the best individual
				tournament = pop.getIndividuals().get(0);
				
				// display the wait list
				htmlOut += generateWaitListHtml();
				
				// display the squads
				int count = 1;
				for (Squad s : tournament.getSquads()) {
					htmlOut += generateSquadHtml(count, s);
					count++;
				}
			}
			
			// make the closing tags
			htmlOut += generateEndHtml();
			return htmlOut;
		}

		/**
		 * Generate HTML for the title and the top bar
		 * @return
		 */
		private String generateTopHtml() { // TODO clean up
			String htmlOut = "<html><head><title>SquadBuilder</title></head>";
			htmlOut += "<body bgcolor=ddeeff><center>";
			htmlOut += "<link rel='stylesheet' href='https://www.w3schools.com/w3css/4/w3.css'>";
			htmlOut += "<div class='w3-card-4' style='width:100%'><div class='w3-display-container'>";
			htmlOut += "<img src='image.jpg' style='width:100%' alt='Hockey'>";
			htmlOut += "<div class='w3-display-bottommiddle w3-container w3-text-light-grey w3-wide w3-padding-64'>";
			htmlOut += "<h1 style='text-shadow:3px 3px 0 #444'><big><b>SquadBuilder</b></big></h1></div></div></div>";
			return htmlOut;
		}

		/**
		 * Generate HTML for controls
		 * @param htmlOut
		 * @return
		 */
		private String generateFormHtml() {
			
			String htmlOut = "<div class='w3-card-4 w3-round w3-margin w3-padding-16 w3-white' style='max-width: 50%'>";
			htmlOut += "<div class='w3-container w3-margin'>";
			htmlOut += "<form method='get' action='/make'>";
			htmlOut += "<div class='w3-panel w3-border-top w3-border-bottom w3-half'>";
			htmlOut += "<input type='text' name='squads' placeholder='How many squads?' ";
			htmlOut += "class='w3-input w3-border-0 w3-xlarge w3-round' style='width: 100%'></div>";
			htmlOut += "<input type='submit' value='Make' class='w3-button w3-white w3-large w3-half w3-round-large'>";
			htmlOut += "</form>";
			
			htmlOut += "<form method='post' action='/'>";
			htmlOut += "<div class='w3-container w3-half'></div>";
			htmlOut += "<input type='submit' value='Reset' class='w3-button w3-white w3-large w3-half w3-round-large'>";
			htmlOut += "</form></div></div>";
			return htmlOut;
		}

		/**
		 * Generate HTML for displaying a squad
		 * @param count
		 * @param s
		 * @return
		 */
		private String generateSquadHtml(int count, Squad s) {
			
			String htmlOut = "<div class='w3-card-4 w3-round w3-margin w3-padding-16 w3-white' style='max-width: 50%'>";
			htmlOut += "<h2>Squad " + count + "</h2>";
			htmlOut += "<table class='w3-table w3-centered w3-hoverable w3-striped' style='max-width: 100%'><tr>";
			htmlOut += "<td><b>Player</b></td>";
			htmlOut += "<td><b>Skating</b></td>";
			htmlOut += "<td><b>Shooting</b></td>";
			htmlOut += "<td><b>Checking</b></td></tr>";
			for (Player p : s.getMembers()) { // TODO sort alphabetical
				htmlOut += "<tr><td>" + p.getName() + "</td>";
				htmlOut += "<td>" + p.getSkatingRating() + "</td>";
				htmlOut += "<td>" + p.getShootingRating() + "</td>";
				htmlOut += "<td>" + p.getCheckingRating() + "</td></tr>";
			}
			htmlOut += "<tr><td><b>Average</b></td>";
			htmlOut += "<td><b>" + Math.round(s.getSkatingAvg()) + "</b></td>";
			htmlOut += "<td><b>" + Math.round(s.getShootingAvg()) + "</b></td>";
			htmlOut += "<td><b>" + Math.round(s.getCheckingAvg()) + "</b></td></tr>";
			htmlOut += "</table></div>";
			return htmlOut;
		}

		/**
		 * Generate HTML for displaying the waitlist
		 * @return
		 */
		private String generateWaitListHtml() {
			String htmlOut = "<div class='w3-card-4 w3-round w3-margin w3-padding-16 w3-white' style='max-width: 50%'>";
			htmlOut += "<h2>Wait list</h2>";
			if (tournament.getWaitList().size() > 0) {
				htmlOut += "<table class='w3-table w3-centered w3-hoverable w3-striped' style='max-width: 100%'><tr>";
				htmlOut += "<td><b>Player</b></td>";
				htmlOut += "<td><b>Skating</b></td>";
				htmlOut += "<td><b>Shooting</b></td>";
				htmlOut += "<td><b>Checking</b></td></tr>";
				for (Player p : tournament.getWaitList()) { // TODO sort alphabetical
					htmlOut += "<tr><td>" + p.getName() + "</td>";
					htmlOut += "<td>" + p.getSkatingRating() + "</td>";
					htmlOut += "<td>" + p.getShootingRating() + "</td>";
					htmlOut += "<td>" + p.getCheckingRating() + "</td></tr>";
				}
				htmlOut += "</table>";
			}
			else {
				htmlOut += "<i>Empty</i>";
			}
			htmlOut += "</div>";
			return htmlOut;
		}
		
		/**
		 * Generate HTML for displaying an error
		 * @param error
		 * @return
		 */
		private String generateErrorHtml(String error) {
			String htmlOut = generateTopHtml();
			htmlOut += generateFormHtml();
			htmlOut += "<div class='w3-panel w3-border w3-border-red w3-round' style='max-width: 50%'>";
			htmlOut += "<b>Error:</b> " + error + "</div>";
			tournament = new Tournament(players, 1);
			htmlOut += generateWaitListHtml();
			htmlOut += generateEndHtml();
			return htmlOut;
		}
		
		/**
		 * Generate closing tags
		 * @return
		 */
		private String generateEndHtml() {
			return "</center></body></html>";
		}
	}
}
