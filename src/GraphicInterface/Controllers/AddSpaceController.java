package src.GraphicInterface.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.Spaces.InstituteAbbr;
import src.Spaces.Space;
import src.Spaces.SpaceType;

public class AddSpaceController {
    @FXML
    private TextField spaceNameField;
    @FXML
    private ComboBox<InstituteAbbr> institutesComboBox;
    @FXML
    private ComboBox<SpaceType> typesComboBox;
    
    private Stage dialogStage;
    private boolean okClicked = false;
    private Space space;

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
    
    public Space getSpace() {
        return this.space;
    }
    
    @FXML
    private void handleOk() {
        String spaceID = spaceNameField.getText();
        if(spaceID == null)
            return;
        InstituteAbbr institute = (InstituteAbbr) institutesComboBox.getValue();
        SpaceType type = (SpaceType) typesComboBox.getValue();
        if (type == null)
            return;

        space = new Space(spaceID, type, institute);
        okClicked = true;
        dialogStage.close();
    }
    
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
