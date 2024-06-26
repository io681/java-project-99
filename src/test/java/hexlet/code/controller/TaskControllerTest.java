package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.taskDTO.TaskCreateDTO;
import hexlet.code.dto.taskDTO.TaskDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    private Task testTask = new Task();
    private User testUser;
    private TaskStatus testTaskStatus;
    private Label testLabel = new Label();
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor tokenAdmin;

    @BeforeEach
    public void setUpTest() {
        tokenAdmin = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

        testTaskStatus = taskStatusRepository.findBySlug("to_review").get();

        testUser = Instancio.of(modelGenerator.getUserModel())
                .create();
        userRepository.save(testUser);

        testLabel.setName("testLabel");
        labelRepository.save(testLabel);

        testTask.setName("testName1");
        testTask.setIndex(1);
        testTask.setDescription("test description 1");
        testTask.setTaskStatus(testTaskStatus);
        testTask.setAssignee(testUser);
        testTask.setLabels(new ArrayList<>());
        testTask.getLabels().add(testLabel);
    }

    @Test
    public void testIndex() throws Exception {
        taskRepository.save(testTask);

        var testTask2 = new Task();
        testTask2.setName("testName1");
        testTask2.setIndex(2);
        testTask2.setDescription("test description 1");
        testTask2.setTaskStatus(testTaskStatus);
        testTask2.setAssignee(testUser);
        taskRepository.save(testTask2);

        MockHttpServletRequestBuilder request = get("/api/tasks").with(tokenAdmin);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        testTask = taskRepository.save(testTask);

        MockHttpServletRequestBuilder request = get("/api/tasks/{id}", testTask.getId()).with(tokenAdmin);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("status").isEqualTo(testTask.getTaskStatus().getSlug()),
                v -> v.node("assignee_id").isEqualTo(testTask.getAssignee().getId()),
                v -> v.node("taskLabelIds[0]").isEqualTo(testTask.getLabels().get(0).getId())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var testDTO = new TaskCreateDTO();
        testDTO.setTitle("testName1");
        testDTO.setIndex(1);
        testDTO.setContent("test description 1");
        testDTO.setStatus(testTaskStatus.getSlug());
        testDTO.setAssigneeId(testUser.getId());
        testDTO.setTaskLabelIds(new ArrayList<>());
        testDTO.getTaskLabelIds().add(testLabel.getId());

        var request = post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testDTO))
                .with(tokenAdmin);
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var expectedTask = taskRepository.findByName(testDTO.getTitle()).get();

        assertThat(expectedTask).isNotNull();
        assertThat(expectedTask.getName()).isEqualTo(testDTO.getTitle());
        assertThat(expectedTask.getAssignee().getId()).isEqualTo(testDTO.getAssigneeId());
        assertThat(expectedTask.getLabels().get(0).getId()).isEqualTo(testDTO.getTaskLabelIds().get(0));

    }

    @Test
    public void testUpdate() throws Exception {
        taskRepository.save(testTask);

        TaskDTO dto = taskMapper.map(testTask);
        dto.setTitle("newTitle");
        dto.setContent("New content");

        Label testLabel2 = new Label();
        testLabel2.setName("New testLabel");
        labelRepository.save(testLabel2);

        dto.setTaskLabelIds(new ArrayList<>());
        dto.getTaskLabelIds().add(testLabel2.getId());

        var request = put("/api/tasks/{id}", testTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto))
                .with(tokenAdmin);
        mockMvc.perform(request)
                .andExpect(status().isOk());

        Task expectedTask = taskRepository.findById(testTask.getId()).get();

        assertThat(expectedTask.getName()).isEqualTo(dto.getTitle());
        assertThat(expectedTask.getDescription()).isEqualTo(dto.getContent());
        assertThat(expectedTask.getLabels().get(0).getId()).isEqualTo(dto.getTaskLabelIds().get(0));
    }

    @Test
    public void testDelete() throws Exception {
        taskRepository.save(testTask);

        var request = delete("/api/tasks/{id}", testTask.getId()).with(tokenAdmin);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(taskRepository.existsById(testTask.getId())).isEqualTo(false);
    }

    @AfterEach
    public void cleanData() {
        taskRepository.deleteAll();
        userRepository.deleteById(testUser.getId());
        labelRepository.deleteAll();
    }
}
