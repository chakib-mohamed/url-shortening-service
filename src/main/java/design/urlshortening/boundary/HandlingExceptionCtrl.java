package design.urlshortening.boundary;

import design.urlshortening.control.exception.BusinessException;
import design.urlshortening.control.exception.URLNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class HandlingExceptionCtrl {

    @ExceptionHandler(URLNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    private void onURLNotFoundException(URLNotFoundException e){
        log.error(String.format("A Business Exception occurred : %s", e.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
    private void onBusinessException(BusinessException e){
        log.error(String.format("A Business Exception occurred : %s", e.getMessage()));
    }
}
