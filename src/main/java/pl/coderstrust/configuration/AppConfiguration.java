package pl.coderstrust.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.coderstrust.helpers.FileHelper;
import pl.coderstrust.helpers.FileHelperImpl;
import pl.coderstrust.repository.invoice.InFileInvoiceRepository;
import pl.coderstrust.repository.invoice.InMemoryInvoiceRepository;
import pl.coderstrust.repository.invoice.InvoiceRepository;

@Configuration
@EnableConfigurationProperties(InFileDatabaseProperties.class)
public class AppConfiguration {

  @Autowired
  private InFileDatabaseProperties inFileDatabaseProperties;

  public static ObjectMapper getObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }

  @Bean
  public FileHelper fileHelper() {
    return new FileHelperImpl(inFileDatabaseProperties.getDatabaseFilePath());
  }

  @Bean
  @Profile("in-memory-database")
  public InvoiceRepository inMemoryInvoiceRepository() {
    return new InMemoryInvoiceRepository();
  }

  @Bean
  @Profile("in-file-database")
  public InvoiceRepository inFileInvoiceRepository(FileHelper fileHelper) throws Exception {
    return new InFileInvoiceRepository(fileHelper, getObjectMapper());
  }
}
