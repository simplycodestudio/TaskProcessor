package org.simplycodestudio.taskprocessor.task;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.simplycodestudio.taskprocessor.fixtures.TaskDeliver;
import org.simplycodestudio.taskprocessor.task.exceptions.IncompleteTaskRequestException;
import org.simplycodestudio.taskprocessor.task.exceptions.NoTaskFoundException;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskProcessorTest {

    @Mock
    private TaskRepository taskRepository;

    @Test
    public void should_process_with_success_task_with_correct_input_and_pattern() {
        // Given
        String taskId = "0b837eb4-1f28-4584-875f-c25e4f11f2d4";
        String pattern = "BCD";
        String input = "ABCD";
        int delay = 0;
        Task task = new Task(taskId, pattern, input, 0, 0, 0, TaskStatus.CREATED);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        TaskProcessor taskProcessor = new TaskProcessor(taskRepository);

        // When
        taskProcessor.processTaskAsync(taskId, delay);

        // Then
        verify(taskRepository, times(2)).flush();
        assertEquals(0, task.getTypos());
        assertEquals(1, task.getPosition());
        assertEquals(100, task.getProgress());
        assertEquals(TaskStatus.COMPLETED, task.getStatus());
    }

    @Test
    public void should_process_with_success_task_with_incorrect_input_and_pattern() {
        // Given
        String taskId = "0b837eb4-1f28-4584-875f-c25e4f11f2d4";
        String pattern = null;
        String input = null;
        int delay = 0;
        Task task = new Task(taskId, pattern, input, 0, 0, 0, TaskStatus.CREATED);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        TaskProcessor taskProcessor = new TaskProcessor(taskRepository);

        // Then
        assertThrows(IncompleteTaskRequestException.class, () -> taskProcessor.processTaskAsync(taskId, delay));
    }

    @Test
    public void should_pass_when_all_predefined_results_matches_with_expected_set() {
        // Given
        Map<Task, Task> predefinedTaskCasesWithExpectedResults = TaskDeliver.PREDEFINED_TASK_CASES_WITH_EXPECTED_RESULTS;
        int delay = 0;
        predefinedTaskCasesWithExpectedResults.forEach((key, value) -> when(taskRepository.findById(key.getId())).thenReturn(Optional.of(key)));
        TaskProcessor taskProcessor = new TaskProcessor(taskRepository);

        // When
        predefinedTaskCasesWithExpectedResults.forEach((key, value) -> taskProcessor.processTaskAsync(key.getId(), delay));

        //then
        predefinedTaskCasesWithExpectedResults.forEach((key, value) -> assertEquals(taskRepository.findById(key.getId()).get(), value));
    }

    @Test
    public void testProcessTaskAsyncWithInvalidId() {
        // Given
        String taskId = "invalid";
        int delay = 0;
        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(null));
        TaskProcessor taskProcessor = new TaskProcessor(taskRepository);

        // Then
        assertThrows(NoTaskFoundException.class, () -> taskProcessor.processTaskAsync(taskId, delay));
    }

    @Test
    public void should_check_if_algorithm_can_process_task_with_long_input() {
        //Given
        String taskId = "0b837eb4-1f28-4584-875f-c25e4f11f2d4";
        int delay = 0;

        String input = StringUtils.repeat("abcdefghijklmnopqrstuvwxyz", 100);
        String pattern = StringUtils.repeat("zyx", 50);
        Task task = new Task(taskId, pattern, input, 0, 0, 0, TaskStatus.CREATED);
        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(task));
        TaskProcessor taskProcessor = new TaskProcessor(taskRepository);
        //When
        taskProcessor.processTaskAsync(task.getId(), delay);

        //Then
        Task updatedTask = taskRepository.findById(task.getId()).orElse(null);
        assertNotNull(updatedTask);
        assertEquals(TaskStatus.COMPLETED, updatedTask.getStatus());
        assertEquals(100, updatedTask.getProgress());
        assertTrue(updatedTask.getPosition() >= 0);
        assertTrue(updatedTask.getTypos() >= 0);

    }

    @Test
    void should_retrieve_task_error_when_process_is_interrupted() throws InterruptedException {
        // Arrange
        String taskId = "0b837eb4-1f28-4584-875f-c25e4f11f2d4";
        String pattern = "BCD";
        String input = "ABCD";
        int delay = 1000;
        Task task = new Task(taskId, pattern, input, 0, 0, 0, TaskStatus.CREATED);
        taskRepository.save(task);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        TaskProcessor taskProcessor = new TaskProcessor(taskRepository);
        //When
        taskProcessor.processTaskAsync(task.getId(), delay);

        // Act
        // Start the task processing in a new thread and interrupt it after 1 second
        Thread thread = new Thread(() -> taskProcessor.processTaskAsync(taskId, delay));
        thread.start();
        Thread.sleep(100);
        thread.interrupt();

        // Assert
        // The task status should be set to ERROR
        Task savedTask = taskRepository.findById(taskId).orElse(null);
        assertNotNull(savedTask);
        assertEquals(TaskStatus.ERROR, savedTask.getStatus());
    }

}