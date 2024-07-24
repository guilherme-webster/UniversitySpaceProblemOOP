package src.GraphicInterface.Views;

/**
 * Singleton class responsible for displaying selection stage
 */
public class SelectionView extends View{
	private static SelectionView instance;
	
	private SelectionView() {
		super.setPrefWidth(1300);
		super.setPrefHeight(910);
	}
	
	public static SelectionView getInstance() {
        if(instance == null) {
            instance= new SelectionView();
        }
        return instance;
    }
}
