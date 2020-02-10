package com.ev.model;

import com.ev.model.entity.SessionEntity;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

/**
 * A thread safe singleton instance representing the CHARGING STORE to keep in memory data.
 */
public class ChargingStore {
  private Map<String, SessionEntity> sessionEntityMap = new TreeMap<String, SessionEntity>();
  private static ChargingStore chargingStoreInstance;

  // private constructor for singleton. 
  private ChargingStore() {
  }

  /**
   * Get the singleton instance. Here <b>double checked locking</b> is used to reduce the cost of synchronized and
   * it'll be done only for first call.
   *
   * @return The {@link ChargingStore} instance from memory if already exists else creates a new and returns it.
   */
  public static ChargingStore getInstance() {
    if (chargingStoreInstance == null) {
      // double checked locking with synchronized within the null check.
      synchronized (ChargingStore.class) {
        chargingStoreInstance = new ChargingStore();
      }
    }
    return chargingStoreInstance;
  }

  /**
   * Put a {@link SessionEntity} into the {@link ChargingStore}. Null entities are not stored.
   *
   * @param entity Entity to put in the store.
   */
  public void putSessionEntity(SessionEntity entity) {
    if (entity != null) {
      // To be thread safe at entity level.
      SessionEntity entityToStore = entity.deepCopy();
      this.sessionEntityMap.put(entityToStore.getID().toString(), entityToStore);
    }
  }

  /**
   * Stops the given {@link SessionEntity} and returns a copy of the SessionEntity.
   */
  public Optional<SessionEntity> stopSessionEntity(String id){
    return getSessionEntity(id).map(entity -> {
      entity.stopSession();
      return Optional.ofNullable(entity.deepCopy());
    }).orElseGet(() -> Optional.empty());
  }
  
  /**
   * Gets all {@link SessionEntity}s in the context.
   * 
   * @return A {@link Collection} of {@link SessionEntity}.
   */
  public Collection<SessionEntity> getAllSessionEntities(){
    return this.sessionEntityMap.values();
  }

  /**
   * Gets the {@link SessionEntity} for the given entityID.
   *
   * @param entityID {@link UUID} entity ID object of entity to be retrieved.
   * @return Optional of the {@link SessionEntity} if id exists else {@link Optional#empty()}.
   */
  public Optional<SessionEntity> getSessionEntity(UUID entityID) {
    if (entityID != null) {
      return getSessionEntity(entityID.toString());
    }

    return Optional.empty();
  }

  /**
   * Gets the {@link SessionEntity} for the given entityID.
   *
   * @param entityID string entity ID of entity to be retrieved.
   * @return Optional of the {@link SessionEntity} if id exists else {@link Optional#empty()}.
   */
  private Optional<SessionEntity> getSessionEntity(String entityID) {
    return Optional.ofNullable(this.sessionEntityMap.get(entityID));
  }

  /**
   * Retrieve a summary of submitted charging sessions in context including: 
   * <ul>
   *   <li><b>totalCount</b> – total number of charging session updates for the last minute.</li>
   *   <li><b>startedCount</b> - total number of started charging sessions for the last minute.</li>
   *   <li><b>stoppedCount</b> - total number of stopped charging sessions for the last minute.</li>
   * </ul>
   */
  public Summary getLastMinuteSummary(){
    Summary result = new Summary();
    // Keeping a threshold time. Entities beyond this threshold will be ignored.
    LocalDateTime thresholdForSummary = LocalDateTime.now().minusMinutes(1); 
    this.sessionEntityMap.values().forEach(entity -> {
      if(entity.isWithinThreshold(thresholdForSummary)) {
        result.totalCount++;
        switch (entity.getStatus()){
          case IN_PROGRESS: 
            result.startedCount++;
            break;
          case FINISHED:
            result.stoppedCount++;
            break;
          default: break;
        }
      }
    });
    return result;
  }
}
