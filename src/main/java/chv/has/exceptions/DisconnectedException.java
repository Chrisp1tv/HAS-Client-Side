package chv.has.exceptions;

/**
 * @author Christopher Anciaux
 */
public class DisconnectedException extends Exception {
    protected static final long serialVersionUID = -4668096837839986885L;

    public DisconnectedException() {
        super();
    }

    public DisconnectedException(String message) {
        super(message);
    }
}
