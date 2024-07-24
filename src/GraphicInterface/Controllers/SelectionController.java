package src.GraphicInterface.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.GraphicInterface.LectureSelector;
import src.GraphicInterface.Views.ScheduleView;
import src.GraphicInterface.Views.SelectionView;
import src.Spaces.Space;
import src.CourseRelated.Discipline;
import src.System.ScheduleAllocator;

/**
 * Controller class for handling the selection of courses, spaces, and electives in the GUI.
 */
public class SelectionController extends ScheduleAllocator {
    // FXML fields
    @FXML
    private VBox coursesVBox;
    @FXML
    private FlowPane spacesFlowPane;
    @FXML
    private FlowPane electivesFlowPane;
    @FXML
    private Label attemptsLabel;

    // Lists for removed items chosen by the user 
    private static List<String> removedCourses = new ArrayList<>();
    private static List<String> removedSpaces = new ArrayList<>();
    private static List<String> removedElectives = new ArrayList<>();

    private static IntegerProperty attempts = new SimpleIntegerProperty(0);

    public static List<String> getRemovedCourses() {
        return removedCourses;
    }

    public static List<String> getRemovedSpaces() {
        return removedSpaces;
    }

    public static List<String> getRemovedElectives() {
        return removedElectives;
    }

    /**
     * Handles the removal of a course.
     */
    @FXML
    public void removeCourse(ActionEvent e) {
        remove(e, removedCourses);
    }

    /**
     * Handles the removal of a space.
     */
    @FXML
    public void removeSpace(ActionEvent e) {
        remove(e, removedSpaces);
    }

    /**
     * Handles the removal of an elective.
     */
    @FXML
    public void removeElective(ActionEvent e) {
        remove(e, removedElectives);
    }
    
    /**
     * Transition from selection view to schedule view.
     */
    @FXML
    public void submit(ActionEvent e) throws IOException {
        attempts.set(0);

        new Thread(() -> {
            LectureSelector lectureSelector = LectureSelector.getInstance();
            lectureSelector.readAllResources();
            lectureSelector.filterResourcesAndAllocate();

            Platform.runLater(() -> {
                ScheduleView scheduleView = ScheduleView.getInstance();
                scheduleView.setStage(SelectionView.getInstance().getStage());
                try {
                    scheduleView.loadScene("schedule");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                attemptsLabel.setText("");
                scheduleView.showStage();
                scheduleView.getStage().setMaximized(true);
            });
        }).start();
    }

    @FXML
    public void initialize() {
        initializeContainer(coursesVBox);
        initializeContainer(spacesFlowPane);
        initializeContainer(electivesFlowPane);
        LectureSelector.getInstance().readAllResources();

        attempts.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                if(newValue.intValue() >= 100)
                    attemptsLabel.setText("Tentativas: " + newValue);
            });
        });
    }

    /**
     * Displays an error message in a new stage.
     * @param message: message to be displayed
     */
    public static void errorStage(String message) {
        Label label = new Label(message);

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(label);
        vbox.setStyle("-fx-font-family: 'Liberation Serif'; -fx-font-size: 20; -fx-alignment: center; -fx-padding: 10;");

        Scene scene = new Scene(vbox);

        Stage stage = new Stage();
        stage.setTitle("Error message");
        stage.setScene(scene);
        stage.setMinWidth(550);
        stage.setMaxHeight(150);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Initializes a container by setting user data for each button to false.
     * @param container: parent container
     */
    private void initializeContainer(Parent container) {
        for (Node node : container.getChildrenUnmodifiable()) {
            if (node instanceof Button button)
                button.setUserData(false);
        }
    }

    /**
     * Handles the removal or re-addition of an item.
     */
    private List<String> remove(ActionEvent e, List<String> list) {
    	Button button = (Button) e.getSource();
    	
    	if (!((Boolean) button.getUserData())) {
    		list.add(button.getText());
    		button.getStyleClass().add("removed");
    		button.setUserData(true);
    	} else {
    		list.remove(button.getText());
    		button.getStyleClass().remove("removed");
    		button.setUserData(false);
    	}
    	return list;
    }

    public static void increaseAttempts() {
        attempts.set(attempts.get() + 1);
    }

    @FXML
    private void handleAddSpace(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../../FXML/addSpace.fxml"));
            GridPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Adicionar Local");
            //dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());
            dialogStage.centerOnScreen();
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AddSpaceController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.setMinWidth(400);
            dialogStage.setMinHeight(200);
            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                Space newSpace = controller.getSpace();
                LectureSelector.getInstance().addSpace(newSpace);
                addNewSpaceButton(newSpace);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewSpaceButton(Space newSpace) {
        Button spaceButton = new Button(newSpace.getSpaceID());
        spaceButton.setOnAction(this::removeSpace);
        spaceButton.getStyleClass().add("selection-button");
        spaceButton.setUserData(Boolean.FALSE);
        spacesFlowPane.getChildren().add(spaceButton);
    }

    @FXML
    private void handleAddElective(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../../FXML/addElective.fxml"));
            GridPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Adicionar Eletiva");
            //dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AddElectiveController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.setMinWidth(450);
            dialogStage.setMinHeight(400);
            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                Discipline newDiscipline = controller.getElectiveAdded();
                LectureSelector.getInstance().addDiscipline(newDiscipline);
                addElectiveButton(newDiscipline);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addElectiveButton(Discipline discipline) {
        Button electiveButton = new Button(discipline.getDisciplineId());
        electiveButton.setOnAction(this::removeElective);
        electiveButton.getStyleClass().add("selection-button");
        electiveButton.setUserData(Boolean.FALSE);
        electivesFlowPane.getChildren().add(electiveButton);
    }
}