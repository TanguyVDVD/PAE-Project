package be.vinci.pae.utils.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.apache.commons.text.StringEscapeUtils;

/**
 * JSON serializer that escapes HTML.
 */
public class EscapeHTMLSerializer extends JsonSerializer<String> {

  /**
   * Serialize a string by escaping HTML.
   *
   * @param value    the string to serialize
   * @param jgen     the JsonGenerator
   * @param provider the SerializerProvider
   * @throws IOException if an error occurs
   */
  @Override
  public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException {
    jgen.writeString(StringEscapeUtils.escapeHtml4(value));
  }
}