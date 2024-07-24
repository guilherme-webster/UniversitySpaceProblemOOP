package src.GraphicInterface.Controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import src.CourseRelated.Course;
import src.CourseRelated.LectureRelated.Lecture;
import src.CourseRelated.LectureRelated.MandatoryLecture;
import src.GraphicInterface.LectureSelector;
import src.GraphicInterface.Views.ElectivesView;
import src.GraphicInterface.Views.SelectionView;
import src.Schedule.HourOfClass;
import src.Schedule.WeekDay;

/**
 * Controller class for handling the schedule view in the GUI.
 */
public class ScheduleController {

    @FXML
    private ComboBox<String> coursesComboBox, semesterComboBox, scheduleComboBox;
    
    @FXML
    private Label course, semester, invalidCourse, invalidSemester, invalidSchedule;
    
    @FXML
    private GridPane scheduleGridPane;
    
    private Font font = new Font("Liberation Serif", 20);

    @FXML
    public void initialize() {
        loadCoursesComboBox();
        loadSemestersComboBox();
        loadSchedulesComboBox();
    }

    /**
     * Loads the course names into the courses ComboBox.
     */
    private void loadCoursesComboBox() {
    	List<String> allCourses = new ArrayList<>();
        for (Course course : LectureSelector.getInstance().getAllCourses()) {
            allCourses.add(course.getCourseName());
        }
        ObservableList<String> observableCoursesList;
        observableCoursesList = FXCollections.observableArrayList(allCourses);
        coursesComboBox.setItems(observableCoursesList);
    }

    /**
     * Loads the semesters into the semesters ComboBox.
     */
    private void loadSemestersComboBox() {
    	List<String> allSemesters = Arrays.asList("1°", "2°", "3°", "4°", "5°", "6°", "7°", "8°", "9°", "10°");
    	ObservableList<String> observableSemesterList;
        observableSemesterList = FXCollections.observableArrayList(allSemesters);
        semesterComboBox.setItems(observableSemesterList);
    }

    /**
     * Loads the schedule options into the schedules ComboBox.
     */
    private void loadSchedulesComboBox() {
    	List<String> schedules = Arrays.asList("Cronograma atual", "Todas as aulas");
    	ObservableList<String> observableSchedulesList;
        observableSchedulesList = FXCollections.observableArrayList(schedules);
        scheduleComboBox.setItems(observableSchedulesList);
    }

    /**
     * Loads the schedule based on the selected course and semester.
     */
    @FXML
    public void loadSchedule() {
        cleanSchedule();
        if (invalidOption()) 
            return;
        
        int currentSemesterInt = convertSemesterToNumber(semesterComboBox.getValue());
        for (Lecture lecture : LectureSelector.getInstance().getAllLectures()) {
            if (lecture.getLectureDiscipline().getIsMandatory()) {
                MandatoryLecture mandatoryLecture = (MandatoryLecture) lecture;
                if (mandatoryLecture.getLectureCourse().getCourseName().equals(coursesComboBox.getValue()) 
                    && mandatoryLecture.getLectureCourse().getDisciplineSemester(lecture.getLectureDiscipline()) == currentSemesterInt) 
                    assignLectureToGrid(lecture);
            }
        }
    }

    /**
     * Rebuilds the schedule by redoing the distribution
     */
    @FXML
    public void rebuildSchedule() {
        LectureSelector.getInstance().readAllResources();
		LectureSelector.getInstance().filterResourcesAndAllocate();
        loadSchedule();
    }

    /**
     * Opens the electives view.
     * @throws IOException if there is an error loading the electives view.
     */
    @FXML
    public void viewElectives() throws IOException {
        ElectivesView electivesView = ElectivesView.getInstance();
        electivesView.closeStage();
        electivesView.loadScene("electives");
        electivesView.showStage();
    }

    /**
     * Goes back to the selection view.
     */
    @FXML
    public void goBack() {
        SelectionView.getInstance().showStage();
        SelectionView.getInstance().getStage().setMaximized(true);
    }

