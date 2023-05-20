package ru.itis.rssnews.aspects;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.itis.rssnews.exceptions.NotFoundException;

@Log4j2
@ControllerAdvice
public class ExceptionHandlerAdvice {
    private final String LOG_FORMAT = "Exception: %s with message: %s";

    @ExceptionHandler({NoHandlerFoundException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundError(Exception ex) {
        log.info(String.format(LOG_FORMAT, ex, ex.getMessage()));
        return "error/404";
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleInternalError(Throwable throwable) {
        Class<?> clazz = throwable.getClass();
        log.warn(String.format(LOG_FORMAT, clazz, throwable.getMessage()));
        return "error/500";
    }
}
