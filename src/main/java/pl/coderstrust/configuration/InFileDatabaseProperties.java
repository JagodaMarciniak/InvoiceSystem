package pl.coderstrust.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("database")
public class InFileDatabaseProperties {

  private String databaseFilePath;

  public String getDatabaseFilePath() {
    return databaseFilePath;
  }

  public void setDatabaseFilePath(String databaseFilePath) {
    this.databaseFilePath = databaseFilePath;
  }
}
