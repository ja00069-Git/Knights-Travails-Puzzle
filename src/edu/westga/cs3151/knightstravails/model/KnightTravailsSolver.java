package edu.westga.cs3151.knightstravails.model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A class that solves the knight trail in the shorted moves posible
 * 
 * @author jabes
 * @version spring 24
 */
public class KnightTravailsSolver {

	private final Position startPosition;
	private final Position targetPosition;

	/**
	 * Initialize the knight solver
	 * 
	 * @pre start possition !=null
	 * @post none
	 * @param startPosition  the start possition of the knight
	 * @param targetPosition the target possition
	 */
	public KnightTravailsSolver(Position startPosition, Position targetPosition) {
		this.startPosition = startPosition;
		this.targetPosition = targetPosition;
	}

	/**
	 * Solves the the knight trail using breath first search
	 * 
	 * @pre none
	 * @post none
	 * @return the solution path as a linked list
	 */
	public LinkedList<Position> solve() {
		Queue<Position> queue = new LinkedList<>();
		LinkedList<Position> solutionPath = new LinkedList<>();
		boolean[][] visited = new boolean[Position.MAX_ROWS][Position.MAX_COLS];
		Position[][] parent = new Position[Position.MAX_ROWS][Position.MAX_COLS];

		queue.offer(this.startPosition);
		visited[this.startPosition.getRow()][this.startPosition.getCol()] = true;

		while (!queue.isEmpty()) {
			Position currentPosition = queue.poll();

			if (currentPosition.equals(this.targetPosition)) {
				solutionPath = this.reconstructPath(parent, currentPosition);
				break;
			}

			for (Position nextPosition : this.generateNextMoves(currentPosition)) {
				if (!visited[nextPosition.getRow()][nextPosition.getCol()]) {
					queue.offer(nextPosition);
					visited[nextPosition.getRow()][nextPosition.getCol()] = true;
					parent[nextPosition.getRow()][nextPosition.getCol()] = currentPosition;
				}
			}
		}

		return solutionPath;
	}

	private LinkedList<Position> generateNextMoves(Position position) {
		LinkedList<Position> nextMoves = new LinkedList<>();
		int[] rowOffsets = { -2, -1, 1, 2, 2, 1, -1, -2 };
		int[] colOffsets = { 1, 2, 2, 1, -1, -2, -2, -1 };

		for (int index = 0; index < 8; index++) {
			int newRow = position.getRow() + rowOffsets[index];
			int newCol = position.getCol() + colOffsets[index];
			if (this.isValidMove(newRow, newCol)) {
				nextMoves.add(new Position(newRow, newCol));
			}
		}

		return nextMoves;
	}

	private boolean isValidMove(int row, int col) {
		return row >= 0 && row < Position.MAX_ROWS && col >= 0 && col < Position.MAX_COLS;
	}

	private LinkedList<Position> reconstructPath(Position[][] parent, Position targetPosition) {
		LinkedList<Position> path = new LinkedList<>();
		Position current = targetPosition;

		while (current != null) {
			path.addFirst(current);
			current = parent[current.getRow()][current.getCol()];
		}

		return path;
	}
}
