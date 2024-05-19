package dev.kei.exception;

public class InvalidAuthAccessTokenException extends RuntimeException{
    public InvalidAuthAccessTokenException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
