package exceptions;

public class ZeroPriceComputationException extends RuntimeException {

    public ZeroPriceComputationException() {
        super();
    }


    public ZeroPriceComputationException(String message) {
        super(message);
    }


    public ZeroPriceComputationException(String message, Throwable cause) {
        super(message, cause);
    }


    public ZeroPriceComputationException(Throwable cause) {
        super(cause);
    }
}
