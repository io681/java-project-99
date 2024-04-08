package hexlet.code.app.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.labelDTO.LabelCreateDTO;
import hexlet.code.app.dto.labelDTO.LabelDTO;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
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
public class LabelContollerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelMapper labelMapper;

    private Label testLabel = new Label();

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor tokenAdmin;

    @BeforeEach
    public void setUpTest() {
        tokenAdmin = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testLabel.setName("testName label");
    }

    @Test
    public void testIndex() throws Exception {
        labelRepository.save(testLabel);

        var testLabel2 = new Label();
        testLabel2.setName("testName label 2");
        labelRepository.save(testLabel2);

        MockHttpServletRequestBuilder request = get("/api/labels").with(tokenAdmin);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        labelRepository.save(testLabel);

        MockHttpServletRequestBuilder request = get("/api/labels/{id}", testLabel.getId()).with(tokenAdmin);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testLabel.getName())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var testDTO = new LabelCreateDTO();
        testDTO.setName("new label");

        var request = post("/api/labels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testDTO))
                .with(tokenAdmin);

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var expectedLabel = labelRepository.findByName(testDTO.getName()).get();

        assertThat(expectedLabel).isNotNull();
        assertThat(expectedLabel.getName()).isEqualTo(testDTO.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        labelRepository.save(testLabel);

        LabelDTO dto = labelMapper.map(testLabel);
        dto.setName("label name updated");

        var request = put("/api/labels/{id}", testLabel.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto))
                .with(tokenAdmin);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        Label expectedLabel = labelRepository.findById(testLabel.getId()).get();

        assertThat(expectedLabel.getName()).isEqualTo(dto.getName());
    }

    @Test
    public void testDelete() throws Exception {

        labelRepository.save(testLabel);

        var request = delete("/api/labels/{id}", testLabel.getId()).with(tokenAdmin);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(labelRepository.existsById(testLabel.getId())).isEqualTo(false);
    }

    @AfterEach
    public void cleanData() {
        labelRepository.deleteAll();
    }
}
