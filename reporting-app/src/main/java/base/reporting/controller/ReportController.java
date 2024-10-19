package mch.reporting.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mch.reporting.entity.ReportEntity;
import mch.reporting.model.ReportResponse;
import mch.reporting.model.StatisticResponse;
import mch.reporting.service.ReportService;
import mch.reporting.service.ReportingService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:5051")
public class ReportController implements ReportService {

    private final ReportingService reportingService;

    @Override
    public Mono<ReportResponse> getAll() {
        log.info("Entering Get all reports");
        return reportingService.getAllReports();
    }

    @Override
    public Mono<ReportResponse> getByDateBetween(String dateStart, String dateEnd) {
        log.info("Entering Get by date-range reports");
        return reportingService.getByDateBetween(dateStart, dateEnd);
    }

    @Override
    public Mono<ReportResponse> getById(Long id) {
        log.info("Entering Get by id reports");
        return reportingService.getById(id);
    }

    @Override
    public Mono<ReportResponse> getByChannel(String channel) {
        log.info("Entering Get by channel reports");
        return reportingService.getAllReportsByChannelName(channel);
    }

    @Override
    public Mono<ReportResponse> getByDate(String date) {
        log.info("Entering Get by date reports");
        return reportingService.getAllReportsByDate(date);
    }

    @Override
    public Mono<ReportResponse> getByError(String errorMessageBody) {
        log.info("Entering Get by error reports");
        return reportingService.getAllReportsByErrorBody(errorMessageBody);
    }

    @Override
    public Mono<ReportResponse> getBySuccess(String successMessageBody) {
        return reportingService.getAllReportsByErrorBodySuccess("success");
    }

    @Override
    public Mono<ReportResponse> getByPayload(String payloadBody) {
        return reportingService.getAllReportsByEventPayload(payloadBody);
    }

    @Override
    public Mono<StatisticResponse> getLastWeek(){
        return reportingService.getLastWeekStatistics();
    }

    @Override
    public Mono<StatisticResponse> getStatisticByRange(String dateStart, String dateEnd){
        return reportingService.getStatisticsForDateRange(dateStart, dateEnd);
    }
}
