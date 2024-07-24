package src.GraphicInterface.Controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import src.CourseRelated.LectureRelated.Lecture;
import src.GraphicInterface.LectureSelector;
import src.GraphicInterface.Views.ElectivesView;
import src.Schedule.HourOfClass;
import src.Schedule.WeekDay;

public class ElectivesController {
    
    @FXML
    private GridPane electivesGridPane;
    
    @FXML
    public void initialize() {
        loadSchedule();
    }
    
    /**
     * Loads the electives schedule from LectureSelector and populates the grid.
     */
    public void loadSchedule() {
    	List<Lecture> electivesLectures = new ArrayList<>();
        for (Lecture lecture : LectureSelector.getInstance().getAllLectures()) {
            if (!lecture.getLectureDiscipline().getIsMandatory())
                electivesLectures.add(lecture);
        }
        
        sortLecturesByHourOfClass(electivesLectures);
    }
    
    /**
     * Assigns a lecture to the grid at the specified column based on its day of the week.
     *      
     * @param lecture: the lecture to assign to the grid.
     */
    private void assignLectureToGrid(Lecture lecture) {
        WeekDay day = lecture.getLectureSchedule().getDay();
        int column = WeekDay.getNumericValue(day) - 1;

        VBox vBox = (VBox) ScheduleController.getNodeByRowColumnIndex(electivesGridPane, 1, column);
        vBox.getChildren().add(createDisciplineIdLabel(lecture));
    }
    
    /**
     * Creates the disciplineID label.
     * Displays a preview popup when the discipline ID label is hovered over.
     * @param lecture: the lecture to assign to the grid.
     * @return the disciplineID label.
     */
    public Label createDisciplineIdLabel(Lecture lecture) {
    	Pane previewPane = new Pane();
        VBox previewPaneVBox = new VBox();
        Popup previewPopup = new Popup();
        List<Label> previewPaneLabels = createPreviewPaneLabels(lecture);
        
        previewPaneVBox.getChildren().addAll(previewPaneLabels);
        previewPane.getChildren().add(previewPaneVBox);
        previewPopup.getContent().add(previewPane);
        
        Label labelDisciplineId = new Label(lecture.getLectureDiscipline().getDisciplineId());
        labelDisciplineId.setOnMouseEntered(event -> {
            previewPopup.show(ElectivesView.getInstance().getStage(), event.getScreenX(), event.getScreenY() + 10);
        });
        labelDisciplineId.setOnMouseExited(event -> {
            previewPopup.hide();
        });
        
        labelDisciplineId.getStyleClass().addAll("elective");
        previewPaneVBox.setStyle("-fx-font-family: 'Liberation Serif'; -fx-font-size: 15; -fx-alignment: center; -fx-padding: 10;");
        previewPane.setStyle("-fx-background-color: lightgrey; -fx-border-color: black;");
        
        return labelDisciplineId;
    }
    
    /**
     * Creates labels for the preview pane displaying lecture details.
     * 
     * @param lecture: the lecture for which labels are created.
     * @return a list of labels containing lecture details.
     */
    private List<Label> createPreviewPaneLabels(Lecture lecture) {
        Label labelName = new Label(lecture.getLectureDiscipline().getDisciplineName());
        labelName.setStyle("-fx-font-weight: bold");
        Label labelProfessor = new Label(lecture.getProfessor());
        Label labelSpace = new Label(lecture.getLectureSpace().getSpaceID());
        Label labelCredits = new Label("Créditos: " + lecture.getLectureDiscipline().getDisciplineCredits());
        Label labelGroup = new Label("Grupo: " + lecture.getLectureGroup());
        Label labelHourOfClass = new Label(lecture.getLectureSchedule().getHourOfClass().toString());
        
        return new ArrayList<>(Arrays.asList(labelName, labelProfessor, labelSpace, labelCredits, labelGroup, labelHourOfClass));
    }
    
    /**
     * Sorts elective lectures by hour of class and assigns them to the grid.
     * 
     * @param electiveLectures: the list of elective lectures to be sorted and assigned.
     */
    private void sortLecturesByHourOfClass(List<Lecture> electiveLectures) {
        for (int i = 1; i <= 6; i++) {
            for (Lecture lecture : electiveLectures) {
                HourOfClass hourOfClass = lecture.getLectureSchedule().getHourOfClass();
                int intHourOfClass = HourOfClass.getNumericValue(hourOfClass);
                
                if (intHourOfClass == i) {
                    assignLectureToGrid(lecture);
                }
            }
        }
    }   
}