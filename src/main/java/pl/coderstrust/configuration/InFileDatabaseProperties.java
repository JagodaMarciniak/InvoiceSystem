package pl.coderstrust.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("in-file-database")
public class InFileDatabaseProperties {

  @Getter
  @Setter
  private String databaseFilePath;
}
