package be.vinci.pae.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Custom ObjectMapper class, to properly serialize and deserialize dates.
 */
public class MyObjectMapper {

  private static final ObjectMapper jsonMapper = JsonMapper.builder()
      .addModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .build();

  /**
   * Get the custom ObjectMapper.
   *
   * @return the custom ObjectMapper
   */
  public static ObjectMapper getJsonMapper() {
    return jsonMapper;
  }
}