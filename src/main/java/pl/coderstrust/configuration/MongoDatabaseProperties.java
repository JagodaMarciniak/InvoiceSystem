package pl.coderstrust.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("mongo-database")
public class MongoDatabaseProperties {

  private String databaseName;
  private String collectionName;
  private String host;
  private int port;
}
