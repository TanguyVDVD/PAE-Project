package be.vinci.pae.main;

import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.MyObjectMapper;
import be.vinci.pae.utils.WebExceptionMapper;
import be.vinci.pae.utils.views.Views;
import java.io.IOException;
import java.net.URI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class.
 */
public class Main {

  /**
   * Base URI the Grizzly HTTP server will listen on.
   */
  public static final String BASE_URI;

  // Loading properties
  static {
    Config.load("dev.properties");
    BASE_URI = Config.getProperty("BaseUri");
  }

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   *
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer() {
    // Create a JacksonJaxbJsonProvider with our custom ObjectMapper
    final JacksonJaxbJsonProvider jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();
    jacksonJaxbJsonProvider.setMapper(MyObjectMapper.getJsonMapper());
    jacksonJaxbJsonProvider.setDefaultWriteView(Views.Public.class);

    // create a resource config that scans for JAX-RS resources and providers
    // in vinci.be package
    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae.api")
        .register(ApplicationBinder.class)
        .register(WebExceptionMapper.class)
        .register(jacksonJaxbJsonProvider);

    // create and start a new instance of grizzly http server
    // exposing the Jersey application at BASE_URI
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  /**
   * Main method.
   *
   * @param args the arguments
   * @throws IOException I/O exception
   */
  public static void main(String[] args) throws IOException {
    final HttpServer server = startServer();
    System.out.println(String.format("Jersey app started with WADL available at "
        + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
    System.in.read();
    server.stop();
  }

}
