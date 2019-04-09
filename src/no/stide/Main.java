package no.stide;

import no.stide.generators.RandomizedPrim;

public class Main {

	public static void main(String[] args) {

		Maze maze = new Maze(127, 63);
		maze.generate(new RandomizedPrim());
		System.out.println(maze.toString());

	}
	
}
