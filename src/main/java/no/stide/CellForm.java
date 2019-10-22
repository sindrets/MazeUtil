package no.stide;

public enum CellForm {
	WALL("██"),
	PATH("  "),
	CORRECT("▒▒");

	private String image;

	public String getImage() {
		return this.image;
	}

	CellForm(String image) {
		this.image = image;
	}
}
