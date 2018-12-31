package pl.coderstrust.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import pl.coderstrust.helpers.FileHelper;
import pl.coderstrust.helpers.FileHelperImpl;
import pl.coderstrust.repository.invoice.InFileInvoiceRepository;
import pl.coderstrust.repository.invoice.InMemoryInvoiceRepository;
import pl.coderstrust.repository.invoice.InvoiceRepository;

@Configuration
@EnableConfigurationProperties(InFileRepositoryProperties.class)
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:in-file-repository.yml")
public class ApplicationConfiguration {

  @Autowired
  private InFileRepositoryProperties inFileRepositoryProperties;

  @Bean
  @ConditionalOnProperty(name = "repository", havingValue = "in-file")
  public ObjectMapper getObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }

  @Bean
  @ConditionalOnProperty(name = "repository", havingValue = "in-file")
  public FileHelper getFileHelper() {
    return new FileHelperImpl(inFileRepositoryProperties.getDatabaseFilePath());
  }

  @Bean
  @ConditionalOnProperty(name = "repository", havingValue = "in-memory")
  public InvoiceRepository getInMemoryInvoiceRepository() {
    return new InMemoryInvoiceRepository();
  }

  @Bean
  @ConditionalOnProperty(name = "repository", havingValue = "in-file")
  public InvoiceRepository getInFileInvoiceRepository(FileHelper fileHelper, ObjectMapper objectMapper) throws Exception {
    return new InFileInvoiceRepository(fileHelper, objectMapper);
  }

  @Bean
  @ConditionalOnProperty(name = "repository", havingValue = "in-memory-h2-hibernate")
  public InvoiceJpaConfig getHibernateH2Repository() {
    return new InvoiceJpaConfig();
  }
}
