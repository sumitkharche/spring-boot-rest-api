package com.ev.controllers;

import com.ev.factory.SessionEntityFactory;
import com.ev.model.ChargingStore;
import com.ev.model.Summary;
import com.ev.model.entity.SessionEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class ChargingSessionsController {

  /**
   * Submit a new charging session for the station. with complexity O(log(n)). Example below:<br/>
   * Request: <code>{"stationId":"ABC-12345"}</code> <br/>
   * Response: <code>{"id":"d9bb7458-d5d9-4de7-87f7-7f39edd51d18","stationId": "ABC-12345","startedAt":"2019-05-06T19:00:20.529","status": "IN_PROGRESS"}</code>
   */
  @PostMapping("/chargingSessions")
  public SessionEntity createSessionEntity(@RequestBody CreateRequest request) {

    // TODO [IMPROVEMENT]: If each station is one charging entity then, 
    //  if already an entity exists for the station ID with status IN_PROGRESS, return that entity, 
    //  else return a new one. 
    // Note for Reviewer: For this exercise I am not adding this check. SessionEntityFactory can be refactored to enforce this.
    // With Current logic multiple SessionEntity for same StationID can be created with same IN_PROGRESS status.
    SessionEntityFactory factory = new SessionEntityFactory();
    SessionEntity chargingSessionEntity = factory.getSessionEntity(SessionEntityFactory.Catalog.CHARGING, request.stationId);

    ChargingStore.getInstance().putSessionEntity(chargingSessionEntity);
    return chargingSessionEntity;
  }

  /**
   * Stops the charging session with given id.
   */
  @PutMapping("/chargingSessions/{id}")
  public ResponseEntity<SessionEntity> stopSessionEntity(@PathVariable("id") String id) {
    return ChargingStore.getInstance().stopSessionEntity(id)
        .map(entity ->  ResponseEntity.ok().body(entity))          // 200 OK
        .orElseGet(() -> ResponseEntity.notFound().build());      // 404 NOT FOUND.
  }

  /**
   * Retrieve all charging sessions.
   */
  @GetMapping("/chargingSessions")
  public Collection<SessionEntity> getAllSessionEntities(){
    
    return ChargingStore.getInstance().getAllSessionEntities();
  }

  /**
   * Retrieve a summary of submitted charging sessions including: 
   * <ul>
   *   <li><b>totalCount</b> – total number of charging session updates for the last minute.</li>
   *   <li><b>startedCount</b> - total number of started charging sessions for the last minute.</li>
   *   <li><b>stoppedCount</b> - total number of stopped charging sessions for the last minute.</li>
   * </ul>
   */
  @GetMapping("/chargingSessions/summary")
  public Summary getSummary(){
    return ChargingStore.getInstance().getLastMinuteSummary();    
  } 
}

/**
 * POJO to represent station creation request.
 */
class CreateRequest {
  public String stationId;
}