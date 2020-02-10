package com.ev.model.entity;

import com.ev.model.StatusEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ChargingSessionEntityTest {

  private ChargingSessionEntity sessionEntity;
  
  @BeforeEach
  private void setUp() {
    sessionEntity = new ChargingSessionEntity("test-123");
  }
  
  @Test
  public void testIsWithinThreshold() {
 
    // Within 1 min threshold.
    Assertions.assertTrue(sessionEntity.isWithinThreshold(LocalDateTime.now().minusMinutes(2)));
 
    // Within 0s threshold.
    Assertions.assertFalse(sessionEntity.isWithinThreshold(LocalDateTime.now().plusMinutes(1)));
  }

  @Test
  public void testStopSession() {
    // Before stop.
    Assertions.assertTrue(sessionEntity.getStatus() == StatusEnum.IN_PROGRESS);
    LocalDateTime thresholdTime = LocalDateTime.now();
    Assertions.assertFalse(sessionEntity.isWithinThreshold(LocalDateTime.now().plusMinutes(1)));
    // After stop.
    sessionEntity.stopSession();
    Assertions.assertTrue(sessionEntity.getStatus() == StatusEnum.FINISHED && sessionEntity.isWithinThreshold(thresholdTime));    
  }
  
  @Test 
  public void testDeepCopy() {
    SessionEntity copiedEntity = this.sessionEntity.deepCopy();
    copiedEntity.stopSession();
    Assertions.assertTrue(this.sessionEntity.getStatus() == StatusEnum.IN_PROGRESS &&
        copiedEntity.getStatus() == StatusEnum.FINISHED);
  }
}