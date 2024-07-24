package src.GraphicInterface.Controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.CourseRelated.Discipline;
import src.Spaces.InstituteAbbr;
import src.Spaces.SpaceType;

public class AddElectiveController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField idField;
    @FXML
    private TextField creditsField;
    @FXML
    private ComboBox<InstituteAbbr> institutesComboBox;
    @FXML
    private ComboBox<SpaceType> typesComboBox;
    @FXML
    private TextField professorsField;

    private Stage dialogStage;
    private boolean okClicked = false;
    private Discipline electiveDiscipline;

    @FXML
    public void initialize() {
        loadInstitutesComboBox();
        loadTypesComboBox();
    }

    private void loadInstitutesComboBox() {
        ObservableList<InstituteAbbr> observableInstitutesList = FXCollections.observableArrayList(InstituteAbbr.values());
        institutesComboBox.setItems(observableInstitutesList);
    }

    private void loadTypesComboBox() {
        ObservableList<SpaceType> observableTypesList = FXCollections.observableArrayList(SpaceType.values());
        typesComboBox.setItems(observableTypesList);
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public Discipline getElectiveAdded(){
        return this.electiveDiscipline;
    }

    @FXML
    private void handleOk() {
        String name = nameField.getText();
        String id = idField.getText();
        String credits = creditsField.getText();
        InstituteAbbr institute = (InstituteAbbr) institutesComboBox.getValue();
        List<InstituteAbbr> institutes = Collections.singletonList(institute);
        SpaceType type = (SpaceType) typesComboBox.getValue();
        List<SpaceType> types = new ArrayList<>();
        types.add(type);
        List<String> professors = Arrays.asList(professorsField.getText().split(","));

        if(Objects.equals(name, "") || Objects.equals(id, "") || Objects.equals(credits, "") || institute == null || type == null)
            return;

        int creditsInt = Integer.parseInt(credits);
        int necessaryTypes = creditsInt / 2;
        for(int i = 0; i < necessaryTypes - 1; i++){
            types.add(type);
        }

        electiveDiscipline = new Discipline(name, id, creditsInt, types, institutes, professors);
        okClicked = true;

        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
