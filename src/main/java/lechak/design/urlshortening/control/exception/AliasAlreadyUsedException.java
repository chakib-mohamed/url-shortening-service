package lechak.design.urlshortening.control.exception;

public class AliasAlreadyUsedException extends BusinessException {

    public AliasAlreadyUsedException(String message) {
        super(message);
    }
}
