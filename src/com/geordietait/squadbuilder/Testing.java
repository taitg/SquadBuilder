package com.geordietait.squadbuilder;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

/**
 * JUnit test class for SquadBuilder
 * @author Geordie Tait
 *
 */
class Testing {

	/**
	 * Test the Players class
	 */
	@Test
	void testPlayers() {
		
		// prepare skill objects
		Skill skatingGood = new Skill("Skating", 1);
		Skill shootingGood = new Skill("Shooting", 1);
		Skill checkingGood = new Skill("Checking", 1);
		ArrayList<Skill> skillsGood = new ArrayList<Skill>();
		skillsGood.add(skatingGood);
		skillsGood.add(shootingGood);
		skillsGood.add(checkingGood);
		
		Skill skatingBad = new Skill("Skating", -1);
		Skill shootingBad = new Skill("Shooting", -1);
		Skill checkingBad = new Skill("Checking", -1);
		ArrayList<Skill> skillsBad = new ArrayList<Skill>();
		skillsBad.add(skatingBad);
		skillsBad.add(shootingBad);
		skillsBad.add(checkingBad);
		
		ArrayList<Skill> skillsMissing = new ArrayList<Skill>();
		skillsMissing.add(skatingGood);
		
		// positive test
		Player pGood = new Player("1234", "Tait", "Geordie", skillsGood);
		Players psGood = new Players();
		psGood.getList().add(pGood);
		assertTrue(psGood.checkPlayers());
		
		// negative test (no name)
		Player pBad1 = new Player("1234", "", "", skillsGood);
		Players psBad1 = new Players();
		psBad1.getList().add(pBad1);
		assertFalse(psBad1.checkPlayers());
		
		// negative test (negative skill ratings)
		Player pBad2 = new Player("1234", "Tait", "Geordie", skillsBad);
		Players psBad2 = new Players();
		psBad2.getList().add(pBad2);
		assertFalse(psBad2.checkPlayers());
	
		// negative test (missing skill ratings)
		Player pBad3 = new Player("1234", "Tait", "Geordie", skillsMissing);
		Players psBad3 = new Players();
		psBad3.getList().add(pBad3);
		assertFalse(psBad3.checkPlayers());
	}

	/**
	 * Test the Squad class
	 */
	@Test
	void testSquad() {
		
		// prepare skill objects
		Skill skating1 = new Skill("Skating", 1);
		Skill shooting1 = new Skill("Shooting", 3);
		Skill checking1 = new Skill("Checking", 5);
		ArrayList<Skill> skills1 = new ArrayList<Skill>();
		skills1.add(skating1);
		skills1.add(shooting1);
		skills1.add(checking1);
		
		Skill skating2 = new Skill("Skating", 3);
		Skill shooting2 = new Skill("Shooting", 5);
		Skill checking2 = new Skill("Checking", 7);
		ArrayList<Skill> skills2 = new ArrayList<Skill>();
		skills2.add(skating2);
		skills2.add(shooting2);
		skills2.add(checking2);
		
		// prepare player objects
		Player p1 = new Player("1234", "Nietzsche", "Friedrich", skills1);
		Player p2 = new Player("4321", "Tesla", "Nikola", skills2);
		
		// prepare squad object
		Squad s = new Squad();
		s.addMember(p1);
		s.addMember(p2);
		
		// test skating average
		assertEquals(2, s.getSkatingAvg());
		
		// test shooting average
		assertEquals(4, s.getShootingAvg());
		
		// test checking average
		assertEquals(6, s.getCheckingAvg());
	}
	
	/**
	 * Test the Tournament class
	 */
	@Test
	void testTournament() {
		
		// prepare skill objects
		Skill skating1 = new Skill("Skating", 1);
		Skill shooting1 = new Skill("Shooting", 3);
		Skill checking1 = new Skill("Checking", 5);
		ArrayList<Skill> skills1 = new ArrayList<Skill>();
		skills1.add(skating1);
		skills1.add(shooting1);
		skills1.add(checking1);
		
		Skill skating2 = new Skill("Skating", 3);
		Skill shooting2 = new Skill("Shooting", 5);
		Skill checking2 = new Skill("Checking", 7);
		ArrayList<Skill> skills2 = new ArrayList<Skill>();
		skills2.add(skating2);
		skills2.add(shooting2);
		skills2.add(checking2);
		
		Skill skating3 = new Skill("Skating", 2);
		Skill shooting3 = new Skill("Shooting", 4);
		Skill checking3 = new Skill("Checking", 6);
		ArrayList<Skill> skills3 = new ArrayList<Skill>();
		skills3.add(skating3);
		skills3.add(shooting3);
		skills3.add(checking3);
		
		Skill skating4 = new Skill("Skating", 4);
		Skill shooting4 = new Skill("Shooting", 6);
		Skill checking4 = new Skill("Checking", 8);
		ArrayList<Skill> skills4 = new ArrayList<Skill>();
		skills4.add(skating4);
		skills4.add(shooting4);
		skills4.add(checking4);
		
		// prepare player objects
		Player p1 = new Player("1234", "Bush", "George", skills1);
		Player p2 = new Player("2345", "Edison", "Thomas", skills2);
		Player p3 = new Player("3456", "Walkin", "Christopher", skills3);
		Player p4 = new Player("4567", "Man", "Bat", skills4);
		Players ps = new Players();
		ps.getList().add(p1);
		ps.getList().add(p2);
		ps.getList().add(p3);
		ps.getList().add(p4);
		
		// prepare squad objects
		Squad s1 = new Squad();
		s1.addMember(p1);
		s1.addMember(p2);
		Squad s2 = new Squad();
		s2.addMember(p3);
		s2.addMember(p4);
		
		// prepare tournament object
		Tournament t = new Tournament(ps, 2);
		t.getSquads().add(s1);
		t.getSquads().add(s2);
		
		// test skating variance
		assertEquals(0.5, t.getSkatingVariance());
		
		// test skating variance
		assertEquals(0.5, t.getShootingVariance());
		
		// test skating variance
		assertEquals(0.5, t.getCheckingVariance());
		
		// test total variance
		assertEquals(1.5, t.getVariance());
	}
	
