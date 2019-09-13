package no.stide;

import java.util.HashMap;

public class Cell {

	private int x, y;
	private CellState state;
	private CellForm form;
	private HashMap<Direction, Cell> neighbours;

	public Cell(int x, int y) {
		this(x, y, CellForm.WALL, CellState.UNKNOWN);
	}

	public Cell(int x, int y, CellForm form, CellState state) {

		this.x = x;
		this.y = y;
		this.form = form;
		this.state = state;
		this.neighbours = new HashMap<>();

	}

	public String getImage() {
		return this.form.getImage();
	}

	public String toString() {
		return String.format("[%s,%s]", x, y);
	}

	public void setState(CellState state) {
		this.state = state;
	}

	public void setForm(CellForm form) {
		this.form = form;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public CellState getState() {
		return this.state;
	}

	public CellForm getForm() {
		return this.form;
	}

	public HashMap<Direction, Cell> getNeighbours() {
		return this.neighbours;
	}

}
