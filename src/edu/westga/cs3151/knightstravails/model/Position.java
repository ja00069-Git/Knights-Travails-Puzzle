package edu.westga.cs3151.knightstravails.model;

/**
 * The Class Position
 * 
 * @author CS3151
 * @version Spring 2024
 */
public class Position {

	public static final int MAX_ROWS = 8;
	public static final int MAX_COLS = 8;

	private final int row;
	private final int col;

	/**
	 * Instantiates a new position on a board with MAX_ROWS x MAX_COLS cells
	 * 
	 * @pre row >= 0 and row < MAX_ROWS and col >= 0 and col < MAX_COLS
	 * @post getRow() == row and getCol() == col
	 * @param row the row index
	 * @param col the column index
	 */
	public Position(int row, int col) {
		if (row < 0 || row >= MAX_ROWS || col < 0 || col >= MAX_COLS) {
			throw new IllegalArgumentException("invalid chess board position");
		}
		this.row = row;
		this.col = col;
	}

	/**
	 * Gets the row index
	 * 
	 * @pre none
	 * @post none
	 * @return the row
	 */
	public int getRow() {
		return this.row;
	}

	/**
	 * Gets the column index
	 * 
	 * @pre none
	 * @post none
	 * @return the column
	 */
	public int getCol() {
		return this.col;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Position)) {
			return false;
		}
		return this.getRow() == ((Position) obj).getRow() && this.getCol() == ((Position) obj).getCol();
	}

	@Override
	public int hashCode() {
		return 8 * this.getRow() + this.getCol();
	}
}
