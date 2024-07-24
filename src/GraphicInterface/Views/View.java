package src.GraphicInterface.Views;

import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Abstract class that provides basic functionality for JavaFX views.
 */
public abstract class View {
	private Stage stage;
	private Scene scene;
	private int prefWidth;
	private int prefHeight;
	
	public View() {
		
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public void loadScene(String path) throws IOException {
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/src/FXML/" + path + ".fxml")));
		this.scene = new Scene(root);
	}
	
	public void showStage() {
		stage.setScene(scene);
		stage.centerOnScreen();
	    stage.show();
	}
	
	public void closeStage() {
		stage.close();
	}
	
	public void hideStage() {
		stage.hide();
	}
	
	public void stagePrefSize() {
		stage.setWidth(prefWidth);
		stage.setHeight(prefHeight);
	}
	
	public void setPrefWidth(int prefWidth) {
		this.prefWidth = prefWidth;
	}
	
	public void setPrefHeight(int prefHeight) {
		this.prefHeight = prefHeight;
	}
}
