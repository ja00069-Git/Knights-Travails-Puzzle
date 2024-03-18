package edu.westga.cs3151.knightstravails.viewmodel;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

import edu.westga.cs3151.knightstravails.model.KnightTravailsSolver;
import edu.westga.cs3151.knightstravails.model.Position;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Duration;

/**
 * The Class PuzzleViewModel
 * 
 * @author CS3151, jabes
 * @version Spring 2024
 */
public class PuzzleViewModel {

	private final SimpleObjectProperty<Position> knightPositionProperty;
	private final SimpleObjectProperty<Position> targetPositionProperty;
	private Position startPosition;
	private final BooleanProperty wonProperty;
	private final BooleanProperty lostProperty;
	private final SimpleIntegerProperty numberMovesProperty;
	private Stack<Position> moveHistory;
	private LinkedList<Position> solutionPath;

	/**
	 * Instantiates a new puzzle viewmodel for the logic of the knight's travails
	 * puzzle GUI
	 * 
	 * @pre none
	 * @post a new view model representing a knight's travails puzzle instance
	 */
	public PuzzleViewModel() {
		this.knightPositionProperty = new SimpleObjectProperty<Position>();
		this.targetPositionProperty = new SimpleObjectProperty<Position>();
		this.wonProperty = new SimpleBooleanProperty(false);
		this.lostProperty = new SimpleBooleanProperty(false);
		this.startPosition = null;
		this.numberMovesProperty = new SimpleIntegerProperty();
		this.moveHistory = new Stack<>();

	}

	/**
	 * Gets the knight position property.
	 *
	 * @pre none
	 * @post none
	 * @return the knight position property
	 */
	public SimpleObjectProperty<Position> getKnightPositionProperty() {
		return this.knightPositionProperty;
	}

	/**
	 * Gets the target position property.
	 *
	 * @pre none
	 * @post none
	 * @return the knight position property
	 */
	public SimpleObjectProperty<Position> getTargetPositionProperty() {
		return this.targetPositionProperty;
	}

	/**
	 * Gets the won property.
	 *
	 * @pre none
	 * @post none
	 * @return the solved property
	 */
	public BooleanProperty wonProperty() {
		return this.wonProperty;
	}

	/**
	 * Gets the lost property.
	 *
	 * @pre none
	 * @post none
	 * @return the solved property
	 */
	public BooleanProperty lostProperty() {
		return this.lostProperty;
	}

	/**
	 * Gets the number moves property.
	 *
	 * @pre none
	 * @post none
	 * @return the solved property
	 */
	public SimpleIntegerProperty numberMovesProperty() {
		return this.numberMovesProperty;
	}

	/**
	 * Moves knight to the position selected by the user if the move is valid.
	 * 
	 * @pre position != null
	 * @post the knight is moved to the specified position if valid && (knight has
	 *       reached the goal in the minimum number moves ->
	 *       this.wonProperty.getValue() == true) && (knight has reached the goal in
	 *       more than the minimum number of moves -> this.lostProperty.getValue()
	 *       == true)
	 * @param position the new position of the knight
	 */
	public void moveKnight(Position position) {

		this.generateSolutionPath();

		if (this.isValidMove(position)) {
			this.knightPositionProperty.setValue(position);
			this.numberMovesProperty.setValue(this.numberMovesProperty.getValue() + 1);

			if (position.equals(this.targetPositionProperty.getValue())) {
				int minMovesToTarget = this.solutionPath.size() - 1;
				if (this.numberMovesProperty.getValue() == minMovesToTarget) {
					this.wonProperty.setValue(true);
					this.lostProperty.setValue(false);
				} else {
					this.wonProperty.setValue(false);
					this.lostProperty.setValue(true);
				}
			}
		}
		this.moveHistory.push(this.knightPositionProperty.getValue());
	}

	/**
	 * Checks if the move to the specified position is valid from the current knight
	 * position.
	 *
	 * @param position the position to move to
	 * @return true if the move is valid, false otherwise
	 */
	private boolean isValidMove(Position position) {
		Position currentPos = this.knightPositionProperty.get();
		int rowDiff = Math.abs(position.getRow() - currentPos.getRow());
		int colDiff = Math.abs(position.getCol() - currentPos.getCol());
		return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
	}

	/**
	 * Undoes the most recent move.
	 * 
	 * @pre none
	 * @post the most recent move is undone
	 */
	public void undo() {
		if (!this.moveHistory.isEmpty()) {
			this.moveHistory.pop();
			if (!this.moveHistory.isEmpty()) {
				this.knightPositionProperty.setValue(this.moveHistory.peek());
			} else {
				this.knightPositionProperty.setValue(this.startPosition);
			}
			this.numberMovesProperty.setValue(this.numberMovesProperty.getValue() - 1);
		}
	}

	/**
	 * Shows a shortest path from the start to the goal position
	 * 
	 * @pre none
	 * @post wonProperty.getValue == true && lostProperty.getValue() == true
	 */
	public void showSolution() {

		this.generateSolutionPath();
		this.wonProperty.setValue(true);
		this.lostProperty.setValue(true);
		this.tracePath(this.solutionPath.iterator(), this.solutionPath.size() - 1);
	}

	private void generateSolutionPath() {
		KnightTravailsSolver solver = new KnightTravailsSolver(this.startPosition,
				this.targetPositionProperty.getValue());
		this.solutionPath = solver.solve();
	}

	/**
	 * Instantiates a new knight's travails instance
	 * 
	 * @pre none
	 * @post wonProperty.getValue == false && lostProperty.getValue() == false
	 */
	public void initializeNewPuzzle() {
		Random rand = new Random();
		this.startPosition = new Position(rand.nextInt(Position.MAX_ROWS), rand.nextInt(Position.MAX_COLS));
		this.knightPositionProperty.setValue(this.startPosition);
		Position targetPosition = this.startPosition;
		while (targetPosition.equals(this.startPosition)) {
			targetPosition = new Position(rand.nextInt(Position.MAX_ROWS), rand.nextInt(Position.MAX_COLS));
		}
		this.targetPositionProperty.setValue(targetPosition);
		this.wonProperty.setValue(false);
		this.lostProperty.setValue(false);
		this.numberMovesProperty.setValue(0);
		this.moveHistory = new Stack<>();
	}

	/**
	 * Traces the specified path by setting the knight position property one-by-one
	 * to the positions on the path.
	 * 
	 * @pre the number positions the iterator path returns is equal to pathLength+1
	 * @post none
	 * @param path       the path to be traced
	 * @param pathLength the number moves on the path
	 */
	private void tracePath(Iterator<Position> path, int pathLength) {
		this.numberMovesProperty.setValue(0);
		if (path.hasNext()) {
			this.knightPositionProperty.setValue(path.next());
		}
		if (path.hasNext()) {
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.7), evt -> {
				Position nextPosition = path.next();
				this.knightPositionProperty.setValue(nextPosition);
				this.numberMovesProperty.setValue(this.numberMovesProperty.getValue() + 1);
			}));
			timeline.setCycleCount(pathLength);
			timeline.play();
		}
	}
}