	/**
	 * Test the Population class
	 */
	@Test
	void testPopulation() {
		
		// prepare skill objects
		Skill skating1 = new Skill("Skating", 1);
		Skill shooting1 = new Skill("Shooting", 3);
		Skill checking1 = new Skill("Checking", 5);
		ArrayList<Skill> skills1 = new ArrayList<Skill>();
		skills1.add(skating1);
		skills1.add(shooting1);
		skills1.add(checking1);
		
		Skill skating2 = new Skill("Skating", 3);
		Skill shooting2 = new Skill("Shooting", 5);
		Skill checking2 = new Skill("Checking", 7);
		ArrayList<Skill> skills2 = new ArrayList<Skill>();
		skills2.add(skating2);
		skills2.add(shooting2);
		skills2.add(checking2);
		
		Skill skating3 = new Skill("Skating", 2);
		Skill shooting3 = new Skill("Shooting", 4);
		Skill checking3 = new Skill("Checking", 6);
		ArrayList<Skill> skills3 = new ArrayList<Skill>();
		skills3.add(skating3);
		skills3.add(shooting3);
		skills3.add(checking3);
		
		Skill skating4 = new Skill("Skating", 4);
		Skill shooting4 = new Skill("Shooting", 6);
		Skill checking4 = new Skill("Checking", 8);
		ArrayList<Skill> skills4 = new ArrayList<Skill>();
		skills4.add(skating4);
		skills4.add(shooting4);
		skills4.add(checking4);
		
		// prepare player objects
		Player p1 = new Player("1234", "America", "Captain", skills1);
		Player p2 = new Player("2345", "Bear", "Yogi", skills2);
		Player p3 = new Player("3456", "Gandhi", "Mahatma", skills3);
		Player p4 = new Player("4567", "Gautama", "Siddhartha", skills4);
		Players ps = new Players();
		ps.getList().add(p1);
		ps.getList().add(p2);
		ps.getList().add(p3);
		ps.getList().add(p4);
		
		// prepare population object
		Population pop = new Population(ps, 3, 100);
		
		// save a list of the initial variances
		ArrayList<Double> initialVariances = new ArrayList<Double>();
		for (Tournament t : pop.getIndividuals()) 
			initialVariances.add(t.getVariance());
		
		// test sorting by fitness
		boolean firstIsLowest1 = pop.getIndividuals().get(0).getVariance() <= pop.getIndividuals().get(99).getVariance();
		assertTrue(firstIsLowest1);
		
		// test population size
		assertEquals(100, pop.getIndividuals().size());
		
		// test wait list size
		assertEquals(1, pop.getIndividuals().get(0).getWaitList().size());
		
		// test number of squads
		assertEquals(3, pop.getIndividuals().get(0).getSquads().size());
		
		// test squad size
		for (int i = 0; i < 3; i++) {
			assertEquals(1, pop.getIndividuals().get(0).getSquads().get(i).getMembers().size());
		}
		
		// advance to next generation
		pop.nextGeneration();
		
		// test sorting by fitness of next gen
		boolean firstIsLowest2 = pop.getIndividuals().get(0).getVariance() <= pop.getIndividuals().get(99).getVariance();
		assertTrue(firstIsLowest2);
		
		// test population size of next gen
		assertEquals(100, pop.getIndividuals().size());
		
		// test wait list size of next gen
		assertEquals(1, pop.getIndividuals().get(0).getWaitList().size());
				
		// test number of squads in next gen
		assertEquals(3, pop.getIndividuals().get(0).getSquads().size());
		
		// test squad size of next gen
		for (int i = 0; i < 3; i++) {
			assertEquals(1, pop.getIndividuals().get(0).getSquads().get(i).getMembers().size());
		}
		
		// test that variances changed for the next gen
		boolean completeMatch = true;
		for (int i = 0; i < pop.getIndividuals().size(); i++) {
			if (pop.getIndividuals().get(i).getVariance() != initialVariances.get(i))
				completeMatch = false;
		}
		assertFalse(completeMatch);
	}
}
