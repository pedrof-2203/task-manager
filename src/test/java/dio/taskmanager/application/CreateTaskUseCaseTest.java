
package dio.taskmanager.application;

import dio.taskmanager.application.input.CreateTaskInput;
import dio.taskmanager.application.output.TaskOutput;
import dio.taskmanager.domain.Task;
import dio.taskmanager.domain.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateTaskUseCaseTest {

    private TaskRepository repository;
    private CreateTaskUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(TaskRepository.class);
        useCase = new CreateTaskUseCase(repository);
    }

    @Test
    void shouldSaveTaskAndReturnTaskOutput() {
        // Arrange
        var input = new CreateTaskInput(
                "Study Java",
                Optional.of("Learn unit testing")
        );

        var savedTask = new Task(
                input.title(),
                input.description()
        );

        when(repository.save(any(Task.class)))
                .thenReturn(savedTask);

        // Act
        var output = useCase.execute(input);

        // Assert
        assertNotNull(output);

        verify(repository).save(argThat(task ->
                task.getTitle().equals(input.title())
                        && task.getDescription().equals(input.description())
        ));
    }

    @Test
    void shouldReturnOutputMappedFromSavedTask() {
        // Arrange
        var input = new CreateTaskInput(
                "Study Java",
                Optional.of("Practice Mockito")
        );

        var savedTask = new Task(
                input.title(),
                input.description()
        );

        when(repository.save(any(Task.class)))
                .thenReturn(savedTask);

        var expectedOutput = TaskOutput.from(savedTask);

        // Act
        var output = useCase.execute(input);

        // Assert
        assertEquals(expectedOutput, output);
    }

    @Test
    void shouldSaveTaskExactlyOnce() {
        // Arrange
        var input = new CreateTaskInput(
                "Write tests",
                Optional.of("Test the use case")
        );

        when(repository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        useCase.execute(input);

        // Assert
        verify(repository, times(1)).save(any(Task.class));
        verifyNoMoreInteractions(repository);
    }
}
