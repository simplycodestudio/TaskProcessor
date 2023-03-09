package org.simplycodestudio.taskprocessor.fixtures;

import org.simplycodestudio.taskprocessor.task.Task;
import org.simplycodestudio.taskprocessor.task.TaskResult;
import org.simplycodestudio.taskprocessor.task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDeliver {

    public static Map<Task, Task> PREDEFINED_TASK_CASES_WITH_EXPECTED_RESULTS = new HashMap<>();

    static {
        PREDEFINED_TASK_CASES_WITH_EXPECTED_RESULTS.put(new Task("d0ebb437-a6c8-48ae-8791-23b77d7aadfa", "BCD", "ABCD", 0, 0, 0, TaskStatus.CREATED), new Task("d0ebb437-a6c8-48ae-8791-23b77d7aadfa", "BCD", "ABCD", 1, 0, 100, TaskStatus.COMPLETED));
        PREDEFINED_TASK_CASES_WITH_EXPECTED_RESULTS.put(new Task("4b8ba998-f9bf-49bc-98f8-eb526825b1fb", "BWD", "ABCD", 0, 0, 0, TaskStatus.CREATED), new Task("4b8ba998-f9bf-49bc-98f8-eb526825b1fb", "BWD", "ABCD", 1, 1, 100, TaskStatus.COMPLETED));
        PREDEFINED_TASK_CASES_WITH_EXPECTED_RESULTS.put(new Task("dc90f9dc-399b-4602-925a-4c1c19894e3c", "CFG", "ABCDEFG", 0, 0, 0, TaskStatus.CREATED), new Task("dc90f9dc-399b-4602-925a-4c1c19894e3c", "CFG", "ABCDEFG", 4, 1, 100, TaskStatus.COMPLETED));
        PREDEFINED_TASK_CASES_WITH_EXPECTED_RESULTS.put(new Task("184ace43-94f5-4cf8-8323-6f7a2f8dea60", "ABC", "ABCABC", 0, 0, 0, TaskStatus.CREATED), new Task("184ace43-94f5-4cf8-8323-6f7a2f8dea60", "ABC", "ABCABC", 0, 0, 100, TaskStatus.COMPLETED));
        PREDEFINED_TASK_CASES_WITH_EXPECTED_RESULTS.put(new Task("29ff5a04-4851-407d-9705-6cd79b02554e", "TDD", "ABCDEFG", 0, 0, 0, TaskStatus.CREATED), new Task("29ff5a04-4851-407d-9705-6cd79b02554e", "TDD", "ABCDEFG", 1, 2, 100, TaskStatus.COMPLETED));
    }

    public static List<TaskRequest> PREDEFINED_TASK_REQUESTS = new ArrayList<>();

    static {
        PREDEFINED_TASK_REQUESTS.add(new TaskRequest("ABCD", "BCD"));
        PREDEFINED_TASK_REQUESTS.add(new TaskRequest("ABCD", "BWD"));
        PREDEFINED_TASK_REQUESTS.add(new TaskRequest("ABCDEFG", "CFG"));
        PREDEFINED_TASK_REQUESTS.add(new TaskRequest("ABCABC", "ABC"));
        PREDEFINED_TASK_REQUESTS.add(new TaskRequest("ABCDEFG", "TDD"));
    }

    public static List<TaskResult> EXPECTED_TASK_RESULTS = new ArrayList<>();

    static {
        EXPECTED_TASK_RESULTS.add(new TaskResult(1, 0));
        EXPECTED_TASK_RESULTS.add(new TaskResult(1, 1));
        EXPECTED_TASK_RESULTS.add(new TaskResult(4, 1));
        EXPECTED_TASK_RESULTS.add(new TaskResult(0, 0));
        EXPECTED_TASK_RESULTS.add(new TaskResult(1, 2));
    }

}
