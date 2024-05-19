package dev.kei.exception;

public class ExceedRateLimitException extends RuntimeException{
    public ExceedRateLimitException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
