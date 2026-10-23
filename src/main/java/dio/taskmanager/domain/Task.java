package dio.taskmanager.domain;

import org.springframework.util.Assert;

import java.util.Optional;

public class Task {
    private final TaskId id;
    private final String title;
    private final Optional<String> description;
    private final TaskStatus status;

    public Task(String title, Optional<String> description, TaskStatus status) {
        Assert.notNull(title, "Title must not be empty");

        this.id = new TaskId();
        this.title = title;
        this.description = description;
        this.status = TaskStatus.PENDING;
    }
}
