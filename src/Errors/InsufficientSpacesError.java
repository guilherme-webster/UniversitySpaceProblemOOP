package src.Errors;

public class InsufficientSpacesError extends Error {

    /**
     * Parameterless constructor
     */
    public InsufficientSpacesError(){}

    /**
     * Constructor with error message
     * 
     * @param message: the error message
     */
    public InsufficientSpacesError(String message){
        super(message);
    }
}
