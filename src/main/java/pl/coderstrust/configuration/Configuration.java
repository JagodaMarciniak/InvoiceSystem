package pl.coderstrust.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;

public class Configuration {
  public static final String DATABASE_FILE_NAME = "invoice_database.txt";
  public static final String DATABASE_FILE_PATH = String.format("%1$s%2$ssrc%2$smain%2$sjava%2$sresources%2$s%3$s",
      System.getProperty("user.dir"), File.separator, DATABASE_FILE_NAME);

  public static ObjectMapper getObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }
}
