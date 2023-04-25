package be.vinci.pae.utils.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.apache.commons.text.StringEscapeUtils;

public class EscapeHTMLSerializer extends StdSerializer<String> implements
    ContextualSerializer {

  public EscapeHTMLSerializer() {
    super(String.class);
  }

  @Override
  public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException {
    jgen.writeString(StringEscapeUtils.escapeHtml4(value));
  }

  @Override
  public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
      throws JsonMappingException {
    if (property != null) {
      return new EscapeHTMLSerializer();
    } else {
      return prov.findValueSerializer(String.class, property);
    }
  }
}