package dev.kei.exception;

public class MissingAuthHeaderException extends RuntimeException{
    public MissingAuthHeaderException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
