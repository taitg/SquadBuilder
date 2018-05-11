package com.geordietait.squadbuilder;

/**
 * Class for generating HTML strings for the SquadBuilder WebServer to send out
 * @author Geordie Tait
 *
 */
public class HtmlGenerator {
	
	// the tournament arrangement to display to the user
	private Tournament tournament;
	
	// the players object containing the list of all players
	private Players players;
	
	/**
	 * Constructor for HtmlGenerator
	 * @param tournament Tournament to display
	 * @param players Players object
	 */
	public HtmlGenerator(Tournament tournament, Players players) {
		this.tournament = tournament;
		this.players = players;
	}
	
	/**
	 * Set the tournament object
	 * @param tournament Tournament to display
	 */
	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	/**
	 * Generate HTML for the title and the top bar
	 * @return HTML string for initialization, title, and top bar
	 */
	public String generateTop() {
		
		// title
		String htmlOut = "<html><head><title>SquadBuilder</title>";
		
		// font
		htmlOut += "<style>.w3-myfont {font-family: 'Verdana', sans-serif;}</style></head>";
		
		// background color
		htmlOut += "<body style='background: linear-gradient(rgba(208,222,239,0), "
				+ "rgba(188, 215, 256,1))'><center>";
		
		// stylesheet
		htmlOut += "<link rel='stylesheet' href='w3.css'>";
		
		// container for top bar
		htmlOut += "<div class='w3-card-4' style='width:100%'><div class='w3-display-container'>";
		
		// image
		htmlOut += "<img src='topbar.jpg' style='width:100%' alt='Topbar'>";
		
		// container for title text
		htmlOut += "<div class='w3-display-bottommiddle w3-container "
				+ "w3-text-light-grey w3-wide w3-padding-64'>";
		
		// title text
		htmlOut += "<h1 style='text-shadow:3px 3px 0 #444' class='w3-jumbo w3-myfont'>"
				+ "<big><b>SquadBuilder</b></big></h1></div></div></div>";
		return htmlOut;
	}

	/**
	 * Generate HTML for controls
	 * @return HTML string for buttons and input
	 */
	public String generateForm() {
		
		// containers for forms
		String htmlOut = "<div class='w3-card-4 w3-round w3-margin "
				+ "w3-padding-16 w3-white' style='max-width: 50%'>";
		htmlOut += "<div class='w3-container w3-margin'>";
		
		// form for text box and make button
		htmlOut += "<form method='get' action='/make'>";
		
		// container for text box
		htmlOut += "<div class='w3-panel w3-border-top w3-border-bottom w3-half'>";
		
		// text box
		htmlOut += "<input type='number' name='squads' placeholder='How many squads?' ";
		htmlOut += "class='w3-input w3-border-0 w3-xlarge w3-round w3-myfont' "
				+ "style='width: 100%'></div>";
		
		// make button
		htmlOut += "<input type='submit' value='Make' class='w3-button "
				+ "w3-white w3-large w3-half w3-round-large w3-myfont'>";
		htmlOut += "</form>";
		
		// form for reset button
		htmlOut += "<form method='post' action='/'>";
		
		// reset button
		htmlOut += "<input type='submit' value='Reset' class='w3-button "
				+ "w3-white w3-large w3-half w3-round-large w3-myfont'>";
		htmlOut += "</form></div></div>";
		return htmlOut;
	}

	/**
	 * Generate HTML for displaying a squad
	 * @param count Squad number
	 * @param s Squad to display
	 * @return HTML string for a squad
	 */
	public String generateSquad(int count, Squad s) {
		
		// container for displaying a squad
		String htmlOut = "<div class='w3-card-4 w3-round w3-margin w3-myfont "
				+ "w3-padding-16 w3-white' style='max-width: 50%'>";
		
		// heading
		htmlOut += "<h2 class='w3-myfont'>Squad " + count + "</h2>";
		
		// table
		htmlOut += "<table class='w3-table w3-centered w3-hoverable "
				+ "w3-striped' style='max-width: 100%'><tr>";
		
		// column headings
		htmlOut += "<td><b>Player</b></td>";
		htmlOut += "<td><b>Skating</b></td>";
		htmlOut += "<td><b>Shooting</b></td>";
		htmlOut += "<td><b>Checking</b></td></tr>";
		
		// data
		for (Player p : s.getMembers()) { // TODO sort alphabetical
			htmlOut += "<tr><td>" + p.getName() + "</td>";
			htmlOut += "<td>" + p.getSkatingRating() + "</td>";
			htmlOut += "<td>" + p.getShootingRating() + "</td>";
			htmlOut += "<td>" + p.getCheckingRating() + "</td></tr>";
		}
		
		// averages
		htmlOut += "<tr><td><b>Average</b></td>";
		htmlOut += "<td><b>" + Math.round(s.getSkatingAvg()) + "</b></td>";
		htmlOut += "<td><b>" + Math.round(s.getShootingAvg()) + "</b></td>";
		htmlOut += "<td><b>" + Math.round(s.getCheckingAvg()) + "</b></td></tr>";
		htmlOut += "</table></div>";
		return htmlOut;
	}

	/**
	 * Generate HTML for displaying the waitlist
	 * @return HTML string for the waitlist
	 */
	public String generateWaitList() {
		
		// container for displaying waitlist
		String htmlOut = "<div class='w3-card-4 w3-round w3-margin w3-myfont "
				+ "w3-padding-16 w3-white' style='max-width: 50%'>";
		
		// heading
		htmlOut += "<h2 class='w3-myfont'>Wait list</h2>";
		
		// table if waitlist is not empty
		if (tournament.getWaitList().size() > 0) {
			htmlOut += "<table class='w3-table w3-centered w3-hoverable "
					+ "w3-striped' style='max-width: 100%'><tr>";
			
			// column headings
			htmlOut += "<td><b>Player</b></td>";
			htmlOut += "<td><b>Skating</b></td>";
			htmlOut += "<td><b>Shooting</b></td>";
			htmlOut += "<td><b>Checking</b></td></tr>";
			
			// data
			for (Player p : tournament.getWaitList()) { // TODO sort alphabetical
				htmlOut += "<tr><td>" + p.getName() + "</td>";
				htmlOut += "<td>" + p.getSkatingRating() + "</td>";
				htmlOut += "<td>" + p.getShootingRating() + "</td>";
				htmlOut += "<td>" + p.getCheckingRating() + "</td></tr>";
			}
			htmlOut += "</table>";
		}
		else {
			// no players on the wait list
			htmlOut += "<i>Empty</i>";
		}
		htmlOut += "</div>";
		return htmlOut;
	}
	
	/**
	 * Generate HTML for displaying an error
	 * @param error Error text to display
	 * @return HTML string for an error page
	 */
	public String generateError(String error) {
		
		// make top and form
		String htmlOut = generateTop();
		htmlOut += generateForm();
		
		// container for displaying errors
		htmlOut += "<div class='w3-panel w3-border w3-border-red w3-round w3-myfont' style='max-width: 50%'>";
		
		// error message
		htmlOut += "<b>Error:</b> " + error + "</div>";
		
		// reset the tournament
		tournament = new Tournament(players, 1);
		
		// make waitlist and closing tags
		htmlOut += generateWaitList();
		htmlOut += generateEnd();
		return htmlOut;
	}
	
	/**
	 * Generate closing tags
	 * @return HTML string for closing tags
	 */
	public String generateEnd() {
		return "</center></body></html>";
	}
}
