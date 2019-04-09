package no.stide;

import no.stide.generators.MazeGenerator;

public class Maze {

	private int width, height;
	private Cell[][] grid;

	public Maze(int width, int height) {

		this.width = width;
		this.height = height;
		this.grid = new Cell[width][height];
		this.initGrid();
		this.initNeighbours();

	}

	private void initGrid() {

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				this.grid[x][y] = new Cell(x, y);
			}
		}

	}

	private void initNeighbours() {

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Cell cell = grid[x][y];
				if (x > 0) cell.getNeighbours().put(Direction.WEST, grid[x-1][y]);
				if (x < width-1) cell.getNeighbours().put(Direction.EAST, grid[x+1][y]);
				if (y > 0) cell.getNeighbours().put(Direction.NORTH, grid[x][y-1]);
				if (y < height-1) cell.getNeighbours().put(Direction.SOUTH, grid[x][y+1]);
			}
		}

	}

	public void generate(MazeGenerator generator) {
		generator.generate(this);
	}

	public String toString() {

		StringBuilder result = new StringBuilder();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				result.append(grid[x][y].getImage());
			}
			if (y != height-1) result.append("\n");
		}

		return result.toString();

	}

	public void printGrid() {

		StringBuilder result = new StringBuilder();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x != 0) result.append(",");
				result.append(grid[x][y].toString());
			}
			if (y != height-1) result.append("\n");
		}

		System.out.println(result.toString());

	}

	public void setStateAll(CellState state) {
		for (Cell[] cellColumn : this.grid) {
			for (Cell cell : cellColumn) {
				cell.setState(state);
			}
		}
	}

	public void setFormAll(CellForm form) {
		for (Cell[] cellColumn : grid) {
			for (Cell cell : cellColumn) {
				cell.setForm(form);
			}
		}
	}

	public Cell getCell(int x, int y) {
		if (x < 0 || x > width-1 || y < 0 || y > height-1) return null;
		return grid[x][y];
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public Cell[][] getGrid() {
		return this.grid;
	}

}
