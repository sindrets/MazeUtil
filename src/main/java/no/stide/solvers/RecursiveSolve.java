package no.stide.solvers;

import no.stide.Cell;
import no.stide.CellForm;
import no.stide.CellState;
import no.stide.Maze;

public class RecursiveSolve implements MazeSolver {

    private Maze maze;
    private int endX, endY;

    @Override
    public void solve(Maze maze, int startX, int startY, int endX, int endY) {
        this.maze = maze;
        this.endX = endX;
        this.endY = endY;
        this.recurse(startX, startY);
    }

    private boolean recurse(int x, int y) {
        Cell cell = maze.getCell(x, y);
        if (x == this.endX && y == this.endY) {
            cell.setForm(CellForm.CORRECT);
            return true;
        }
        if (cell.getState() == CellState.VISITED || cell.getForm() == CellForm.WALL) {
            return false;
        }

        cell.setState(CellState.VISITED);

        if (x != 0) {
            if (recurse(x - 1, y)) {
                cell.setForm(CellForm.CORRECT);
                return true;
            }
        }
        if (x != maze.getWidth() - 1) {
            if (recurse(x + 1, y)) {
                cell.setForm(CellForm.CORRECT);
                return true;
            }
        }
        if (y != 0) {
            if (recurse(x, y - 1)) {
                cell.setForm(CellForm.CORRECT);
                return true;
            }
        }
        if (y != maze.getHeight() - 1) {
            if (recurse(x, y + 1)) {
                cell.setForm(CellForm.CORRECT);
                return true;
            }
        }

        return false;
    }

}
