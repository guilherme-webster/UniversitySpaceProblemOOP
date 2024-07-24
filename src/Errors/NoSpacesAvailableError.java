package src.Errors;

import javafx.application.Platform;
import src.GraphicInterface.Controllers.SelectionController;

public class NoSpacesAvailableError extends Error {
    
    public NoSpacesAvailableError(){}

    public NoSpacesAvailableError(String message) {
        super(message);
        Platform.runLater(() -> SelectionController.errorStage(message));
    }
}
