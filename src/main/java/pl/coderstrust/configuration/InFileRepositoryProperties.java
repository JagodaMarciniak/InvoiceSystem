package pl.coderstrust.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("in-file-repository")
public class InFileRepositoryProperties {

  @Getter
  @Setter
  private String databaseFilePath;
}
