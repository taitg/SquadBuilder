# SquadBuilder

Description
===========

This application creates equally matched hockey squads from a collection of players. Each player is assumed to have an assigned rating for 3 skills: skating, shooting, and checking. Player data is read from JSON and the user is served via a webpage.

Optimization Algorithm
======================

Squads of equal size and balanced skill ratings are generated using a genetic algorithm (https://en.wikipedia.org/wiki/Genetic_algorithm), in which the fitness function is the total of the variances (https://en.wikipedia.org/wiki/Variance) between squads in each skill: skating, shooting, and checking. Each generation is repopulated (after culling the lowest-fitness individuals) with mutated versions of the surviving individuals, as well as a number of new random individuals. Mutation consists of randomly swapping members between squads or the waiting list. The algorithm halts and returns the best tournament arrangement found after a given number of milliseconds. 

Usage
=====

To run the server:

java -jar squadbuilder.jar [PORT] [JSON_LOCATION]

where:
(*) PORT, if entered, is a port number
(*) JSON_LOCATION, if entered, is either a file or URL containing properly formatted player data

To use the webpage:

(*) By default, the homepage shows all players as being on the waiting list
(*) The user can enter a desired number of squads and click "Make" to generate and display balanced squads
(*) The user can then click "Reset" to return all players to the waiting list