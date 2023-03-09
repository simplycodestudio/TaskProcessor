package org.simplycodestudio.taskprocessor.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.simplycodestudio.taskprocessor.task.exceptions.TaskNotReadyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
class TaskService {
    private final TaskRepository taskRepository;
    private final TaskProcessor taskProcessor;
    private int delay = 0;

    String createTask(final String pattern, final String input) {
        String id = UUID.randomUUID().toString();
        Task task = new Task(id, pattern, input, 0, 0, 0, TaskStatus.CREATED);
        taskRepository.save(task);

        log.info("Created task with id {}", id);
        taskProcessor.processTaskAsync(id, delay);

        return id;
    }

    Task getTask(final String id) {
        return taskProcessor.getTask(id);
    }

    int getTaskProgress(final String id) {
        return taskProcessor.getTask(id).getProgress();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true, isolation = Isolation.READ_UNCOMMITTED )
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    TaskResult getTaskResult(final String id) {
        return Stream.of(getTask(id))
                .map(task -> {
                    if (task.getStatus() != TaskStatus.COMPLETED) {
                        throw new TaskNotReadyException(task.getProgress());
                    }
                    return new TaskResult(task.getPosition(), task.getTypos());
                })
                .peek(taskResult -> log.info(taskResult.toString()))
                .findAny()
                .orElse(null);
    }

    void addDelay(final int delay) {
        this.delay = delay;
    }
}
