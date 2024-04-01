package hexlet.code.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
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
public class TaskStatusControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    private final TaskStatus testTaskStatus = new TaskStatus();
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor tokenAdmin;

    @BeforeEach
    public void setUp() {
        tokenAdmin = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testTaskStatus.setName("testName1");
        testTaskStatus.setSlug("testSlug1");
    }

    @Test
    public void testIndex() throws Exception {
        taskStatusRepository.save(testTaskStatus);

        var testTaskStatus2 = new TaskStatus();
        testTaskStatus2.setName("testName2");
        testTaskStatus2.setSlug("testSlug2");
        taskStatusRepository.save(testTaskStatus2);

        MockHttpServletRequestBuilder request = get("/api/task_statuses").with(tokenAdmin);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        taskStatusRepository.save(testTaskStatus);

        MockHttpServletRequestBuilder request = get("/api/task_statuses/{id}", testTaskStatus.getId()).with(tokenAdmin);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testTaskStatus.getName()),
                v -> v.node("slug").isEqualTo(testTaskStatus.getSlug())
        );
    }

    @Test
    public void testCreate() throws Exception {

        var request = post("/api/task_statuses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testTaskStatus))
                .with(tokenAdmin);

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var expectedTaskStatus = taskStatusRepository.findBySlug(testTaskStatus.getSlug()).get();

        assertThat(expectedTaskStatus).isNotNull();
        assertThat(expectedTaskStatus.getName()).isEqualTo(testTaskStatus.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        taskStatusRepository.save(testTaskStatus);

        var dto = taskStatusMapper.map(testTaskStatus);

        dto.setName("newName");
        dto.setSlug("newSlug");

        var request = put("/api/task_statuses/{id}", testTaskStatus.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto))
                .with(tokenAdmin);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var expectedTaskStatus = taskStatusRepository.findById(testTaskStatus.getId()).get();

        assertThat(expectedTaskStatus.getName()).isEqualTo(dto.getName());
        assertThat(expectedTaskStatus.getSlug()).isEqualTo(dto.getSlug());
    }

    @Test
    public void testDelete() throws Exception {

        taskStatusRepository.save(testTaskStatus);

        var request = delete("/api/task_statuses/{id}", testTaskStatus.getId()).with(tokenAdmin);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(taskStatusRepository.existsById(testTaskStatus.getId())).isEqualTo(false);
    }

    @AfterEach
    public void cleanData() {
        taskStatusRepository.deleteAll();
    }
}
