package com.geordietait.squadbuilder;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class representing an organization of the tournament
 * @author taitg
 *
 */
public class Tournament {

	private Players players;
	private int maxPlayers;
	private int numSquads;
	private ArrayList<Squad> squads;
	private ArrayList<Player> waitList;
	private double variance;
	
	/**
	 * Main constructor
	 * @param players
	 * @param numSquads
	 */
	public Tournament(Players players, int numSquads) {
		
		this.players = players;
		this.numSquads = numSquads;
		maxPlayers = players.getNumber() / numSquads;
		squads = new ArrayList<Squad>();
		waitList = new ArrayList<Player>();
		variance = -1.0;
		
		// initially populate waitlist with all players
		for (Player p : this.getPlayers()) {
			waitList.add(p);
		}
	}
	
	/**
	 * Constructor for copying an existing tournament object
	 * @param original
	 */
	public Tournament(Tournament original) {
		
		players = original.players;
		numSquads = original.numSquads;
		maxPlayers = original.maxPlayers;
		variance = -1.0;
		
		squads = new ArrayList<Squad>();
		for (Squad s : original.getSquads()) {
			squads.add(new Squad(s));
		}
		
		waitList = new ArrayList<Player>();
		for (Player p : original.getWaitList()) {
			waitList.add(p);
		}
	}
	
	/**
	 * Get the list of all players in the tournament
	 * @return
	 */
	public ArrayList<Player> getPlayers() {
		return players.getList();
	}
	
	/**
	 * Get the list of squads in the tournament
	 * @return
	 */
	public ArrayList<Squad> getSquads() {
		return squads;
	}
	
	/**
	 * Get the waitlist for the tournament (initially all players)
	 * @return
	 */
	public ArrayList<Player> getWaitList() {
		return waitList;
	}
	
	/**
	 * Reset the tournament
	 */
	public void reset() {
		squads.clear();
		waitList.clear();
		for (Player p : this.getPlayers()) {
			waitList.add(p);
		}
	}
	
	/**
	 * Fill the squads randomly with available players
	 */
	public void fillSquadsRandom() {
		
		// make a copy of player list and shuffle it
		ArrayList<Player> playersRandom = new ArrayList<Player>();
		for (Player p : getPlayers()) {
			playersRandom.add(p);
		}
		Collections.shuffle(playersRandom);
		
		// fill squads randomly
		int j = 0;
		for (int i = 0; i < numSquads; i++) {
			Squad s = new Squad();
			while (j < players.getNumber() && s.getSize() < maxPlayers) {
				Player p = playersRandom.get(j);
				s.addMember(p);
				waitList.remove(p);
				j++;
			}
			squads.add(s);
		}
		getVariance();
	}
	
	/**
	 * Calculate variance for a list of doubles
	 * @param values
	 * @return
	 */
	private static double calcVariance(ArrayList<Double> values) {
		
		// calculate mean
		double sum = 0.0;
		for (Double value : values) {
			sum += value;
		}
		double mean = sum / (double) values.size(); 
				
		// calculate variance
		double vSum = 0.0;
		for (Double value : values) {
			double diff = value - mean;
			vSum += diff * diff;
		}
		return vSum / ((double) values.size() - 1);
	}
	
	/**
	 * Get the variance in the skating averages of squads
	 * @return
	 */
	public double getSkatingVariance() {
		
		// extract values into a list
		ArrayList<Double> values = new ArrayList<Double>();
		for (Squad s : squads) {
			values.add(s.getSkatingAvg());
		}
		
		// get the variance of the values
		return calcVariance(values);
	}
	
	/**
	 * Get the variance in the shooting averages of squads
	 * @return
	 */
	public double getShootingVariance() {
		
		// extract values into a list
		ArrayList<Double> values = new ArrayList<Double>();
		for (Squad s : squads) {
			values.add(s.getShootingAvg());
		}
		
		// get the variance of the values
		return calcVariance(values);
	}
	
	/** 
	 * Get the variance in the checking averages of squads
	 * @return
	 */
	public double getCheckingVariance() {
		
		// extract values into a list
		ArrayList<Double> values = new ArrayList<Double>();
		for (Squad s : squads) {
			values.add(s.getCheckingAvg());
		}
		
		// get the variance of the values
		return calcVariance(values);
	}
	
	/**
	 * Get the sum of the variances for all ratings of squads
	 * @return
	 */
	public double getVariance() {
		if (variance < 0.0)
			return getSkatingVariance() + getShootingVariance() + getCheckingVariance();
		else
			return variance;
	}
	
	/**
	 * Get the maximum number of players per team
	 * @return
	 */
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	/**
	 * Print tournament values (for debugging)
	 */
	public void print() {
//		for (Squad s : squads) {
//			System.out.print("Squad: " + s.getSize() + " - ");
//			System.out.format("%.2f, ", s.getSkatingAvg());
//			System.out.format("%.2f, ", s.getShootingAvg());
//			System.out.format("%.2f\n", s.getCheckingAvg());
//		}
//		System.out.println("Waitlist: " + waitList.size());
//		System.out.format("\nVariances: %.2f, ", getSkatingVariance());
//		System.out.format("%.2f, ", getShootingVariance());
//		System.out.format("%.2f", getCheckingVariance());
		System.out.format("\nTotal: %.2f\n", getVariance());
	}
}
