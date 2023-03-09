package org.simplycodestudio.taskprocessor.task.exceptions;

public class NoTaskFoundException extends RuntimeException{

    private static final String MESSAGE = "Task with id %s not exists";

    public NoTaskFoundException(final String taskId) {
        super(String.format(MESSAGE,taskId));
    }
}
