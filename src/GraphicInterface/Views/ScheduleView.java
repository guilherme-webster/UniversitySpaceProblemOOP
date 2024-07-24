package src.GraphicInterface.Views;

/**
 * Singleton class responsible for displaying schedule stage
 */
public class ScheduleView extends View {
	private static ScheduleView instance;
	
	private ScheduleView() {
		super.setPrefWidth(1080);
		super.setPrefHeight(700);
	}
	
	public static ScheduleView getInstance() {
        if(instance == null) {
            instance= new ScheduleView();
        }
        return instance;
    }
}
