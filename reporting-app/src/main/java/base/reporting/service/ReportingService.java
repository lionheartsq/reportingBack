package mch.reporting.service;

import mch.reporting.entity.ReportEntity;
import mch.reporting.model.ReportResponse;
import mch.reporting.model.StatisticResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReportingService {

    //Get all registers
    Mono<ReportResponse> getAllReports();
    Flux<ReportEntity> getAllReportsFlux();
    Mono<Long> countAllReports();

    //Get all registers by Id
    Mono<ReportResponse> getById(Long id);
    Mono<ReportEntity> getAllReportsOptional(Long id);
    Mono<Long> countById(Long id);

    //Get registers between two dates
    Mono<ReportResponse> getByDateBetween(String dateStart, String dateEnd);
    Flux<ReportEntity> getAllReportsByDateBetween(String startDate, String endDate);
    Mono<Long> countAllReportsByDateBetween(String startDate, String endDate);

    //Get all registers by Channel name
    Mono<ReportResponse> getAllReportsByChannelName(String channelName);
    Flux<ReportEntity> getAllReportsByChannelNameFlux(String channelName);
    Mono<Long> countAllReportsByChannelName(String channelName);

    //Get all registers by Date
    Mono<ReportResponse> getAllReportsByDate(String date);
    Flux<ReportEntity> getAllReportsByDateFlux(String date);
    Mono<Long> countAllReportsByDate(String date);

    //Get all registers by Error
    Mono<ReportResponse> getAllReportsByErrorBody(String errorMessageBody);
    Flux<ReportEntity> getAllReportsByErrorBodyFlux(String errorMessageBody);
    Mono<Long> countAllReportsByErrorBody(String errorMessageBody);

    //Get all registers by Success
    Mono<ReportResponse> getAllReportsByErrorBodySuccess(String errorMessageBody);
    Flux<ReportEntity> getAllReportsByErrorBodySuccessFlux(String errorMessageBody);
    Mono<Long> countAllReportsByErrorBodySuccess(String errorMessageBody);

    //Get all registers by Payload
    Mono<ReportResponse> getAllReportsByEventPayload(String eventPayload);
    Flux<ReportEntity> getAllReportsByEventPayloadFlux(String eventPayload);
    Mono<Long> countAllReportsByEventPayload(String eventPayload);

    //Get statistics
    Mono<StatisticResponse> getLastWeekStatistics();
    Mono<StatisticResponse> getStatisticsForDateRange(String startDate, String endDate);
}
