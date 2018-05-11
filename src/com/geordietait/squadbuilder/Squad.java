package com.geordietait.squadbuilder;

import java.util.ArrayList;

/**
 * Class representing a squad of players
 * @author taitg
 *
 */
public class Squad {

	private ArrayList<Player> members;
	
	/**
	 * Main constructor
	 */
	public Squad() {
		members = new ArrayList<Player>();
	}
	
	/**
	 * Constructor for copying a squad
	 * @param original
	 */
	public Squad(Squad original) {
		members = new ArrayList<Player>();
		for (Player p : original.getMembers()) {
			members.add(p);
		}
	}
	
	/**
	 * Get the list of member players
	 * @return
	 */
	public ArrayList<Player> getMembers() {
		return members;
	}
	
	/**
	 * Add a player to the squad
	 * @param p
	 */
	public void addMember(Player p) {
		members.add(p);
	}
	
	/**
	 * Remove a player from the squad
	 * @param p
	 */
	public void removeMember(Player p) {
		members.remove(p);
	}
	
	/**
	 * Get the size of the squad
	 * @return
	 */
	public int getSize() {
		return members.size();
	}
	
	/**
	 * Get the total skating rating of the squad
	 * @return
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
	 * @return
	 */
	public double getSkatingAvg() {
		return (double) this.getSkatingTotal() / (double) this.getSize();
	}
	
	/**
	 * Get the total shooting rating of the squad
	 * @return
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
	 * @return
	 */
	public double getShootingAvg() {
		return (double) this.getShootingTotal() / (double) this.getSize();
	}
	
	/**
	 * Get the total checking rating of the squad
	 * @return
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
	 * @return
	 */
	public double getCheckingAvg() {
		return (double) this.getCheckingTotal() / (double) this.getSize();
	}
}
