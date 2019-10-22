package no.stide.solvers;

import no.stide.Maze;

public interface MazeSolver {
    public void solve(Maze maze, int startX, int startY, int endX, int endY);
}
