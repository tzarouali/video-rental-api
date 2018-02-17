package exceptions;

public class InvalidRequestInputException extends RuntimeException {

    public InvalidRequestInputException() {
        super();
    }


    public InvalidRequestInputException(String message) {
        super(message);
    }


    public InvalidRequestInputException(String message, Throwable cause) {
        super(message, cause);
    }


    public InvalidRequestInputException(Throwable cause) {
        super(cause);
    }
}
