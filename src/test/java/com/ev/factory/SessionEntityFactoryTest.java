package com.ev.factory;

import com.ev.model.StatusEnum;
import com.ev.model.entity.ChargingSessionEntity;
import com.ev.model.entity.SessionEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class SessionEntityFactoryTest {

  private SessionEntityFactory factory = new SessionEntityFactory();
  
  @Test
  public void getChargingSessionEntity() {
   
    SessionEntity sessionEntity = factory.getSessionEntity(SessionEntityFactory.Catalog.CHARGING, "test-123");

    Assertions.assertTrue(sessionEntity instanceof ChargingSessionEntity, "Not a ChargingSessionEntity");
    Assertions.assertTrue(sessionEntity.getStatus() == StatusEnum.IN_PROGRESS 
        && sessionEntity.getStartedAt().isBefore(LocalDateTime.now().plusSeconds(1)) && sessionEntity.getStoppedAt() == null, 
        "SessionEntity NOT set properly.");
  }
  
  @Test
  public void testGetInvalidSessionEntity(){
    SessionEntity sessionEntity = factory.getSessionEntity("TestType", "test-123");
    Assertions.assertNull(sessionEntity, "Expected null value.");
  }
}