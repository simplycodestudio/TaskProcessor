package org.simplycodestudio.taskprocessor.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.simplycodestudio.taskprocessor.fixtures.TaskDeliver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_create_multiple_tasks_with_expected_results() {
        //When
        List<String> createdTasksIds = TaskDeliver.PREDEFINED_TASK_REQUESTS.stream()
                .map(taskRequest -> {
                    try {
                        return mockMvc.perform(post("/api/v1/tasks")
                                        .param("pattern", taskRequest.pattern())
                                        .param("input", taskRequest.input()))
                                .andExpect(status().isOk())
                                .andReturn();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(result -> {
                    try {
                        return result.getResponse().getContentAsString();
                    } catch (UnsupportedEncodingException e) {
                        return "empty";
                    }
                })
                .collect(Collectors.toList());

        List<TaskResult> retrievedTaskResults = createdTasksIds.stream().map(createdTasksId -> {
                    try {
                        String taskResultAsString = mockMvc.perform(get("/api/v1/tasks/" + createdTasksId + "/result"))
                                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
                        ObjectMapper objectMapper = new ObjectMapper();
                        return objectMapper.readValue(taskResultAsString, TaskResult.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        //Then
        Assertions.assertTrue(retrievedTaskResults.size() == TaskDeliver.EXPECTED_TASK_RESULTS.size()
                && retrievedTaskResults.containsAll(TaskDeliver.EXPECTED_TASK_RESULTS)
                && TaskDeliver.EXPECTED_TASK_RESULTS.containsAll(retrievedTaskResults));
    }
}