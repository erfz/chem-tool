package io.github.erfz.chemtool;

/**
 * Created by lebesgue on 9/18/17.
 */

public class InvalidEquationException extends Exception {
    public InvalidEquationException() {
    }

    public InvalidEquationException(String message) {
        super(message);
    }

    public InvalidEquationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEquationException(Throwable cause) {
        super(cause);
    }
}
