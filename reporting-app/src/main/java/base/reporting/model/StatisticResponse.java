package mch.reporting.model;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class StatisticResponse {
    private Long totalItemsCount;
    private Long totalErrorsCount;
    private Long totalSuccessCount;
    private Long totalPropertyCount;
    private Long totalResidentCount;
    private Long totalPropertyDlqCount;
    private Long totalResidentDlqCount;
    private float percentageErrorsCount;
    private float percentageSuccessCount;
    private float percentagePropertyCount;
    private float percentageResidentCount;
    private float percentagePropertyDlqCount;
    private float percentageResidentDlqCount;
}
