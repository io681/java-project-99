package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.util.ModelGenerator;
import org.instancio.Instancio;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest
@AutoConfigureMockMvc
public final class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ModelGenerator modelGenerator;

    private User testUser;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor tokenAdmin;

    @BeforeEach
    public void setUp() {
        tokenAdmin = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testUser = Instancio.of(modelGenerator.getUserModel())
                .create();
//        userRepository.save(testUser);
    }

    @Test
    public void testIndex() throws Exception {
        userRepository.save(testUser);
        var testUser2 = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(testUser2);

        MockHttpServletRequestBuilder request = get("/api/users").with(tokenAdmin);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        userRepository.save(testUser);

        MockHttpServletRequestBuilder request = get("/api/users/{id}", testUser.getId()).with(tokenAdmin);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("firstName").isEqualTo(testUser.getFirstName()),
                v -> v.node("lastName").isEqualTo(testUser.getLastName()),
                v -> v.node("email").isEqualTo(testUser.getEmail())
        );
    }

    @Test
    public void testCreate() throws Exception {

        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testUser))
                .with(tokenAdmin);

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var expectedUser = userRepository.findByEmail(testUser.getEmail()).get();

        assertThat(expectedUser).isNotNull();
        assertThat(expectedUser.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(expectedUser.getLastName()).isEqualTo(testUser.getLastName());
    }

    @Test
    public void testUpdate() throws Exception {
        userRepository.save(testUser);

        var dto = userMapper.map(testUser);

        dto.setFirstName("newFirstName");
        dto.setLastName("newLastName");
        dto.setEmail("newEmail@newEmail.hh");

        var request = put("/api/users/{id}", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto))
                .with(tokenAdmin);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var expectedUser = userRepository.findById(testUser.getId()).get();

        assertThat(expectedUser.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(expectedUser.getLastName()).isEqualTo(dto.getLastName());
        assertThat(expectedUser.getEmail()).isEqualTo(dto.getEmail());
    }


    @Test
    public void testDelete() throws Exception {

        userRepository.save(testUser);

        var request = delete("/api/users/{id}", testUser.getId()).with(tokenAdmin);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(userRepository.existsById(testUser.getId())).isEqualTo(false);
    }
}
