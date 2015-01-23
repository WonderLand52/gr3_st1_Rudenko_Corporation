package exceptions;


public class NoProjectException extends Exception{
    private String message;

    public NoProjectException(String message) {
        super(message);
    }
}
