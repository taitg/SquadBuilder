# SquadBuilder

Description
===========

This application creates equally matched hockey squads from a collection of players. Each player is assumed to have an assigned rating for 3 skills - skating, shooting, and checking - and it is assumed that skills should not have a negative rating. Player data is read from JSON and the user is served via a webpage. The web server implementation is threaded, with a main thread that listens for incoming connections and spawns additional worker threads to handle them.

Optimization Algorithm
======================

Squads of equal size and balanced skill ratings are generated using a genetic algorithm (https://en.wikipedia.org/wiki/Genetic_algorithm), in which the fitness function is the total of the variances (https://en.wikipedia.org/wiki/Variance) between squads in each skill: skating, shooting, and checking. Each generation is repopulated (after removing the lowest-fitness individuals) with mutated versions of the surviving individuals, as well as a number of new random individuals. Mutation consists of randomly swapping members between squads or the waiting list. The algorithm halts and returns the best tournament arrangement found after a given number of milliseconds. 

Usage
=====

To run the server:

`java -jar squadbuilder.jar [PORT] [JSON_LOCATION]`

where:
* `PORT`, if entered, is a port number (default 8080)
* `JSON_LOCATION`, if entered, is either a file or URL containing properly formatted player data (default players.json)
	* The `PORT` argument must also be present to use the `JSON_LOCATION` argument

To use the webpage:

* By default, the homepage shows all players as being on the waiting list
* The user can enter a desired number of squads and click "Make" to generate and display balanced squads
	* Any players not assigned to a squad will remain on the waiting list
	* Because the algorithm utilizes randomness, the assignments for players will be different each run
* The user can then click "Reset" to return all players to the waiting list
* The waiting list and each squad are sorted alphabetically by last name

Technologies and Compilation
============================

The program was coded in Java (Eclipse) and depends on the GSON library for JSON-to-object deserialization. The jar file for GSON 2.8.4 has been included in the lib directory and should be included in the classpath before attempting to compile.

The web server is coded in a fairly low-level manner, without libraries or frameworks for connecting or producing HTML. This is because these are the techniques I am most familiar with at this time, but code would be easier to read and edit if these kinds of tools were applied.

CSS for the webpage utilizes the lightweight W3 framework, which has been included in the root directory.
