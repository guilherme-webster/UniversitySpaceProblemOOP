package src.GraphicInterface.Views;

import javafx.stage.Stage;

/**
 * Singleton class responsible for displaying electives stage
 */
public class ElectivesView extends View{
	private static ElectivesView instance;
	
	private ElectivesView() {
		Stage stage = new Stage();
		stage.setMinWidth(700);
		stage.setMinHeight(560); 
		super.setStage(stage);
	}
	
	public static ElectivesView getInstance(){
        if(instance == null) {
            instance= new ElectivesView();
        }
        return instance;
    }
}

