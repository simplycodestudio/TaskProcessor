package org.simplycodestudio.taskprocessor.task;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = { "This section contains all Tasks management" })
@RequestMapping("/api/v1/tasks")
@Validated
class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ApiOperation(value = "Create new task", notes = "this method also invokes task processing")
    ResponseEntity<String> createTask(@Valid @RequestParam @NotEmpty String pattern, @Valid @RequestParam @NotEmpty String input) {
        return ResponseEntity.ok(taskService.createTask(pattern, input));
    }

    @PutMapping("/delay")
    @ApiOperation(value = "Adding delay for task processing",
            notes = "This method allows to add delay for task processing. input requires milliseconds. In fact of delay is being using in calculation loop, it may be invoked more than once, significantly slowing down the processing task. Multiplication of delays depends mainly on input and pattern length. Default value is 0")
    ResponseEntity<String> addDelay(@RequestParam(defaultValue = "0") int delay) {
        taskService.addDelay(delay);
        return ResponseEntity.ok(String.format("Delay was set to %d ms", delay));
    }

    @GetMapping
    @Cacheable(value = "tasks")
    @ApiOperation(value = "returning current tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    @Cacheable(value = "tasks", key = "#id")
    @ApiOperation(value = "returning task with given id")
    public ResponseEntity<Task> getTask(@PathVariable String id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @GetMapping("/{id}/progress")
    @Cacheable(value = "taskProgress", key = "#id")
    @ApiOperation(value = "returning task progress", notes = "method is returning percentage value, eg.:50%")
    public ResponseEntity<String> getTaskStatus(@PathVariable String id) {
        return ResponseEntity.ok(String.format("%d%%", taskService.getTaskProgress(id)));
    }

    @GetMapping("/{id}/result")
    @Cacheable(value = "taskResults", key = "#id")
    @ApiOperation(value = "returning task processing result")
    public ResponseEntity<TaskResult> getTaskResult(@PathVariable String id) {
        return ResponseEntity.ok(taskService.getTaskResult(id));
    }
}