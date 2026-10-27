
package dio.taskmanager.infrastructure.http;

import dio.taskmanager.application.*;
import dio.taskmanager.application.output.TaskOutput;
import dio.taskmanager.domain.TaskId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@ExtendWith(RestDocumentationExtension.class)
class TaskControllerTest {

    private CreateTaskUseCase createTaskUseCase;
    private GetTasksUseCase getTasksUseCase;
    private GetTaskByIdUseCase getTaskByIdUseCase;
    private DeleteTaskUseCase deleteTaskUseCase;
    private UpdateTaskUseCase updateTaskUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        createTaskUseCase = mock(CreateTaskUseCase.class);
        getTasksUseCase = mock(GetTasksUseCase.class);
        getTaskByIdUseCase = mock(GetTaskByIdUseCase.class);
        deleteTaskUseCase = mock(DeleteTaskUseCase.class);
        updateTaskUseCase = mock(UpdateTaskUseCase.class);

        var controller = new TaskController(
                createTaskUseCase,
                getTasksUseCase,
                getTaskByIdUseCase,
                deleteTaskUseCase,
                updateTaskUseCase
        );

        mockMvc = standaloneSetup(controller)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void shouldCreateTask() throws Exception {
        when(createTaskUseCase.execute(any()))
                .thenReturn(mock(TaskOutput.class));

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "title": "Study Java",
                              "description": "Learn testing"
                            }
                            """))
                .andExpect(status().isCreated())
                .andDo(document("create-task"));

        verify(createTaskUseCase).execute(any());
    }

    @Test
    void shouldListTasks() throws Exception {
        when(getTasksUseCase.execute())
                .thenReturn(List.of());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andDo(document("list-tasks"));

        verify(getTasksUseCase).execute();
    }

    @Test
    void shouldGetTaskById() throws Exception {
        UUID id = UUID.randomUUID();

        when(getTaskByIdUseCase.execute(any(TaskId.class)))
                .thenReturn(mock(TaskOutput.class));

        mockMvc.perform(get("/tasks/{id}", id))
                .andExpect(status().isOk())
                .andDo(document("get-task",
                        pathParameters(
                                parameterWithName("id")
                                        .description("Task identifier")
                        )
                ));

        verify(getTaskByIdUseCase).execute(any(TaskId.class));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        UUID id = UUID.randomUUID();

        when(updateTaskUseCase.execute(any(TaskId.class), any()))
                .thenReturn(mock(TaskOutput.class));

        mockMvc.perform(patch("/tasks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Study Spring Boot",
                                  "description": "Learn REST Docs"
                                }
                                """))
                .andExpect(status().isOk())
                .andDo(document("update-task",
                        pathParameters(
                                parameterWithName("id")
                                        .description("Task identifier")
                        )
                ));

        verify(updateTaskUseCase)
                .execute(any(TaskId.class), any());
    }

    @Test
    void shouldDeleteTask() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/tasks/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""))
                .andDo(document("delete-task",
                        pathParameters(
                                parameterWithName("id")
                                        .description("Task identifier")
                        )
                ));

        verify(deleteTaskUseCase).execute(any(TaskId.class));
    }
}
