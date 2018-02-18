package chv.has.exceptions;

/**
 * @author Christopher Anciaux
 */
public class DisconnectedException extends Exception {
    public DisconnectedException() {
        super();
    }

    public DisconnectedException(String message) {
        super(message);
    }
}
