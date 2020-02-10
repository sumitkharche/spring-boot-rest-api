package com.ev.model;

import com.ev.factory.SessionEntityFactory;
import com.ev.model.entity.SessionEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class ChargingStoreTest {

  private SessionEntityFactory factory = new SessionEntityFactory();
  private SessionEntity sessionEntity = factory.getSessionEntity(SessionEntityFactory.Catalog.CHARGING, "test-11");
  
  @BeforeEach
  private void setUp(){
    ChargingStore.getInstance();
    ChargingStore.getInstance().putSessionEntity(sessionEntity);
  }
  
  @Test
  void testGetInstance() {
    Assertions.assertNotNull(ChargingStore.getInstance());
  }

  @Test
  void testPutSessionEntityWithSummary() {
    ChargingStore.getInstance().putSessionEntity(null);
    // Testing Summary here.
    Summary lastMinuteSummary = ChargingStore.getInstance().getLastMinuteSummary();
    Assertions.assertTrue(lastMinuteSummary.startedCount >= 1);
  }

  @Test
  void testStopSessionEntity() {
    Optional<SessionEntity> result = ChargingStore.getInstance().stopSessionEntity(sessionEntity.getID());
    Assertions.assertTrue(result.isPresent() && result.get().getStatus() == StatusEnum.FINISHED);
    // Testing Summary here.
    Summary lastMinuteSummary = ChargingStore.getInstance().getLastMinuteSummary();
    Assertions.assertTrue(lastMinuteSummary.stoppedCount >= 1);
  }
}