package com.geordietait.squadbuilder;

import java.util.ArrayList;

/**
 * Class representing all of the players in a tournament
 * @author Geordie Tait
 *
 */
public class Players {

	// the list of all players in the tournament
	private ArrayList<Player> playerList;
	
	/**
	 * Basic constructor
	 */
	public Players() {
		playerList = new ArrayList<Player>();
	}
	
	/**
	 * Get the player list
	 * @return List of players
	 */
	public ArrayList<Player> getList() {
		return playerList;
	}
	
	/**
	 * Get the number of players
	 * @return Number of players
	 */
	public int getNumber() {
		return playerList.size();
	}
	
	/**
	 * Check if players are well-formed
	 * @return True if players are well-formed
	 */
	public boolean checkPlayers() {
		
		for (Player p : playerList) {
			
			// check if any player has no name(s)
			if (p.getFirstName().isEmpty() && p.getLastName().isEmpty()) {
				System.err.println("Player detected with no name");
				return false;
			}
			
			// check if any player has a invalid skating rating
			else if (p.getSkatingRating() < 0) {
				System.err.println("Player detected with invalid skating rating ("
						+ p.getName() + ")");
				return false;
			}
			
			// check if any player has a invalid shooting rating
			else if (p.getShootingRating() < 0) {
				System.err.println("Player detected with invalid shooting rating ("
						+ p.getName() + ")");
				return false;
			}
			
			// check if any player has a invalid checking rating
			else if (p.getCheckingRating() < 0) {
				System.err.println("Player detected with invalid checking rating ("
						+ p.getName() + ")");
				return false;
			}
		}
		return true;
	}
}
