package com.geordietait.squadbuilder;

/**
 * Class for generating the HTML strings for the server to send out
 * @author taitg
 *
 */
public class HtmlGenerator {
	
	Tournament tournament;
	Players players;
	
	/**
	 * Constructor
	 * @param tournament
	 * @param players
	 */
	public HtmlGenerator(Tournament tournament, Players players) {
		this.tournament = tournament;
		this.players = players;
	}

	/**
	 * Generate HTML for the title and the top bar
	 * @return
	 */
	public String generateTop() { // TODO clean up
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
	public String generateForm() {
		
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
	public String generateSquad(int count, Squad s) {
		
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
	public String generateWaitList() {
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
	public String generateError(String error) {
		String htmlOut = generateTop();
		htmlOut += generateForm();
		htmlOut += "<div class='w3-panel w3-border w3-border-red w3-round' style='max-width: 50%'>";
		htmlOut += "<b>Error:</b> " + error + "</div>";
		tournament = new Tournament(players, 1);
		htmlOut += generateWaitList();
		htmlOut += generateEnd();
		return htmlOut;
	}
	
	/**
	 * Generate closing tags
	 * @return
	 */
	public String generateEnd() {
		return "</center></body></html>";
	}
}
