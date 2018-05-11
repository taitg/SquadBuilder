package com.geordietait.squadbuilder;

import java.util.ArrayList;

/**
 * Class for objects representing players
 * @author taitg
 *
 */
public class Player {

	private String _id;
	private String lastName;
	private String firstName;
	private ArrayList<Skill> skills;
	
	/**
	 * Get the player ID
	 * @return
	 */
	public String getID() {
		return _id;
	}
	
	/**
	 * Get the player's first name
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Get the player's last name
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Get the player's whole name
	 * @return
	 */
	public String getName() {
		return firstName + " " + lastName;
	}
	
	/**
	 * Get the player's skating rating
	 * @return
	 */
	public int getSkatingRating() {
		int result = 0;
		for (Skill s : skills) {
			if (s.getType().equals("Skating"))
				result = s.getRating();
		}
		return result;
	}
	
	/**
	 * Get the player's shooting rating
	 * @return
	 */
	public int getShootingRating() {
		int result = 0;
		for (Skill s : skills) {
			if (s.getType().equals("Shooting"))
				result = s.getRating();
		}
		return result;
	}
	
	/**
	 * Get the player's checking rating
	 * @return
	 */
	public int getCheckingRating() {
		int result = 0;
		for (Skill s : skills) {
			if (s.getType().equals("Checking"))
				result = s.getRating();
		}
		return result;
	}
}
