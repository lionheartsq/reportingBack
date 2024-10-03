package mch.reporting.repository;

import mch.reporting.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    List<ReportEntity> findAll();

    Optional<ReportEntity> findById(Long id);

    Long countAllById(Long id);

    List<ReportEntity> findAllByChannelNameEqualsIgnoreCase(String channelName);

    Long countAllByChannelNameEqualsIgnoreCase(String channelName);

    List<ReportEntity> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    Long countAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<ReportEntity> findAllByErrorBodyContainingIgnoreCase(String errorMessageBody);

    Long countAllByErrorBodyContainingIgnoreCase(String errorMessageBody);

    List<ReportEntity> findAllByErrorBodyEquals(String errorBody);

    Long countAllByErrorBodyEquals(String errorBody);

    List<ReportEntity> findAllByEventPayloadContainingIgnoreCase(String eventPayload);

    Long countAllByEventPayloadContainingIgnoreCase(String eventPayload);

    Long countAllByErrorBodyContainingIgnoreCaseAndCreatedAtBetween(String errorBody, LocalDateTime start, LocalDateTime end);

    Long countAllByErrorBodyEqualsAndCreatedAtBetween(String errorBody, LocalDateTime start, LocalDateTime end);

    Long countAllByChannelNameEqualsIgnoreCaseAndCreatedAtBetween(String channelName, LocalDateTime start, LocalDateTime end);

    Optional<ReportEntity> findTopByOrderByCreatedAtDesc(); // Obtains more recent report

}
