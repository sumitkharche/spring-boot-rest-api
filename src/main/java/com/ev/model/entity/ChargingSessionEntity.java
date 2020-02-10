package com.ev.model.entity;

import com.ev.model.StatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * POJO representing a charging session entity to be created for a charging session at a given station.
 */
public class ChargingSessionEntity extends SessionEntity {

  final UUID id;
  final String stationId;
  final LocalDateTime startedAt;
  LocalDateTime stoppedAt;
  StatusEnum status;

  /**
   * This constructor to be used only for deep copy.
   */
  public ChargingSessionEntity(UUID id, String stationId, LocalDateTime startedAt, LocalDateTime stoppedAt,
                               StatusEnum status) {
    this.id = id;
    this.stationId = stationId;
    this.startedAt = startedAt;
    this.stoppedAt = stoppedAt;
    this.status = status;
  }

  /**
   * Constructor available for usage.
   *
   * @param stationId Station id for which session is to be created.
   */
  public ChargingSessionEntity(String stationId) {

    this.id = UUID.randomUUID();
    this.stationId = stationId;
    this.startedAt = LocalDateTime.now();
    this.status = StatusEnum.IN_PROGRESS;
  }

  @Override
  public String getID() {
    return this.id.toString();
  }

  public String getStationId() { return this.stationId; }

  @Override
  public LocalDateTime getStartedAt() {
    return this.startedAt;
  }

  @Override
  public LocalDateTime getStoppedAt() {
    return this.stoppedAt;
  }

  @Override
  public StatusEnum getStatus() {
    return this.status;
  }

  @Override
  public boolean isWithinThreshold(LocalDateTime thresholdDateTime) {
    return !this.startedAt.isBefore(thresholdDateTime) ||
        this.stoppedAt != null && !this.stoppedAt.isBefore(thresholdDateTime);
  }

  @Override
  public void stopSession() {
    if (this.status == StatusEnum.IN_PROGRESS) {
      this.status = StatusEnum.FINISHED;
      this.stoppedAt = LocalDateTime.now();
    }
  }

  @Override
  public SessionEntity deepCopy() {
    return new ChargingSessionEntity(this.id, this.stationId, this.startedAt, this.stoppedAt, this.status);
  }
}
