package pl.coderstrust.integrationtests.repository;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.coderstrust.generators.InvoiceGenerator.copyInvoice;
import static pl.coderstrust.generators.InvoiceGenerator.getRandomInvoice;
import static pl.coderstrust.generators.InvoiceGenerator.getRandomInvoiceWithNoId;
import static pl.coderstrust.generators.InvoiceGenerator.getRandomInvoiceWithNoIdAndSpecificBuyerName;
import static pl.coderstrust.generators.InvoiceGenerator.getRandomInvoiceWithNoIdAndSpecificSellerName;

import com.mongodb.MongoClient;
import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.coderstrust.configuration.MongoDatabaseProperties;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.database.invoice.InvoiceDatabase;
import pl.coderstrust.database.invoice.MongoInvoiceDatabase;
import pl.coderstrust.model.Invoice;

@ExtendWith(SpringExtension.class)
class MongoInvoiceDatabaseIT {

  private static MongoTemplate mongoTemplate;
  private static MongoDatabaseProperties properties;
  private static InvoiceDatabase mongoDatabase;

  @BeforeAll
  static void setUp() throws IOException {
    properties = new MongoDatabaseProperties();
    properties.setCollectionName("invoices-test");
    properties.setHost("localhost");
    properties.setPort(21017);
    properties.setRepositoryName("invoices");
    EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
    mongo.setBindIp(properties.getHost());
    MongoClient mongoClient = mongo.getObject();
    assert mongoClient != null;
    mongoTemplate = new MongoTemplate(mongoClient, properties.getCollectionName());
    mongoDatabase = new MongoInvoiceDatabase(mongoTemplate, properties);
  }

  @AfterEach
  void cleanDatabase() {
    mongoTemplate.getCollection(properties.getCollectionName()).drop();
  }

  @Test
  void contextLoads() {
    assertNotNull(mongoTemplate);
  }

  @Test
  @DisplayName("Should save one invoice to empty repository and return saved invoice.")
  void shouldSaveAndReturnSavedInvoice() throws DatabaseOperationException {
    //given
    Invoice expectedInvoice = getRandomInvoice();

    //when
    Invoice actualInvoice = mongoDatabase.save(expectedInvoice);

    //then
    assertEquals(Collections.singletonList(expectedInvoice), mongoDatabase.findAll());
    assertEquals(expectedInvoice, actualInvoice);
  }

  @Test
  @DisplayName("Should update invoice in repository when save is called and invoiceId is already present.")
  void saveShouldReplaceInvoiceInRepository() throws DatabaseOperationException {
    //given
    Invoice invoice = getRandomInvoiceWithNoId();
    mongoDatabase.save(invoice);
    Invoice alteredInvoice = copyInvoice(invoice);
    alteredInvoice.setComments("random line of comments");

    //when
    mongoDatabase.save(alteredInvoice);

    //then
    assertEquals(Collections.singletonList(alteredInvoice), mongoDatabase.findAll());
  }

  @Test
  @DisplayName("Should return invoice with specified id when findById is invoked.")
  void shouldReturnInvoiceWithSpecifiedId() throws DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoice();
    Invoice invoice3 = getRandomInvoice();
    mongoDatabase.save(invoice1);
    mongoDatabase.save(invoice2);
    mongoDatabase.save(invoice3);

    //when
    Optional<Invoice> actualInvoice = mongoDatabase.findById(invoice2.getId());

