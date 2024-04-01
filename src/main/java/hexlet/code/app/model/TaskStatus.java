package hexlet.code.app.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "task_statuses")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @NotNull
    @Column(unique = true)
    @Size(min = 1)
    private String name;

    @NotNull
    @Column(unique = true)
    @Size(min = 1)
    private String slug;

    @CreatedDate
    private LocalDateTime createdAt;
}
