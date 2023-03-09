package org.simplycodestudio.taskprocessor.task.exceptions;

public class TaskNotReadyException extends RuntimeException {

    private static final String MESSAGE = "Please wait task is being processing: %d%%";

    public TaskNotReadyException(final int progressValue) {
        super(String.format(MESSAGE,progressValue));
    }
}
