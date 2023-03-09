package org.simplycodestudio.taskprocessor.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TaskTest {

    @Test
    public void testEquals() {
        Task task1 = new Task("123", "pattern", "input", 0, 0, 0, TaskStatus.IN_PROGRESS);
        Task task2 = new Task("123", "pattern", "input", 0, 0, 0, TaskStatus.IN_PROGRESS);
        Task task3 = new Task("456", "pattern", "input", 0, 0, 0, TaskStatus.IN_PROGRESS);

        assertEquals(task1, task2);
        assertNotEquals(task1, task3);
    }

    @Test
    public void testHashCode() {
        Task task1 = new Task("123", "pattern", "input", 0, 0, 0, TaskStatus.IN_PROGRESS);
        Task task2 = new Task("123", "pattern", "input", 0, 0, 0, TaskStatus.IN_PROGRESS);
        Task task3 = new Task("456", "pattern", "input", 0, 0, 0, TaskStatus.IN_PROGRESS);

        assertEquals(task1.hashCode(), task2.hashCode());
        assertNotEquals(task1.hashCode(), task3.hashCode());
    }
}