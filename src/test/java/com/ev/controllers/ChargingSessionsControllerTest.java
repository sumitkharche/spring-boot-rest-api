package com.ev.controllers;

import com.ev.factory.SessionEntityFactory;
import com.ev.model.StatusEnum;
import com.ev.model.entity.ChargingSessionEntity;
import com.ev.model.entity.SessionEntity;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ChargingSessionsController.class)
public class ChargingSessionsControllerTest {

  private String baseUri = "/chargingSessions";
  SessionEntityFactory factory;
  SessionEntity entity;
  String backEndEntityId;
  @Autowired
  private MockMvc mockMvc;
  
  @BeforeEach
  public void setUp() throws Exception {
    factory = new SessionEntityFactory();
    entity = factory.getSessionEntity(SessionEntityFactory.Catalog.CHARGING, "test-00");
    MvcResult result = createSessionEntity(entity);
    Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    backEndEntityId = (String)getKeyAndValueFromJson(result.getResponse().getContentAsString()).get("id");
  }

  @Test
  public void testCreateSessionEntity() throws Exception {
    String stationId = "test-11";
    SessionEntity entity = factory.getSessionEntity(SessionEntityFactory.Catalog.CHARGING, stationId);
    MvcResult result = createSessionEntity(entity);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    
    Map<String, Object> resultEntity = getKeyAndValueFromJson(result.getResponse().getContentAsString());
    Assertions.assertTrue(((String)resultEntity.get("status")).equals(StatusEnum.IN_PROGRESS.toString()) &&
        ((String)resultEntity.get("stationId")).equals(stationId));
  }

  @Test
  public void testStopSession() throws Exception {
    String uri = baseUri + "/" + backEndEntityId;
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .put(uri)
        .contentType(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();

    // Checking for expected response 200.
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    
    // Checking for expected status and entity id.
    Map<String, Object> resultEntity = getKeyAndValueFromJson(response.getContentAsString());
    Assertions.assertTrue(((String)resultEntity.get("status")).equals(StatusEnum.FINISHED.toString()) &&
        ((String)resultEntity.get("id")).equals(backEndEntityId));
  }
  
  @Test
  public void testSummary() throws  Exception{
    String uri = baseUri + "/summary";
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(uri)
        .contentType(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();

    // Checking for 200 response code.
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    
    // Expecting summary with 3 keys.
    Map<String, Object> resultEntity = getKeyAndValueFromJson(response.getContentAsString());
    assertEquals(resultEntity.keySet().size(), 3);
  }

  @Test
  public void testGetAllSessionEntities() throws  Exception{
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(baseUri)
        .contentType(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();

    // Checking for 200 response code.
    assertEquals(HttpStatus.OK.value(), response.getStatus());

    // Expecting at least 2 entities in list.
    ArrayList<Object> entityList = new ObjectMapper().readValue(response.getContentAsString(), ArrayList.class);
    assertTrue(entityList.size() >= 1);
  }

  /**
   * Create the given session entity.
   */
  private MvcResult createSessionEntity(SessionEntity entity) throws Exception {
    String entityJson = mapToJson(entity);
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post(baseUri)
        .accept(MediaType.APPLICATION_JSON).content(entityJson)
        .contentType(MediaType.APPLICATION_JSON);

    return mockMvc.perform(requestBuilder).andReturn();
  }
  
  /**
   * Object to JSON mapper.
   */
  private String mapToJson(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }

  /** 
   * Maping json keys to value. Using this as alternative is to write a custom JSON serializer which is out-of-scope.
   */
  private Map<String, Object> getKeyAndValueFromJson(String json) throws JsonProcessingException {
    return new ObjectMapper().readValue(json, HashMap.class);
  }
}