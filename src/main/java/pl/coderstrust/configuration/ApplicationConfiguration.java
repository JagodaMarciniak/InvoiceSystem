package pl.coderstrust.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import pl.coderstrust.helpers.FileHelper;

@Configuration
@EnableConfigurationProperties({InFileDatabaseProperties.class, MongoDatabaseProperties.class})
@PropertySource(factory = YamlPropertySourceFactory.class, value = {"classpath:in-file-database.yml", "classpath:mongo-repository.yml"})
public class ApplicationConfiguration {

  @Autowired
  private InFileDatabaseProperties inFileDatabaseProperties;

  @Autowired
  private MongoDatabaseProperties mongoDatabaseProperties;

  @Bean
  @ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "in-file")
  public ObjectMapper getObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }

  @Bean
  @ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "in-file")
  public FileHelper getFileHelper() {
    return new FileHelper(inFileDatabaseProperties.getDatabaseFilePath());
  }

  @Bean
  @ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "mongodb")
  public MongoClient mongoClient() {
    return new MongoClient(mongoDatabaseProperties.getHost(), mongoDatabaseProperties.getPort());
  }

  @Bean
  @ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "mongodb")
  public MongoDbFactory getMongoDbFactory(MongoClient mongoClient){
    return new SimpleMongoDbFactory(mongoClient, mongoDatabaseProperties.getRepositoryName());
  }

  @Bean
  @ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "mongodb")
  public MongoTemplate getMmongoTemplate(MongoDbFactory mongoDbFactory){
    return new MongoTemplate(mongoDbFactory);
  }
}
