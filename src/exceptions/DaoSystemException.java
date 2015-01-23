package exceptions;


public class DaoSystemException extends Exception {
    String message;

    public DaoSystemException(String message) {
        this.message = message;
    }
}
