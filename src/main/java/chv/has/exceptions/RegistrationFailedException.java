package chv.has.exceptions;

/**
 * @author Christopher Anciaux
 */
public class RegistrationFailedException extends Exception {
    public RegistrationFailedException() {
        super();
    }

    public RegistrationFailedException(String message) {
        super(message);
    }
}
