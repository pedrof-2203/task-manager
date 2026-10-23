
package dio.taskmanager.domain;

import dio.taskmanager.infrastructure.repositories.InMemoryTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskRepositoryTest {

    protected TaskRepository repository;

    @BeforeEach
    void setUp() {
        repository = createRepository();
    }

    protected abstract TaskRepository createRepository();

    @Test
    void shouldReturnEmptyListWhenNoTasksExist() {
        List<Task> tasks = repository.findAll();

        assertTrue(tasks.isEmpty());
    }

    @Test
    void shouldSaveTask() {
        Task task = createTask();

        Task savedTask = repository.save(task);

        assertSame(task, savedTask);
        assertTrue(repository.findAll().contains(task));
    }

    @Test
    void shouldFindTaskById() {
        Task task = createTask();
        repository.save(task);

        Optional<Task> result = repository.findById(task.getId());

        assertTrue(result.isPresent());
        assertSame(task, result.get());
    }

    @Test
    void shouldReturnEmptyWhenTaskDoesNotExist() {
        Task task = createTask();

        Optional<Task> result = repository.findById(task.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldDeleteTask() {
        Task task = createTask();
        repository.save(task);

        repository.delete(task.getId());

        assertTrue(repository.findById(task.getId()).isEmpty());
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldNotFailWhenDeletingNonexistentTask() {
        Task task = createTask();

        assertDoesNotThrow(() -> repository.delete(task.getId()));
    }

    private Task createTask() {
        return new Task(
                "Test task",
                Optional.of("Test description"),
                TaskStatus.PENDING
        );
    }
}
