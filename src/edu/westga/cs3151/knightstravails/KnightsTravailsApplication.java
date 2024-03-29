package edu.westga.cs3151.knightstravails;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Entry point for application.
 * 
 * @author CS3151
 * @version Spring 2024
 */
public class KnightsTravailsApplication extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/PuzzleGui.fxml"));
			Pane pane = loader.load();
			Scene scene = new Scene(pane);
			primaryStage.setScene(scene);
			primaryStage.setTitle("CS3151 Project by Jabesi Abwe");
			primaryStage.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Entry point
	 * 
	 * @pre none
	 * @post none
	 * @param args Not used
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
