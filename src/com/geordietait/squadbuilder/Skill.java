package com.geordietait.squadbuilder;

/**
 * Class representing the skills for a player
 * @author Geordie Tait
 *
 */
public class Skill {
	
	// the type of the skill
	private String type;
	
	// the player's rating in the skill
	private int rating;
	
	/**
	 * Get the type of skill
	 * @return Skill type string
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Get the rating in the particular skill
	 * @return Skill rating number
	 */
	public int getRating() {
		return rating;
	}
}
