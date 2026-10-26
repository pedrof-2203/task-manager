package dio.taskmanager.infrastructure.http.request;

import dio.taskmanager.application.input.CreateTaskInput;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;

public record CreateTaskRequest(
        @NotBlank
        @Size(min = 4, max = 128)
        String title,
        Optional<@Size(max = 256) String> description
) {
    public CreateTaskInput toInput() {
        return new CreateTaskInput(this.title(), this.description());
    }
}
