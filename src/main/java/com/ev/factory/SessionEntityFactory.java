package com.ev.factory;

import com.ev.model.entity.ChargingSessionEntity;
import com.ev.model.entity.SessionEntity;

/**
 * Factory to create available {@link SessionEntity}.
 */
public class SessionEntityFactory {

  /**
   * Given a session type, get the {@link SessionEntity}.
   * @param sessionEntityType session entity type to be created.
   * @param stationId stationId for the session entity.
   * @return session entity as requested, if type not available, returns null.
   */
  public SessionEntity getSessionEntity(String sessionEntityType, String stationId){
    if("charging".equalsIgnoreCase(sessionEntityType)) {
      return new ChargingSessionEntity(stationId);
    }
    return null;
  }
  
  public static final class Catalog{

    private Catalog(){
      // No constructor for catalogs.
    }
    
    public static final String CHARGING = "charging";
  }
}