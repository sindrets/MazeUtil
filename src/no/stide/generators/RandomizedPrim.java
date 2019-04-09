package no.stide.generators;

import no.stide.Cell;
import no.stide.CellForm;
import no.stide.Maze;

import java.util.ArrayList;
import java.util.Random;

public class RandomizedPrim implements MazeGenerator {

	@Override
	public void generate(Maze maze) {

		maze.setFormAll(CellForm.WALL);

		ArrayList<Cell[]> walls = new ArrayList<>();

		// pick random cell with odd coords
		Random rand = new Random();
		int x = rand.nextInt(maze.getWidth() / 2) * 2 + 1;
		int y = rand.nextInt(maze.getHeight() / 2) * 2 + 1;
		Cell cell = maze.getCell(x, y);
		walls.add(new Cell[] {cell, cell});
		System.out.println(cell.toString());

		while (walls.size() > 0) {

			Cell[] current = walls.remove(rand.nextInt(walls.size()));
			Cell pCell = current[0];
			cell = current[1];

			if (cell.getForm() == CellForm.WALL) {

				pCell.setForm(CellForm.PATH);
				cell.setForm(CellForm.PATH);

				if (cell.getX() >= 2 && maze.getCell(cell.getX() - 2, cell.getY()).getForm() == CellForm.WALL) {
					walls.add(new Cell[] {
							maze.getCell(cell.getX() - 1, cell.getY()),
							maze.getCell(cell.getX() - 2, cell.getY())
					});
				}
				if (cell.getX() < maze.getWidth() - 2 && maze.getCell(cell.getX() + 2, cell.getY()).getForm() == CellForm.WALL) {
					walls.add(new Cell[] {
							maze.getCell(cell.getX() + 1, cell.getY()),
							maze.getCell(cell.getX() + 2, cell.getY())
					});
				}
				if (cell.getY() >= 2 && maze.getCell(cell.getX(), cell.getY() - 2).getForm() == CellForm.WALL) {
					walls.add(new Cell[] {
							maze.getCell(cell.getX(), cell.getY() - 1),
							maze.getCell(cell.getX(), cell.getY() - 2)
					});
				}
				if (cell.getY() < maze.getHeight() - 2 && maze.getCell(cell.getX(), cell.getY() + 2).getForm() == CellForm.WALL) {
					walls.add(new Cell[] {
							maze.getCell(cell.getX(), cell.getY() + 1),
							maze.getCell(cell.getX(), cell.getY() + 2)
					});
				}

			}

		}

	}

}
