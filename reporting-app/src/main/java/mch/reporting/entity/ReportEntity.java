package mch.reporting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name= "report_event", schema = "test_support")
@Data
public class ReportEntity {
    @Id
    private Long id;
    private String channelName;
    private String eventPayload;
    private String errorBody;
    private LocalDateTime createdAt;
}
