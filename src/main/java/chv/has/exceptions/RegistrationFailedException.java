package chv.has.exceptions;

/**
 * @author Christopher Anciaux
 */
public class RegistrationFailedException extends Exception {
    protected static final long serialVersionUID = 5059433742505712159L;

    public RegistrationFailedException() {
        super();
    }

    public RegistrationFailedException(String message) {
        super(message);
    }
}
