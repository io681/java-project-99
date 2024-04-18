package hexlet.code.controller;

import hexlet.code.dto.labelDTO.LabelCreateDTO;
import hexlet.code.dto.labelDTO.LabelDTO;
import hexlet.code.dto.labelDTO.LabelUpdateDTO;
import hexlet.code.service.LabelService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
@SecurityRequirement(name = "Authorization")
public class LabelController {
    @Autowired
    private LabelService labelService;

    @GetMapping(path = "")
    public ResponseEntity<List<LabelDTO>> index() {
        var result = labelService.index();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    @GetMapping(path = "/{id}")
    public LabelDTO show(@PathVariable Long id) {
        return labelService.show(id);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(@Valid @RequestBody LabelCreateDTO dto) {
        return labelService.create(dto);
    }

    @PutMapping(path = "/{id}")
    public LabelDTO update(@Valid @RequestBody LabelUpdateDTO dto, @PathVariable Long id) {
        return labelService.update(dto, id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        labelService.delete(id);
    }
}