    /**
     * Saves the current schedule to an XML file.
     */
    @FXML
    private void saveSchedule() {
        if (invalidOption()) {
            return;
        }
        
        List<Lecture> selectedLectures = new ArrayList<>();
        if (scheduleComboBox.getValue() == null || "Current Schedule".equals(scheduleComboBox.getValue())) {
            int currentSemesterInt = convertSemesterToNumber(semesterComboBox.getValue());
            for (Lecture lecture : LectureSelector.getInstance().getAllLectures()) {
                if (lecture.getLectureDiscipline().getIsMandatory()) {
                    if (((MandatoryLecture) lecture).getLectureCourse().getCourseName().equals(coursesComboBox.getValue()) 
                        && ((MandatoryLecture) lecture).getLectureCourse().getDisciplineSemester(lecture.getLectureDiscipline()) == currentSemesterInt) 
                        selectedLectures.add(lecture);
                }
            }
        } else 
            selectedLectures = LectureSelector.getInstance().getAllLectures();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save XML File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("XML files (*.xml)", "*.xml"));
        File file = fileChooser.showSaveDialog(null);
        
        if (file != null) 
            saveSelectedLecturesToXML(file, selectedLectures);
    }

    /**
     * Assigns a lecture to the appropriate cell in the schedule grid.
     * @param lecture: the lecture to be assigned to the grid.
     */
    private void assignLectureToGrid(Lecture lecture) {
        WeekDay day = lecture.getLectureSchedule().getDay();
        HourOfClass hourOfClass = lecture.getLectureSchedule().getHourOfClass();
        int column = WeekDay.getNumericValue(day);
        int row = HourOfClass.getNumericValue(hourOfClass);

        // Create the content of each cell
        Label labelDisciplineId = new Label(lecture.getLectureDiscipline().getDisciplineId());
        Label labelProfessor = new Label(lecture.getProfessor());
        Label labelSpace = new Label(lecture.getLectureSpace().getSpaceID());
        Label labelGroup = new Label(Character.toString(lecture.getLectureGroup()));
        List<Label> labels = Arrays.asList(labelDisciplineId, labelGroup, labelProfessor, labelSpace);
        updateFont(font, labels);

        VBox vBox = (VBox) getNodeByRowColumnIndex(scheduleGridPane, row, column);
        vBox.getChildren().addAll(labels);
        vBox.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            double newSize = newHeight.doubleValue() / 5;
            double minFontSize = 15.0;
            double maxFontSize = 20.0;
            double adjustedFontSize = Math.max(minFontSize, Math.min(maxFontSize, newSize));
            font = new Font("Liberation Serif", adjustedFontSize);
            updateFont(font, labels);
        });
    }

    /**
     * Retrieves a node in the GridPane by its row and column index.
     * @param gridPane: the GridPane to search.
     * @param row: the row index.
     * @param column: the column index.
     * @return the node at the specified row and column, or null if not found.
     */
    public static Node getNodeByRowColumnIndex(GridPane gridPane, int row, int column) {
        for (Node node : gridPane.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            rowIndex = rowIndex == null ? 0 : rowIndex;
            Integer colIndex = GridPane.getColumnIndex(node);
            colIndex = colIndex == null ? 0 : colIndex;
            if (rowIndex.equals(row) && colIndex.equals(column)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Clears the schedule grid by removing all lectures.
     */
    private void cleanSchedule() {
        for (int row = 1; row <= 6; row++) {
            for (int column = 1; column <= 5; column++) {
                VBox vBox = (VBox) getNodeByRowColumnIndex(scheduleGridPane, row, column);
                vBox.getChildren().clear();
            }
        }
    }

    /**
     * Converts the semester from a string representation to an integer.
     * @param currentSemester: the semester in string format.
     * @return the semester in integer format.
     */
    private int convertSemesterToNumber(String currentSemester) {
        String numericString = currentSemester.replace("°", "");
        return Integer.parseInt(numericString);
    }

    /**
     * Updates the font for a list of labels.
     * @param font: the new font.
     * @param labels: the labels to be updated.
     */
    private void updateFont(Font font, List<Label> labels) {
        for (Label label : labels) {
            label.setFont(font);
        }
    }

    /**
     * Checks if the current options are invalid.
     * @return true if the options are invalid, false otherwise.
     */
    private boolean invalidOption() {
        String currentCourse = coursesComboBox.getValue();
        String currentSemester = semesterComboBox.getValue();
        String currentSchedule = scheduleComboBox.getValue();
        invalidCourse.setText("");
        invalidSemester.setText("");
        invalidSchedule.setText("");

        if ((currentSchedule == null || "Current Schedule".equals(currentSchedule)) 
            && (currentCourse == null || currentSemester == null)) {
            if (currentCourse == null) {
                invalidCourse.setText("Por favor escolha um curso");
            }
            if (currentSemester == null) {
                invalidSemester.setText("Por favor escolha um semestre");
            }
            return true;
        }
        
        course.setText(currentCourse);
        semester.setText("Semester: " + currentSemester);
        
        return false;
    }

    /**
     * Saves the selected lectures to an XML file.
     * @param file the file to save the lectures to.
     */
    private void saveSelectedLecturesToXML(File file, List<Lecture> selectedLectures) {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("lectures");
            document.appendChild(root);

            for (Lecture lecture : selectedLectures) {
                root.appendChild(createLectureElement(document, lecture));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{https://xml.apache.org/xslt}indent-amount", "4");

            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(domSource, streamResult);

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }

    /**
     * Creates an XML element for a lecture.
     * @param document: the XML document.
     * @param lecture: the lecture to create an element for.
     * @return the created XML element.
     */
    private Element createLectureElement(Document document, Lecture lecture) {
        Element lectureElement = document.createElement("lecture");

        if (lecture instanceof MandatoryLecture && ((MandatoryLecture) lecture).getLectureCourse() != null) {
            Element courseNameElement = document.createElement("courseName");
            courseNameElement.appendChild(document.createTextNode(((MandatoryLecture) lecture).getLectureCourse().getCourseName()));
            lectureElement.appendChild(courseNameElement);
        }

        Element disciplineIdElement = document.createElement("disciplineId");
        disciplineIdElement.appendChild(document.createTextNode(lecture.getLectureDiscipline().getDisciplineId()));
        lectureElement.appendChild(disciplineIdElement);

        if (lecture instanceof MandatoryLecture && ((MandatoryLecture) lecture).getLectureCourse() != null) {
            Element semesterElement = document.createElement("semester");
            semesterElement.appendChild(document.createTextNode(Integer.toString(((MandatoryLecture) lecture).getLectureCourse().getDisciplineSemester(lecture.getLectureDiscipline()))));
            lectureElement.appendChild(semesterElement);
        }

        Element professorElement = document.createElement("professor");
        professorElement.appendChild(document.createTextNode(lecture.getProfessor()));
        lectureElement.appendChild(professorElement);
        
        Element weekDayElement = document.createElement("weekDay");
        weekDayElement.appendChild(document.createTextNode(lecture.getLectureSchedule().getDay().name()));
        lectureElement.appendChild(weekDayElement);
        
        Element hourOfClassElement = document.createElement("hourOfClass");
        hourOfClassElement.appendChild(document.createTextNode(lecture.getLectureSchedule().getHourOfClass().name()));
        lectureElement.appendChild(hourOfClassElement);
        
        Element placeElement = document.createElement("place");
        placeElement.appendChild(document.createTextNode(lecture.getLectureSpace().getSpaceID()));
        lectureElement.appendChild(placeElement);
        
        Element groupElement = document.createElement("group");
        groupElement.appendChild(document.createTextNode(Character.toString(lecture.getLectureGroup())));
        lectureElement.appendChild(groupElement);
        
        Element creditsElement = document.createElement("credits");
        creditsElement.appendChild(document.createTextNode(Integer.toString(lecture.getLectureDiscipline().getDisciplineCredits())));
        lectureElement.appendChild(creditsElement);

        return lectureElement;
    }
}