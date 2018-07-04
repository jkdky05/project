/*
 * Created on @Nov 14, 2012
 * Copyright - Confidential use
 */
package ucab.ingsw.exception;

/**
 * exception Class BusinessException
 *
 * @author Carlos D. Barroeta M.
 */
public class FailedTestException extends RuntimeException {

    /**
     * Creates a new instance of
     * <code>BusinessException</code> without detail message.
     */
    public FailedTestException() {
    }

    /**
     * Constructs an instance of
     * <code>BusinessException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FailedTestException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of
     * <code>BusinessException</code> with the specified detail message and
     * original cause
     *

     * @param cause root exception
     */
    public FailedTestException(String message, Throwable cause) {
        super(message, cause);
    }
}
