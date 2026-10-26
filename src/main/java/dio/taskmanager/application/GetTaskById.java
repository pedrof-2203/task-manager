package dio.taskmanager.application;

import dio.taskmanager.application.output.TaskOutput;
import dio.taskmanager.domain.TaskId;
import dio.taskmanager.domain.TaskRepository;

import java.util.NoSuchElementException;

public class GetTaskById {
    private final TaskRepository repository;

    public GetTaskById(TaskRepository repository) {
        this.repository = repository;
    }

    TaskOutput execute(TaskId id) {
        return repository.findById(id)
                .map(TaskOutput::from)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
    }
}
