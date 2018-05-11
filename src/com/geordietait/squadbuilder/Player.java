package com.geordietait.squadbuilder;

import java.util.ArrayList;

/**
 * Class for objects representing players
 * @author Geordie Tait
 *
 */
public class Player {

	// the player ID string
	private String _id;
	
	// the player's last name
	private String lastName;
	
	// the player's first name
	private String firstName;
	
	// list of the player's skills
	private ArrayList<Skill> skills;
	
	/**
	 * Get the player ID
	 * @return Player ID string
	 */
	public String getID() {
		return _id;
	}
	
	/**
	 * Get the player's first name
	 * @return Player first name
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Get the player's last name
	 * @return Player last name
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Get the player's whole name
	 * @return Player name
	 */
	public String getName() {
		return firstName + " " + lastName;
	}
	
	/**
	 * Get the player's skating rating
	 * @return Player skating rating
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
	 * @return Player shooting rating
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
	 * @return Player checking rating
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
