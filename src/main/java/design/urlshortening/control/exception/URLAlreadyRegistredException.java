package design.urlshortening.control.exception;

public class URLAlreadyRegistredException extends BusinessException {

    public URLAlreadyRegistredException(String message) {
        super(message);
    }
}
