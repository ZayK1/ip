public class EmptyDescriptionException extends KiwiException {
    public EmptyDescriptionException(String taskType) {
        super("My guy, The description of a " + taskType + " cannot be empty.");
        
    }
}
