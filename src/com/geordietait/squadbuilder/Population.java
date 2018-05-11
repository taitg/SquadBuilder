package com.geordietait.squadbuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * Class for genetic optimization algorithm and associated methods/data.
 * 
 * The algorithm creates a population of randomly assigned squads, given
 * a desired number of squads, then creates successive generations for a
 * given time period before returning the best result found. In each
 * generation, only a proportion of individuals with the best fitness
 * survive (calculated using the variance in skating, shooting, and checking 
 * ratings between the squads). These individuals reproduce with mutations,
 * implemented as random swaps between the squads or the waitlist. A number
 * of random individuals are also added each generation.
 * 
 * @author Geordie Tait
 *
 */
public class Population {

	// the list of individuals in the population
	private ArrayList<Tournament> individuals;
	
	// the maximum number of individuals in a generation
	private int maxPopulation;
	
	// the players object containing the list of all players
	private Players players;
	
	// the desired number of squads
	private int numSquads;
	
	/**
	 * Constructor for Population
	 * @param players Players object
	 * @param numSquads Desired number of squads
	 * @param maxPop Maximum population per generation
	 */
	public Population(Players players, int numSquads, int maxPop) {
		
		// initialize fields
		individuals = new ArrayList<Tournament>();
		maxPopulation = maxPop;
		this.players = players;
		this.numSquads = numSquads;
		
		// fill each tournament with randomized squad assignments
		for (int i = 0; i < maxPop; i++) {
			Tournament t = new Tournament(players, numSquads);
			t.fillSquadsRandom();
			individuals.add(t);
		}
		
		// sort the tournaments by fitness (variance)
		sortByFitness();
	}
	
	/**
	 * Get the list of tournament objects being evolved
	 * @return List of individuals (tournaments)
	 */
	public ArrayList<Tournament> getIndividuals() {
		return individuals;
	}
	
	/**
	 * Comparator class for sorting tournaments by fitness (variance)
	 * @author Geordie Tait
	 *
	 */
	private class FitnessComparator implements Comparator<Tournament> {
		@Override
		public int compare(Tournament a, Tournament b) {
			return a.getVariance() < b.getVariance() ? -1 : a.getVariance() == b.getVariance() ? 0 : 1;
		}
	}
	
	/**
	 * Sort tournaments by fitness (variance)
	 */
	public void sortByFitness() {
		Collections.sort(individuals, new FitnessComparator());
	}
	
	/**
	 * Produce the next generation of individuals
	 */
	public void nextGeneration() {
		
		// proportion of individuals which will survive each generation
		double survivalRate = 0.1;
		int numSurvivors = (int) (maxPopulation * survivalRate);
		
		// cull the lowest-fitness individuals
		individuals.subList(numSurvivors, maxPopulation).clear();
		
		// reproduce each of the survivors with mutations (single parent reproduction)
		for (int i = 0; i < numSurvivors; i++) {
			
			// determine the number of children per survivor
			// this is set so that the second generation will have:
			//	- [survivalrate] survivors
			//	- [survivalrate] new random individuals
			//	- the rest are new mutant children (up to maxPopulation)
			int numChildren = (int) ((1 - survivalRate*2) / survivalRate);
			
			// create the children
			for (int j = 0; j < numChildren; j++) {
				
				// create a new individual
				Tournament t = new Tournament(individuals.get(i));
				
				// mutate (swap players) between random squads
				mutateSquads(t);
				
				// mutate (swap players) between a random squad and the waitlist
				mutateSquadWithWaitlist(t);
				
				individuals.add(t);
			}
		}
		
		// add some new random individuals to fill out the population
		while (individuals.size() < maxPopulation) {
			Tournament t = new Tournament(players, numSquads);
			t.fillSquadsRandom();
			individuals.add(t);
		}
		
		// sort the tournaments by fitness (variance)
		sortByFitness();
	}

	/**
	 * Perform mutation (player swaps) between squads
	 * @param t Tournament to mutate
	 */
	private void mutateSquads(Tournament t) {
		Random rand = new Random();
		
		// mutate at least once, sometimes randomly twice
		for (int i = 0; i < 1+rand.nextInt(2); i++) {
			
			// pick 2 random distinct squads
			Squad s1 = t.getSquads().get(rand.nextInt(numSquads));
			Squad s2 = null;
			while (s2 == null || s2 == s1)
				s2 = t.getSquads().get(rand.nextInt(numSquads));
			
			// pick 2 random distinct players from those squads
			Player p1 = s1.getMembers().get(rand.nextInt(t.getMaxPlayers()));
			Player p2 = null;
			while (p2 == null || p2 == p1)
				p2 = s2.getMembers().get(rand.nextInt(t.getMaxPlayers()));
			
			// swap the players
			s1.removeMember(p1);
			s1.addMember(p2);
			s2.removeMember(p2);
			s2.addMember(p1);
		}
	}
	
	/**
	 * Perform mutation (player swaps) between a squad and the waitlist
	 * @param t Tournament to mutate
	 */
	private void mutateSquadWithWaitlist(Tournament t) {
		if (t.getWaitList().size() == 0) return;
		Random rand = new Random();
		
		// mutate 0 times or 1, randomly
		for (int i = 0; i < rand.nextInt(2); i++) {
			
			// pick a random squad
			Squad s = t.getSquads().get(rand.nextInt(numSquads-1));
			
			// pick a random player from the squad and one from the waitlist
			Player p1 = s.getMembers().get(rand.nextInt(t.getMaxPlayers()));
			Player p2 = t.getWaitList().get(rand.nextInt(t.getWaitList().size()));
			
			// swap the players
			s.removeMember(p1);
			s.addMember(p2);
			t.getWaitList().remove(p2);
			t.getWaitList().add(p1);
		}
	}
	
	/**
	 * Run the genetic algorithm for a set amount of time
	 * @param milliseconds How many ms to run for
	 */
	public void evolve(int milliseconds) {
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < milliseconds)
			nextGeneration();
	}
}
