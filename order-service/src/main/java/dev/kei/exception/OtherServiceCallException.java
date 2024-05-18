package dev.kei.exception;

public class OtherServiceCallException extends RuntimeException{
    public OtherServiceCallException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