    //then
    assertEquals(Optional.of(invoice2), actualInvoice);
  }

  @Test
  @DisplayName("Should return empty optional when findById is invoked and invoice does not exist.")
  void findByIdShouldReturnEmptyOptionalWhenInvoiceDoesNotExist() throws DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoice();
    mongoDatabase.save(invoice1);
    mongoDatabase.save(invoice2);

    //when
    Optional<Invoice> actualInvoice = mongoDatabase.findById("-1");

    //then
    assertEquals(Optional.empty(), actualInvoice);
  }

  @Test
  @DisplayName("Should return all invoices from repository when findAll is invoked.")
  void shouldReturnAllInvoices() throws DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithNoId();
    Invoice invoice2 = getRandomInvoiceWithNoId();
    Invoice invoice3 = getRandomInvoiceWithNoId();
    mongoDatabase.save(invoice1);
    mongoDatabase.save(invoice2);
    mongoDatabase.save(invoice3);

    //when
    Iterable<Invoice> actualInvoices = mongoDatabase.findAll();

    //then
    assertEquals(Arrays.asList(invoice1, invoice2, invoice3), actualInvoices);
  }

  @Test
  @DisplayName("Should return empty list when findAll is invoked and repository contains no invoices.")
  void findAllShouldReturnEmptyListWhenInputStreamIsEmpty() throws DatabaseOperationException {
    //when
    Iterable<Invoice> actualInvoices = mongoDatabase.findAll();

    //then
    assertEquals(Collections.emptyList(), actualInvoices);
  }

  @Test
  @DisplayName("Should return all invoices associated with particular seller name.")
  void shouldReturnAllInvoicesBySpecifiedSellerName() throws DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithNoIdAndSpecificSellerName("Company One");
    Invoice invoice2 = getRandomInvoiceWithNoIdAndSpecificSellerName("Company One");
    Invoice invoice3 = getRandomInvoiceWithNoIdAndSpecificSellerName("Company Two");
    mongoDatabase.save(invoice1);
    mongoDatabase.save(invoice2);
    mongoDatabase.save(invoice3);

    //when
    Iterable<Invoice> actualInvoices = mongoDatabase.findAllBySellerName(invoice1.getSeller().getName());

    //then
    assertEquals(Arrays.asList(invoice1, invoice2), actualInvoices);
  }

  @Test
  @DisplayName("Should return empty list when findAllBySellerName is invoked and repository contains no invoices.")
  void findAllBySellerNameShouldReturnEmptyListWhenRepositoryIsEmpty() throws DatabaseOperationException {
    //when
    Iterable<Invoice> actualInvoices = mongoDatabase.findAllBySellerName(getRandomInvoice().getSeller().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoices);
  }

  @Test
  @DisplayName("Should return empty list when findAllBySellerName is invoked and requested seller is not present in repository.")
  void findAllBySellerNameShouldReturnEmptyListWhenSellerMissing() throws DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithNoIdAndSpecificSellerName("Company One");
    Invoice invoice2 = getRandomInvoiceWithNoIdAndSpecificSellerName("Company Two");
    Invoice invoice3 = getRandomInvoiceWithNoIdAndSpecificSellerName("Company Three");
    mongoDatabase.save(invoice1);
    mongoDatabase.save(invoice2);
    mongoDatabase.save(invoice3);

    //when
    Iterable<Invoice> actualInvoicesBySellerName = mongoDatabase.findAllBySellerName("A.C.M.E. Incorporated");

    //then
    assertEquals(Collections.emptyList(), actualInvoicesBySellerName);
  }

  @Test
  @DisplayName("Should return all invoices associated with particular buyer name when findAllByBuyerName is invoked.")
  void shouldReturnAllInvoicesByBuyerName() throws DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithNoIdAndSpecificBuyerName("Company One");
    Invoice invoice2 = getRandomInvoiceWithNoIdAndSpecificBuyerName("Company Two");
    Invoice invoice3 = getRandomInvoiceWithNoIdAndSpecificBuyerName("Company One");
    mongoDatabase.save(invoice1);
    mongoDatabase.save(invoice2);
    mongoDatabase.save(invoice3);

    //when
    Iterable<Invoice> actualInvoices = mongoDatabase.findAllByBuyerName(invoice1.getBuyer().getName());

    //then
    assertEquals(Arrays.asList(invoice1, invoice3), actualInvoices);
  }

  @Test
  @DisplayName("Should return empty list when findAllByBuyerName is invoked and repository is empty.")
  void shouldReturnEmptyListWhenFindAllByBuyerNameInvokedAndRepositoryIsEmpty() throws DatabaseOperationException {
    //when
    Iterable<Invoice> actualInvoices = mongoDatabase.findAllByBuyerName(getRandomInvoice().getBuyer().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoices);
  }

  @Test
  @DisplayName("Should return empty list when findAllByBuyerName is invoked and specified buyer is missing.")
  void findAllByBuyerNameShouldReturnEmptyListWhenBuyerMissing() throws DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithNoIdAndSpecificBuyerName("Company One");
    Invoice invoice2 = getRandomInvoiceWithNoIdAndSpecificBuyerName("Company Two");
    Invoice invoice3 = getRandomInvoiceWithNoIdAndSpecificBuyerName("Company Three");
    mongoDatabase.save(invoice1);
    mongoDatabase.save(invoice2);
    mongoDatabase.save(invoice3);

    //when
    Iterable<Invoice> actualInvoicesByBuyerName = mongoDatabase.findAllByBuyerName("A.C.M.E. Incorporated");

    //then
    assertEquals(Collections.emptyList(), actualInvoicesByBuyerName);
  }

  @ParameterizedTest
  @DisplayName("Should return number of invoices in repository when count is called.")
  @MethodSource("countInvoicesTestParameters")
  void shouldReturnInvoiceCount(List<Invoice> invoices, Long expectedInvoiceCount) throws DatabaseOperationException {
    //given
    for (Invoice invoice : invoices) {
      mongoDatabase.save(invoice);
    }

    //when
    Long actualInvoiceCount = mongoDatabase.count();

    //then
    assertEquals(expectedInvoiceCount, actualInvoiceCount);
  }

  private static Stream<Arguments> countInvoicesTestParameters() {
    Invoice invoice1 = getRandomInvoiceWithNoId();
    Invoice invoice2 = getRandomInvoiceWithNoId();
    Invoice invoice3 = getRandomInvoiceWithNoId();
    return Stream.of(
        Arguments.of(Collections.emptyList(), 0L),
        Arguments.of(Collections.singletonList(invoice1), 1L),
        Arguments.of(Arrays.asList(invoice1, invoice2), 2L),
        Arguments.of(Arrays.asList(invoice1, invoice2, invoice3), 3L)
    );
  }

  @Test
  @DisplayName("Should return true when existsById is invoked and given invoice is present in repository.")
  void shouldReturnTrueWhenInvoiceExists() throws DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithNoId();
    Invoice invoice2 = getRandomInvoiceWithNoId();
    Invoice invoice3 = getRandomInvoiceWithNoId();
    mongoDatabase.save(invoice1);
    mongoDatabase.save(invoice2);
    mongoDatabase.save(invoice3);

    //when
    boolean result = mongoDatabase.existsById(invoice2.getId());

    //then
    assertTrue(result);
  }

  @Test
  @DisplayName("Should return false when existsById is invoked and given invoice is not present in repository.")
  void shouldReturnFalseWhenInvoiceDoesNotExist() throws DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithNoId();
    Invoice invoice2 = getRandomInvoiceWithNoId();
    Invoice invoice3 = getRandomInvoiceWithNoId();
    mongoDatabase.save(invoice1);
    mongoDatabase.save(invoice2);
    mongoDatabase.save(invoice3);

    //when
    boolean result = mongoDatabase.existsById("-1");

    //then
    assertFalse(result);
  }

  @Test
  @DisplayName("Should clear repository file when deleteAll is invoked.")
  void deleteAllShouldClearRepository() throws DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithNoId();
    Invoice invoice2 = getRandomInvoiceWithNoId();
    Invoice invoice3 = getRandomInvoiceWithNoId();
    mongoDatabase.save(invoice1);
    mongoDatabase.save(invoice2);
    mongoDatabase.save(invoice3);

    //when
    mongoDatabase.deleteAll();

    //then
    assertEquals(0, mongoDatabase.count());
    assertEquals(Collections.emptyList(), mongoDatabase.findAll());
  }

  @Test
  @DisplayName("Should delete specified invoice when deleteById is invoked.")
  void shouldDeleteSpecifiedInvoice() throws DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithNoId();
    Invoice invoice2 = getRandomInvoiceWithNoId();
    Invoice invoice3 = getRandomInvoiceWithNoId();
    mongoDatabase.save(invoice1);
    mongoDatabase.save(invoice2);
    mongoDatabase.save(invoice3);

    //when
    mongoDatabase.deleteById(invoice2.getId());

    //then
    assertEquals(Arrays.asList(invoice1, invoice3), mongoDatabase.findAll());
  }

  @Test
  @DisplayName("Should not alter repository contents if deleteById invoked and specified invoice does not exist.")
  void deleteByIdShouldNotChangeRepositoryContentsWhenInvoiceDoesNotExist() throws DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithNoId();
    Invoice invoice2 = getRandomInvoiceWithNoId();
    Invoice invoice3 = getRandomInvoiceWithNoId();
    mongoDatabase.save(invoice1);
    mongoDatabase.save(invoice2);
    mongoDatabase.save(invoice3);

    //when
    mongoDatabase.deleteById("-1");

    //then
    assertEquals(Arrays.asList(invoice1, invoice2, invoice3), mongoDatabase.findAll());
  }
}
