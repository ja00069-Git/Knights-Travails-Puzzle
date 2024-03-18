package edu.westga.cs3151.knightstravails.view;

import edu.westga.cs3151.knightstravails.model.Position;
import edu.westga.cs3151.knightstravails.viewmodel.PuzzleViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;

/**
 * The Class PuzzleCodeBehind
 * 
 * @author CS3151
 * @version Spring 2024
 */
public class PuzzleCodeBehind {

	@FXML
	private AnchorPane chessBoardPane;

	@FXML
	private Label headerLabel;

	@FXML
	private Label numberMovesLabel;

	@FXML
	private Label youWonLabel;

	@FXML
	private Label youLostLabel;

	private Button[][] squareButtons;

	@FXML
	private Button undoButton;

	@FXML
	private Button helpButton;

	@FXML
	private Button showSolutionButton;

	@FXML
	private Pane numberMovesPane;

	private PuzzleViewModel viewModel;
	private final SimpleObjectProperty<Position> knightPositionProperty;
	private final SimpleObjectProperty<Position> targetPositionProperty;
	private ImageView knightIcon;
	private ImageView targetIcon;
	private ImageView knightOnTargetIcon;

	/**
	 * Instantiates a new student info code behind.
	 * 
	 * @precondition none
	 * @precondition none
	 */
	public PuzzleCodeBehind() {
		this.viewModel = new PuzzleViewModel();
		this.knightPositionProperty = new SimpleObjectProperty<Position>();
		this.targetPositionProperty = new SimpleObjectProperty<Position>();
		this.knightIcon = this.createChessSquareIcon("images/Knight.png");
		this.knightOnTargetIcon = this.createChessSquareIcon("images/KnightOnTarget.png");
		this.targetIcon = this.createChessSquareIcon("images/Target.png");
	}

	private ImageView createChessSquareIcon(String imagename) {
		ImageView icon = new ImageView(new Image(imagename));
		icon.setFitHeight(52);
		icon.setPreserveRatio(true);
		return icon;
	}

	@FXML
	private void initialize() {
		this.setupChessBoard();
		this.setupBindings();
		this.vissibilityDisableBindings();
		this.setupListeners();
		this.viewModel.initializeNewPuzzle();
		this.numberMovesPane.setBorder(new Border(
				new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
	}

	private void vissibilityDisableBindings() {
		this.headerLabel.visibleProperty()
				.bind(Bindings.not(this.viewModel.wonProperty().or(this.viewModel.lostProperty())));

		this.youWonLabel.visibleProperty().bind(this.viewModel.wonProperty());

		this.youLostLabel.visibleProperty().bind(this.viewModel.lostProperty());

		this.undoButton.disableProperty()
				.bind(this.viewModel.wonProperty().and(Bindings.not(this.viewModel.lostProperty()))
						.or(this.viewModel.wonProperty().and(this.viewModel.lostProperty()))
						.or(this.viewModel.lostProperty().and(Bindings.not(this.viewModel.wonProperty()))));

		this.showSolutionButton.disableProperty()
				.bind(this.viewModel.wonProperty().and(Bindings.not(this.viewModel.lostProperty())));

		this.chessBoardPane.disableProperty()
				.bind(this.viewModel.wonProperty().and(Bindings.not(this.viewModel.lostProperty()))
						.or(this.viewModel.wonProperty().and(this.viewModel.lostProperty()))
						.or(this.viewModel.lostProperty().and(Bindings.not(this.viewModel.wonProperty()))));
	}

	private void setupBindings() {
		this.knightPositionProperty.bind(this.viewModel.getKnightPositionProperty());
		this.targetPositionProperty.bind(this.viewModel.getTargetPositionProperty());
		this.numberMovesLabel.textProperty().bindBidirectional(this.viewModel.numberMovesProperty(),
				new NumberStringConverter());
	}

	private void setupChessBoard() {
		this.squareButtons = new Button[8][8];
		for (int row = 0; row < Position.MAX_ROWS; row++) {
			for (int col = 0; col < Position.MAX_COLS; col++) {
				this.squareButtons[row][col] = new Button("");
				this.squareButtons[row][col].setLayoutX(col * 60 + 2);
				this.squareButtons[row][col].setLayoutY(row * 60 + 2);
				this.squareButtons[row][col].setPrefSize(60, 60);
				this.squareButtons[row][col].setPadding(new Insets(2));
				if ((row + col) % 2 == 1) {
					this.squareButtons[row][col].setStyle("-fx-background-color: DarkSlateGray");
				}
				this.chessBoardPane.getChildren().add(this.squareButtons[row][col]);
				this.addBoardSquareListener(row, col);
			}
		}
	}

	private void addBoardSquareListener(int row, int col) {
		this.squareButtons[row][col].setOnAction(event -> {
			this.viewModel.moveKnight(new Position(row, col));
		});
	}

	private void setupListeners() {
		this.knightPositionProperty.addListener((observable, oldValue, newValue) -> {
			if (oldValue != null) {
				if (oldValue.equals(this.targetPositionProperty.getValue())) {
					this.squareButtons[oldValue.getRow()][oldValue.getCol()].setGraphic(this.targetIcon);
				} else {
					this.squareButtons[oldValue.getRow()][oldValue.getCol()].setGraphic(null);
				}
			}
			if (newValue != null) {
				if (newValue.equals(this.targetPositionProperty.getValue())) {
					this.squareButtons[newValue.getRow()][newValue.getCol()].setGraphic(this.knightOnTargetIcon);
				} else {
					this.squareButtons[newValue.getRow()][newValue.getCol()].setGraphic(this.knightIcon);
				}
			}
		});
		this.targetPositionProperty.addListener((observable, oldValue, newValue) -> {
			if (oldValue != null) {
				this.squareButtons[oldValue.getRow()][oldValue.getCol()].setGraphic(null);
			}
			if (newValue != null) {
				this.squareButtons[newValue.getRow()][newValue.getCol()].setGraphic(this.targetIcon);
			}
		});
	}

	@FXML
	void handleUndo(ActionEvent event) {
		this.viewModel.undo();
	}

	@FXML
	void handleNewPuzzle(ActionEvent event) {
		this.viewModel.initializeNewPuzzle();
	}

	@FXML
	void handleShowSolution(ActionEvent event) {
		this.viewModel.showSolution();
	}

	@FXML
	void handleExit(ActionEvent event) {
		Platform.exit();
		System.exit(0);
	}
}
