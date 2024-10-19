package mch.reporting.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mch.reporting.entity.ReportEntity;
import mch.reporting.model.ReportResponse;
import mch.reporting.model.StatisticResponse;
import mch.reporting.repository.ReportRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportingServiceImpl implements ReportingService{

    private final ReportRepository reportRepository;

    // Helper method to build ReportResponse
    private Mono<ReportResponse> buildReportResponse(Mono<Long> specificCountMono, Mono<Long> totalCountMono, List<ReportEntity> reportList) {
        return Mono.zip(
                        specificCountMono.defaultIfEmpty(0L),
                        totalCountMono.defaultIfEmpty(0L)
                )
                .flatMap(tuple -> {
                    Long specificCount = tuple.getT1();
                    Long totalCount = tuple.getT2();
                    float percentage = (totalCount != 0) ? ((float) (specificCount * 100) / totalCount) : 0.0f;

                    // Build the response
                    ReportResponse reportResponse = ReportResponse.builder()
                            .totalQueryCount(specificCount)
                            .totalItemsCount(totalCount)
                            .percentageCount(percentage)
                            .reportEntities(reportList)
                            .build();

                    return Mono.just(reportResponse);
                });
    }

    // Get all registers
    @Override
    public Mono<ReportResponse> getAllReports() {
        return getAllReportsFlux()  // Calling method brings all reports
                .collectList()  // Group all reports into a list
                .flatMap(reportList ->
                        buildReportResponse(
                                Mono.just((long) reportList.size()),  // Total amount of obtained reports in list
                                countAllReports(),  // Total amount of db registers
                                reportList
                        )
                );
    }

    @Override
    public Flux<ReportEntity> getAllReportsFlux() {
        return Flux.fromIterable(reportRepository.findAll());
    }

    @Override
    public Mono<Long> countAllReports() {
        return Mono.just(reportRepository.count());
    }

    // Get registers between two dates
    @Override
    public Mono<ReportResponse> getByDateBetween(String dateStart, String dateEnd) {
        return getAllReportsByDateBetween(dateStart, dateEnd)
                .collectList()
                .flatMap(reportList ->
                        buildReportResponse(
                                countAllReportsByDateBetween(dateStart, dateEnd),
                                countAllReports(),
                                reportList
                        )
                );
    }

    @Override
    public Flux<ReportEntity> getAllReportsByDateBetween(String start, String end) {
        LocalDate localDateStart = LocalDate.parse(start, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime startOfDate = localDateStart.atStartOfDay();

        LocalDate localDateEnd = LocalDate.parse(end, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime endOfDate = localDateEnd.atTime(23,59,59);

        return Flux.fromIterable(reportRepository.findAllByCreatedAtBetween(startOfDate, endOfDate));
    }

    @Override
    public Mono<Long> countAllReportsByDateBetween(String start, String end) {
        LocalDate localDateStart = LocalDate.parse(start, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime startOfDate = localDateStart.atStartOfDay();

        LocalDate localDateEnd = LocalDate.parse(end, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime endOfDate = localDateEnd.atTime(23,59,59);

        return Mono.just(reportRepository.countAllByCreatedAtBetween(startOfDate, endOfDate));
    }

    // Get registers by Id
    @Override
    public Mono<ReportResponse> getById(Long id) {
        return getAllReportsOptional(id)
                .flatMap(reportEntity ->
                        buildReportResponse(
                                countById(id),
                                countAllReports(),
                                List.of(reportEntity)
                        )
                ).switchIfEmpty(Mono.defer(() ->
                        buildReportResponse(
                                Mono.just(0L),
                                countAllReports(),
                                Collections.emptyList()
                        )
                ));
    }

    @Override
    public Mono<ReportEntity> getAllReportsOptional(Long id) {
        return Mono.justOrEmpty(reportRepository.findById(id));
    }

    @Override
    public Mono<Long> countById(Long id) {
        return Mono.just(reportRepository.countAllById(id));
    }

    // Get registers by ChannelName
    @Override
    public Mono<ReportResponse> getAllReportsByChannelName(String channelName) {
        return getAllReportsByChannelNameFlux(channelName)
                .collectList()
                .flatMap(reportList ->
                        buildReportResponse(
                                countAllReportsByChannelName(channelName),
                                countAllReports(),
                                reportList
                        )
                );
    }

    @Override
    public Flux<ReportEntity> getAllReportsByChannelNameFlux(String channelName) {
        return Flux.fromIterable(reportRepository.findAllByChannelNameEqualsIgnoreCase(channelName));
    }

    @Override
    public Mono<Long> countAllReportsByChannelName(String channelName) {
        return Mono.just(reportRepository.countAllByChannelNameEqualsIgnoreCase(channelName));
    }

    // Get all registers by Date
    public Mono<ReportResponse> getAllReportsByDate(String date) {
        return getAllReportsByDateFlux(date)
                .collectList()
                .flatMap(reportList ->
                        buildReportResponse(
                                countAllReportsByDate(date),
                                countAllReports(),
                                reportList
                        )
                );
    }

    @Override
    public Flux<ReportEntity> getAllReportsByDateFlux(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime startOfDate = localDate.atStartOfDay();
        LocalDateTime endOfDate = localDate.atTime(23,59,59);

        return Flux.fromIterable(reportRepository.findAllByCreatedAtBetween(startOfDate, endOfDate));
    }

    @Override
    public Mono<Long> countAllReportsByDate(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime startOfDate = localDate.atStartOfDay();
        LocalDateTime endOfDate = localDate.atTime(23,59,59);

        return Mono.just(reportRepository.countAllByCreatedAtBetween(startOfDate, endOfDate));
    }

    // Get all registers by Error
    public Mono<ReportResponse> getAllReportsByErrorBody(String errorMessageBody) {
        return getAllReportsByErrorBodyFlux(errorMessageBody)
                .collectList()
                .flatMap(reportList ->
                        buildReportResponse(
                                countAllReportsByErrorBody(errorMessageBody),
                                countAllReports(),
                                reportList
                        )
                );
    }

    @Override
    public Flux<ReportEntity> getAllReportsByErrorBodyFlux(String errorMessageBody) {
        return Flux.fromIterable(reportRepository.findAllByErrorBodyContainingIgnoreCase(errorMessageBody));
    }

    @Override
    public Mono<Long> countAllReportsByErrorBody(String errorMessageBody) {
        return Mono.just(reportRepository.countAllByErrorBodyContainingIgnoreCase(errorMessageBody));
    }

    // Get all registers by Success
    public Mono<ReportResponse> getAllReportsByErrorBodySuccess(String errorMessageBody) {
        return getAllReportsByErrorBodySuccessFlux(errorMessageBody)
                .collectList()
                .flatMap(reportList ->
                        buildReportResponse(
                                countAllReportsByErrorBodySuccess(errorMessageBody),
                                countAllReports(),
                                reportList
                        )
                );
    }

    @Override
    public Flux<ReportEntity> getAllReportsByErrorBodySuccessFlux(String errorMessageBody) {
        return Flux.fromIterable(reportRepository.findAllByErrorBodyEquals(errorMessageBody));
    }

    @Override
    public Mono<Long> countAllReportsByErrorBodySuccess(String errorMessageBody) {
        return Mono.just(reportRepository.countAllByErrorBodyEquals(errorMessageBody));
    }

    // Get all registers by Payload
    public Mono<ReportResponse> getAllReportsByEventPayload(String eventPayload) {
        return getAllReportsByEventPayloadFlux(eventPayload)
                .collectList()
                .flatMap(reportList ->
                        buildReportResponse(
                                countAllReportsByEventPayload(eventPayload),
                                countAllReports(),
                                reportList
                        )
                );
    }

    @Override
    public Flux<ReportEntity> getAllReportsByEventPayloadFlux(String eventPayload) {
        return Flux.fromIterable(reportRepository.findAllByEventPayloadContainingIgnoreCase(eventPayload));
    }

    @Override
    public Mono<Long> countAllReportsByEventPayload(String eventPayload) {
        return Mono.just(reportRepository.countAllByEventPayloadContainingIgnoreCase(eventPayload));
    }

    //Capture statistics
    @Override
    public Mono<StatisticResponse> getLastWeekStatistics() {
        // Obtains last registered date
        Mono<LocalDate> lastDateMono = getLastReportDate();

        return lastDateMono.flatMap(lastDate -> {
            // Calculate last week range
            LocalDateTime endOfDate = lastDate.atTime(23, 59, 59);
            LocalDateTime startOfDate = lastDate.minusDays(6).atStartOfDay();

            // Queries for type:
            Mono<Long> errorCountMono = countAllReportsByErrorBodyForDate(startOfDate, endOfDate);
            Mono<Long> successCountMono = countAllReportsByErrorBodySuccessForDate(startOfDate, endOfDate);
            Mono<Long> channelPropertyCountMono = countAllReportsByChannelNameForDate("mch-property-synch:sms-property", startOfDate, endOfDate);
            Mono<Long> channelResidentCountMono = countAllReportsByChannelNameForDate("mch-resident-synch:sms-resident", startOfDate, endOfDate);
            Mono<Long> channelPropertyDlqCountMono = countAllReportsByChannelNameForDate("mch-property-synch.sms-property.DLQ", startOfDate, endOfDate);
            Mono<Long> channelResidentDlqCountMono = countAllReportsByChannelNameForDate("mch-resident-synch.sms-resident.DLQ", startOfDate, endOfDate);
            Mono<Long> totalCountMono = countAllReportsForDate(startOfDate, endOfDate);

            // Combine queries
            return Mono.zip(errorCountMono, successCountMono, channelPropertyCountMono, channelResidentCountMono, channelPropertyDlqCountMono, channelResidentDlqCountMono, totalCountMono)
                    .flatMap(tuple -> {
                        Long errorCount = tuple.getT1();
                        Long successCount = tuple.getT2();
                        Long propertyCount = tuple.getT3();
                        Long residentCount = tuple.getT4();
                        Long propertyDlqCount = tuple.getT5();
                        Long residentDlqCount = tuple.getT6();
                        Long totalCount = tuple.getT7();

                        // Calculate percentages
                        float errorPercentage = (totalCount != 0) ? ((float) (errorCount * 100) / totalCount) : 0.0f;
                        float successPercentage = (totalCount != 0) ? ((float) (successCount * 100) / totalCount) : 0.0f;
                        float propertyPercentage = (totalCount != 0) ? ((float) (propertyCount * 100) / totalCount) : 0.0f;
                        float residentPercentage = (totalCount != 0) ? ((float) (residentCount * 100) / totalCount) : 0.0f;
                        float propertyDlqPercentage = (totalCount != 0) ? ((float) (propertyDlqCount * 100) / totalCount) : 0.0f;
                        float residentDlqPercentage = (totalCount != 0) ? ((float) (residentDlqCount * 100) / totalCount) : 0.0f;

                        // Build the response
                        StatisticResponse reportResponse = StatisticResponse.builder()
                                .totalItemsCount(totalCount)
                                .totalErrorsCount(errorCount)
                                .totalSuccessCount(successCount)
                                .totalPropertyCount(propertyCount)
                                .totalResidentCount(residentCount)
                                .totalPropertyDlqCount(propertyDlqCount)
                                .totalResidentDlqCount(residentDlqCount)
                                .percentageErrorsCount(errorPercentage)
                                .percentageSuccessCount(successPercentage)
                                .percentagePropertyCount(propertyPercentage)
                                .percentageResidentCount(residentPercentage)
                                .percentagePropertyDlqCount(propertyDlqPercentage)
                                .percentageResidentDlqCount(residentDlqPercentage)
                                .build();
                        return Mono.just(reportResponse);
                    });
        });
    }

    @Override
    public Mono<StatisticResponse> getStatisticsForDateRange(String startDate, String endDate) {
        LocalDate localDate = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime startOfDate = localDate.atStartOfDay();

        LocalDate localDateEnd = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime endOfDate = localDateEnd.atTime(23, 59, 59);

        // Queries for type:
        Mono<Long> errorCountMono = countAllReportsByErrorBodyForDate(startOfDate, endOfDate);
        Mono<Long> successCountMono = countAllReportsByErrorBodySuccessForDate(startOfDate, endOfDate);
        Mono<Long> channelPropertyCountMono = countAllReportsByChannelNameForDate("mch-property-synch:sms-property", startOfDate, endOfDate);
        Mono<Long> channelResidentCountMono = countAllReportsByChannelNameForDate("mch-resident-synch:sms-resident", startOfDate, endOfDate);
        Mono<Long> channelPropertyDlqCountMono = countAllReportsByChannelNameForDate("mch-property-synch.sms-property.DLQ", startOfDate, endOfDate);
        Mono<Long> channelResidentDlqCountMono = countAllReportsByChannelNameForDate("mch-resident-synch.sms-resident.DLQ", startOfDate, endOfDate);
        Mono<Long> totalCountMono = countAllReportsForDate(startOfDate, endOfDate);

        // Combine queries
        return Mono.zip(errorCountMono, successCountMono, channelPropertyCountMono, channelResidentCountMono, channelPropertyDlqCountMono, channelResidentDlqCountMono, totalCountMono)
                .flatMap(tuple -> {
                    Long errorCount = tuple.getT1();
                    Long successCount = tuple.getT2();
                    Long propertyCount = tuple.getT3();
                    Long residentCount = tuple.getT4();
                    Long propertyDlqCount = tuple.getT5();
                    Long residentDlqCount = tuple.getT6();
                    Long totalCount = tuple.getT7();

                    // Calculate percentages
                    float errorPercentage = (totalCount != 0) ? ((float) (errorCount * 100) / totalCount) : 0.0f;
                    float successPercentage = (totalCount != 0) ? ((float) (successCount * 100) / totalCount) : 0.0f;
                    float propertyPercentage = (totalCount != 0) ? ((float) (propertyCount * 100) / totalCount) : 0.0f;
                    float residentPercentage = (totalCount != 0) ? ((float) (residentCount * 100) / totalCount) : 0.0f;
                    float propertyDlqPercentage = (totalCount != 0) ? ((float) (propertyDlqCount * 100) / totalCount) : 0.0f;
                    float residentDlqPercentage = (totalCount != 0) ? ((float) (residentDlqCount * 100) / totalCount) : 0.0f;

                    // Build the response
                    StatisticResponse reportResponse = StatisticResponse.builder()
                            .totalItemsCount(totalCount)
                            .totalErrorsCount(errorCount)
                            .totalSuccessCount(successCount)
                            .totalPropertyCount(propertyCount)
                            .totalResidentCount(residentCount)
                            .totalPropertyDlqCount(propertyDlqCount)
                            .totalResidentDlqCount(residentDlqCount)
                            .percentageErrorsCount(errorPercentage)
                            .percentageSuccessCount(successPercentage)
                            .percentagePropertyCount(propertyPercentage)
                            .percentageResidentCount(residentPercentage)
                            .percentagePropertyDlqCount(propertyDlqPercentage)
                            .percentageResidentDlqCount(residentDlqPercentage)
                            .build();
                    return Mono.just(reportResponse);
                });
    }

    public Mono<LocalDate> getLastReportDate() {
        return Mono.justOrEmpty(reportRepository.findTopByOrderByCreatedAtDesc())
                .map(reportEntity -> reportEntity.getCreatedAt().toLocalDate());
    }
    public Mono<Long> countAllReportsByErrorBodyForDate(LocalDateTime start, LocalDateTime end) {
        return Mono.just(reportRepository.countAllByErrorBodyContainingIgnoreCaseAndCreatedAtBetween("error", start, end));
    }

    public Mono<Long> countAllReportsByErrorBodySuccessForDate(LocalDateTime start, LocalDateTime end) {
        return Mono.just(reportRepository.countAllByErrorBodyEqualsAndCreatedAtBetween("success", start, end));
    }

    public Mono<Long> countAllReportsByChannelNameForDate(String channelName, LocalDateTime start, LocalDateTime end) {
        return Mono.just(reportRepository.countAllByChannelNameEqualsIgnoreCaseAndCreatedAtBetween(channelName, start, end));
    }

    public Mono<Long> countAllReportsForDate(LocalDateTime start, LocalDateTime end) {
        return Mono.just(reportRepository.countAllByCreatedAtBetween(start, end));
    }

}
