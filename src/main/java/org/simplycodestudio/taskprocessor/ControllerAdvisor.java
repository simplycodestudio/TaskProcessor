package org.simplycodestudio.taskprocessor;

import org.simplycodestudio.taskprocessor.task.exceptions.IncompleteTaskRequestException;
import org.simplycodestudio.taskprocessor.task.exceptions.NoTaskFoundException;
import org.simplycodestudio.taskprocessor.task.exceptions.TaskNotReadyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.ConstraintViolationException;


@ControllerAdvice
@ApiIgnore
class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoTaskFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFoundException(
            NoTaskFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IncompleteTaskRequestException.class)
    public ResponseEntity<ErrorResponse> handleIncompleteTaskRequestException(
            IncompleteTaskRequestException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {TaskNotReadyException.class})
    public ResponseEntity<ErrorResponse> handleTaskNotReadyException(TaskNotReadyException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}