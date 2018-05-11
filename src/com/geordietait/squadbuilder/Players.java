package com.geordietait.squadbuilder;

import java.util.ArrayList;

/**
 * Class representing all of the players in a tournament
 * @author taitg
 *
 */
public class Players {

	private ArrayList<Player> players;
	
	/**
	 * Get the player list
	 * @return
	 */
	public ArrayList<Player> getList() {
		return players;
	}
	
	/**
	 * Get the number of players
	 * @return
	 */
	public int getNumber() {
		return players.size();
	}
}
