package be.vinci.pae.utils.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.apache.commons.text.StringEscapeUtils;

public class EscapeHTMLSerializer extends JsonSerializer<String> {

  @Override
  public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException {
    jgen.writeString(StringEscapeUtils.escapeHtml4(value));
  }
}