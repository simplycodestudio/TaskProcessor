package org.simplycodestudio.taskprocessor.task;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Task {
    @Id
    private String id;
    private String pattern;
    private String input;
    private int position;
    private int typos;
    private int progress;
    private TaskStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return position == task.position &&
                typos == task.typos &&
                progress == task.progress &&
                Objects.equals(id, task.id) &&
                Objects.equals(pattern, task.pattern) &&
                Objects.equals(input, task.input) &&
                status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pattern, input, position, typos, progress, status);
    }

    @Override
    public String toString() {
        return String.format("Task{id='%s', pattern='%s', input='%s', position=%d, typos=%d, progress=%d, status=%s}",
                id, pattern, input, position, typos, progress, status);
    }
}