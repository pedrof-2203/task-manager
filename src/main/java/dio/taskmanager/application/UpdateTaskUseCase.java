package dio.taskmanager.application;

import dio.taskmanager.application.input.UpdateTaskInput;
import dio.taskmanager.application.output.TaskOutput;
import dio.taskmanager.domain.TaskId;
import dio.taskmanager.domain.TaskNotFoundException;
import dio.taskmanager.domain.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateTaskUseCase {
    private final TaskRepository repository;

    public UpdateTaskUseCase(TaskRepository repository) {
        this.repository = repository;
    }

    public TaskOutput execute(TaskId taskId, UpdateTaskInput input) {
        var task = repository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        task.update(input.title(), input.description(), input.status());
        var updatedTask = repository.save(task);
        return TaskOutput.from(updatedTask);
    }
}
