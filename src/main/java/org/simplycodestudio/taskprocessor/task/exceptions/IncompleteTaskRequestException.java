package org.simplycodestudio.taskprocessor.task.exceptions;

public class IncompleteTaskRequestException extends RuntimeException{
    private static final String MESSAGE = "Please provide non empty input and pattern";

    public IncompleteTaskRequestException() {
        super(MESSAGE);
    }
}
