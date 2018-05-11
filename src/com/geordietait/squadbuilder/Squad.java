package com.geordietait.squadbuilder;

import java.util.ArrayList;

/**
 * Class representing a squad of players
 * @author Geordie Tait
 *
 */
public class Squad {

	// the list of players in the squad
	private ArrayList<Player> members;
	
	/**
	 * Main constructor
	 */
	public Squad() {
		members = new ArrayList<Player>();
	}
	
	/**
	 * Constructor for copying a squad
	 * @param original Squad to make a copy of
	 */
	public Squad(Squad original) {
		members = new ArrayList<Player>();
		for (Player p : original.getMembers()) {
			members.add(p);
		}
	}
	
	/**
	 * Get the list of member players
	 * @return List of players in squad
	 */
	public ArrayList<Player> getMembers() {
		return members;
	}
	
	/**
	 * Add a player to the squad
	 * @param p Player to add
	 */
	public void addMember(Player p) {
		members.add(p);
	}
	
	/**
	 * Remove a player from the squad
	 * @param p Player to remove
	 */
	public void removeMember(Player p) {
		members.remove(p);
	}
	
	/**
	 * Get the size of the squad
	 * @return Number of players in squad
	 */
	public int getSize() {
		return members.size();
	}
	
	/**
	 * Get the total skating rating of the squad
	 * @return Sum of squad skating ratings
	 */
	private int getSkatingTotal() {
		int result = 0;
		for (Player p : members) {
			result += p.getSkatingRating();
		}
		return result;
	}
	
	/**
	 * Get the average skating rating of the squad
	 * @return Average of squad skating ratings
	 */
	public double getSkatingAvg() {
		return (double) this.getSkatingTotal() / (double) this.getSize();
	}
	
	/**
	 * Get the total shooting rating of the squad
	 * @return Sum of squad shooting ratings
	 */
	private int getShootingTotal() {
		int result = 0;
		for (Player p : members) {
			result += p.getShootingRating();
		}
		return result;
	}
	
	/**
	 * Get the average shooting rating of the squad
	 * @return Average of squad shooting ratings
	 */
	public double getShootingAvg() {
		return (double) this.getShootingTotal() / (double) this.getSize();
	}
	
	/**
	 * Get the total checking rating of the squad
	 * @return Sum of squad checking ratings
	 */
	private int getCheckingTotal() {
		int result = 0;
		for (Player p : members) {
			result += p.getCheckingRating();
		}
		return result;
	}
	
	/**
	 * Get the average checking rating of the squad
	 * @return Average of squad checking ratings
	 */
	public double getCheckingAvg() {
		return (double) this.getCheckingTotal() / (double) this.getSize();
	}
}
