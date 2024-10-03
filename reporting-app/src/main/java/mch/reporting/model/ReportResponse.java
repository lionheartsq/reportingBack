package mch.reporting.model;

import lombok.*;
import mch.reporting.entity.ReportEntity;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class ReportResponse {

    private Long totalQueryCount;
    private Long totalItemsCount;
    private float percentageCount;
    @Singular
    private List<ReportEntity> reportEntities;
}
