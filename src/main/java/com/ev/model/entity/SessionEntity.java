package com.ev.model.entity;

import com.ev.model.StatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.web.servlet.server.Session;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class SessionEntity {

  /**
   * Gets the ID of this {@link SessionEntity}.
   *
   * @return String ID.
   */
  public abstract String getID();

  /**
   * Get when this session entity started.
   *
   * @return LocalDateTime when session entity was created.
   */
  public abstract LocalDateTime getStartedAt();

  /**
   * Get when this session entity ended or stopped.
   *
   * @return LocalDateTime when session entity stopped.
   */
  public abstract LocalDateTime getStoppedAt();

  /**
   * Gets the status of this session entity.
   *
   * @return StatusEnum representing the status.
   */
  public abstract StatusEnum getStatus();

  /**
   * Checks if a session entity is within the given threshold limit.
   *
   * @param thresholdDateTime Threshold {@link LocalDateTime}  limit to be used in the check.
   * @return <code>true</code> if the entity is within limit else <code>false</code>
   */
  public abstract boolean isWithinThreshold(LocalDateTime thresholdDateTime);

  /**
   * Updates the session marking the stop or end of session.
   */
  public abstract void stopSession();

  /**
   * Deep copy method to get copy of SessionEntity.
   */
  public abstract SessionEntity deepCopy();
}
