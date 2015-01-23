package exceptions;


public class NoSuchEntityException extends Exception {
    private String message;

    public NoSuchEntityException(String message) {
        super(message);
    }
}
