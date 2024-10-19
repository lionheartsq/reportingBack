package mch.reporting.service;

import mch.reporting.entity.ReportEntity;
import mch.reporting.model.ReportResponse;
import mch.reporting.model.StatisticResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/reporting")
public interface ReportService {
    @GetMapping("/")
    Mono<ReportResponse> getAll();

    @GetMapping("/{id}")
    Mono<ReportResponse> getById(@PathVariable Long id);

    @GetMapping("/channel/{channel}")
    Mono<ReportResponse> getByChannel(@PathVariable String channel);

    @GetMapping("/date")
    Mono<ReportResponse> getByDate(@RequestParam("date") String date);

    @GetMapping("/date-range")
    Mono<ReportResponse> getByDateBetween(
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd);

    @GetMapping("/errors")
    Mono<ReportResponse> getByError(@RequestParam String errorMessageBody);

    @GetMapping("/success")
    Mono<ReportResponse> getBySuccess(@RequestParam(defaultValue = "success") String successMessageBody);

    @GetMapping("/payload")
    Mono<ReportResponse> getByPayload(@RequestParam String payloadBody);

    @GetMapping("/lastWeek")
    Mono<StatisticResponse>getLastWeek();

    @GetMapping("/statistic-range")
    Mono<StatisticResponse>getStatisticByRange(
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd);
}
