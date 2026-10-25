package dio.taskmanager.application.output;

import dio.taskmanager.domain.Task;

public record TaskOutput(
        String id,
        String title,
        String description,
        String status) {

    public static TaskOutput from(Task task) {
        return new TaskOutput(
                task.getId().toString(),
                task.getTitle(),
                task.getDescription().orElse(""),
                task.getStatus().name()
        );
    }

}
