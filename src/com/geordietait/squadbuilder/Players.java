package com.geordietait.squadbuilder;

import java.util.ArrayList;

/**
 * Class representing all of the players in a tournament
 * @author Geordie Tait
 *
 */
public class Players {

	// the list of all players in the tournament
	private ArrayList<Player> players;
	
	/**
	 * Get the player list
	 * @return List of players
	 */
	public ArrayList<Player> getList() {
		return players;
	}
	
	/**
	 * Get the number of players
	 * @return Number of players
	 */
	public int getNumber() {
		return players.size();
	}
}
