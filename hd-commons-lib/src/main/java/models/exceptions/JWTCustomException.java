package models.exceptions;

public class JWTCustomException extends RuntimeException {
    public JWTCustomException(String message) {
        super(message);
    }
}
