package org.simplycodestudio.taskprocessor.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.simplycodestudio.taskprocessor.task.exceptions.IncompleteTaskRequestException;
import org.simplycodestudio.taskprocessor.task.exceptions.NoTaskFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@EnableAsync
@Slf4j
class TaskProcessor {
    private final TaskRepository taskRepository;

    @Async("threadPoolTaskExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processTaskAsync(final String id, final int delay) {
        Task task = getTask(id);

        task.setStatus(TaskStatus.IN_PROGRESS);
        taskRepository.flush();

        String pattern = task.getPattern();
        String input = task.getInput();
        if (StringUtils.isAnyBlank(pattern,input)){
            task.setStatus(TaskStatus.ERROR);
            throw new IncompleteTaskRequestException();
        }

        int minTypos = Integer.MAX_VALUE;
        int bestPosition = -1;
        for (int i = 0; i <= input.length() - pattern.length(); i++) {
            int typos = 0;
            for (int j = 0; j < pattern.length(); j++) {
                if (input.charAt(i + j) != pattern.charAt(j)) {
                    typos++;
                }
            }
            if (typos < minTypos) {
                minTypos = typos;
                bestPosition = i;
            }
            int progress = (int) Math.floor((1.0 - (double) minTypos / pattern.length()) * 100.0);
            if (progress > task.getProgress()) {
                task.setProgress(progress);
                log.info("task {} - progress {}%", task.getId(), task.getProgress());
                taskRepository.flush();
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                task.setStatus(TaskStatus.ERROR);
                return;
            }
        }
        task.setPosition(bestPosition);
        task.setTypos(minTypos);
        task.setProgress(100);
        task.setStatus(TaskStatus.COMPLETED);
        log.info("task {} has done. position: {}, typos: {}", task.getId(), task.getPosition(), task.getTypos());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public Task getTask(final String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoTaskFoundException(id));
    }
}