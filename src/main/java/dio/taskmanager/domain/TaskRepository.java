package dio.taskmanager.domain;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();
    Task save(Task task);
    Optional<Task> findById(TaskId id);
    void delete(TaskId id);
}
