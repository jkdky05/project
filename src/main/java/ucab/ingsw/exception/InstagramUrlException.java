/*
 * Created on @Nov 14, 2012
 * Copyright - Confidential use
 */
package ucab.ingsw.exception;

/**
 * exception Class DataAccessException
 * 
 * @author Carlos D. Barroeta M.
 */
public class InstagramUrlException extends RuntimeException {

    /**
     * Creates a new instance of <code>DataAccessException</code> without detail message.
     */
    public InstagramUrlException() {
    }


    /**
     * Constructs an instance of <code>DataAccessException</code> with the
     * specified detail message.
     * @param msg the detail message.
     */
    public InstagramUrlException(String msg) {
        super(msg);
    }
}
