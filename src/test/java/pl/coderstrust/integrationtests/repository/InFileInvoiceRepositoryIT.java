package pl.coderstrust.integrationtests.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.coderstrust.generators.InvoiceGenerator.getRandomInvoice;
import static pl.coderstrust.generators.InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName;
import static pl.coderstrust.generators.InvoiceGenerator.getRandomInvoiceWithSpecificId;
import static pl.coderstrust.generators.InvoiceGenerator.getRandomInvoiceWithSpecificSellerName;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import pl.coderstrust.configuration.Configuration;
import pl.coderstrust.helpers.FileHelperImpl;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.repository.RepositoryOperationException;
import pl.coderstrust.repository.invoice.InFileInvoiceRepository;
import pl.coderstrust.repository.invoice.InvoiceRepository;

public class InFileInvoiceRepositoryIT {

  private final ObjectMapper mapper = Configuration.getObjectMapper();
  private final String expectedDatabaseFilePath = String.format("%1$s%2$ssrc%2$stest%2$sresources%2$sdatabase%2$s%3$s",
      System.getProperty("user.dir"), File.separator, "expected_invoice_database.txt");
  private final File databaseFile = new File(Configuration.DATABASE_FILE_PATH);
  private final File expectedDatabaseFile = new File(expectedDatabaseFilePath);
  private InvoiceRepository inFileRepository;

  @BeforeEach
  void setUp(TestInfo testInfo) throws Exception {
    if (databaseFile.exists()) {
      databaseFile.delete();
    }
    if (!testInfo.getTestMethod().get().getName().equals("saveShouldSaveNewInvoiceWithProperIdToNonEmptyDatabase")) {
      inFileRepository = new InFileInvoiceRepository(new FileHelperImpl(Configuration.DATABASE_FILE_PATH), mapper);
    }
    if (expectedDatabaseFile.exists()) {
      expectedDatabaseFile.delete();
      expectedDatabaseFile.createNewFile();
    }
  }

  @Test
  @DisplayName("Should save new invoice to empty database when save is invoked.")
  void saveShouldSaveNewInvoiceToDatabase() throws IOException, RepositoryOperationException {
    //given
    Invoice invoice = getRandomInvoiceWithSpecificId(1);
    String invoiceAsJson = mapper.writeValueAsString(invoice);
    FileUtils.writeLines(expectedDatabaseFile, Collections.singleton(invoiceAsJson), null);

    //when
    Invoice savedInvoice = inFileRepository.save(invoice);

    //then
    assertTrue(FileUtils.contentEquals(expectedDatabaseFile, databaseFile));
    assertEquals(1 , savedInvoice.getId());
  }

  @Test
  @DisplayName("Should save new invoice with proper id to non-empty database when save is invoked.")
  void saveShouldSaveNewInvoiceWithProperIdToNonEmptyDatabase() throws IOException, RepositoryOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithSpecificId(1);
    Invoice invoice2 = getRandomInvoiceWithSpecificId(2);
    Invoice invoice3 = getRandomInvoiceWithSpecificId(3);
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    String invoice3AsJson = mapper.writeValueAsString(invoice3);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson), null);
    FileUtils.writeLines(expectedDatabaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), null);
    inFileRepository = new InFileInvoiceRepository(new FileHelperImpl(Configuration.DATABASE_FILE_PATH), mapper);

    //when
    Invoice savedInvoice = inFileRepository.save(invoice3);

    //then
    assertTrue(FileUtils.contentEquals(expectedDatabaseFile, databaseFile));
    assertEquals(3, savedInvoice.getId());
  }

  @Test
  @DisplayName("Should replace invoice in database file when save is called and invoiceId is already present in database.")
  void saveShouldReplaceInvoiceInDatabase() throws IOException, RepositoryOperationException {
    //given
    Invoice invoice = getRandomInvoiceWithSpecificId(1);
    Invoice alteredInvoice = getRandomInvoiceWithSpecificId(1);
    String invoiceAsJson = mapper.writeValueAsString(invoice);
    String alteredInvoiceAsJson = mapper.writeValueAsString(alteredInvoice);
    FileUtils.writeLines(expectedDatabaseFile, Collections.singleton(alteredInvoiceAsJson), null);
    FileUtils.writeLines(databaseFile, Collections.singleton(invoiceAsJson), null);

    //when
    inFileRepository.save(alteredInvoice);

    //then
    assertTrue(FileUtils.contentEquals(expectedDatabaseFile, databaseFile));
  }

  @Test
  @DisplayName("Should return specified invoice when findById is invoked.")
  void findByIdShouldReturnSpecifiedInvoice() throws IOException, RepositoryOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoice();
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson), null);

    //when
    Optional<Invoice> actualInvoice = inFileRepository.findById(invoice1.getId());

    //then
    assertEquals(Optional.of(invoice1), actualInvoice);
  }

  @Test
  @DisplayName("Should return empty optional when findById is invoked and invoice that is searched for is missing.")
  void findByIdShouldReturnEmptyOptionalWhenInvoiceIsMissing() throws IOException, RepositoryOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoice();
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson), null);

    //when
    Optional<Invoice> actualInvoice = inFileRepository.findById(123456789);

    //then
    assertEquals(Optional.empty(), actualInvoice);
  }

  @Test
  @DisplayName("Should return empty optional when findById is called and database file is empty.")
  void shouldReturnEmptyOptionalWhenFidByIdCalledAndDatabaseFileIsEmpty() throws RepositoryOperationException {
    //when
    Optional<Invoice> actualInvoice = inFileRepository.findById(getRandomInvoice().getId());

    //then
    assertEquals(Optional.empty(), actualInvoice);
  }

  @Test
  @DisplayName("Should return empty optional when findById is called and database file contains invalid data.")
  void shouldReturnEmptyOptionalWhenFindByIdCalledAndDatabaseFileContainsInvalidData() throws RepositoryOperationException, IOException {
    //given
    FileUtils.writeLines(databaseFile, Collections.singletonList("xyz"), null);

    //when
    Optional<Invoice> actualInvoice = inFileRepository.findById(getRandomInvoice().getId());

    //then
    assertEquals(Optional.empty(), actualInvoice);
  }

  @Test
  @DisplayName("Should return all invoices from database when findAll is invoked.")
  void shouldReturnAllInvoices() throws IOException, RepositoryOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoice();
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson), null);

    //when
    Iterable<Invoice> actualInvoices = inFileRepository.findAll();

    //then
    assertEquals(Arrays.asList(invoice1, invoice2), actualInvoices);
  }

  @Test
  @DisplayName("Should return empty list when findAll is invoked and database file is empty.")
  void findAllShouldReturnEmptyListWhenDatabaseIsEmpty() throws RepositoryOperationException {
    //when
    Iterable<Invoice> actualInvoices = inFileRepository.findAll();

    //then
    assertEquals(Collections.emptyList(), actualInvoices);
  }

  @Test
  @DisplayName("Should return all invoices by specified seller name when findAllBySellerName is invoked.")
  void shouldReturnAllInvoicesBySpecifiedSellerName() throws IOException, RepositoryOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoiceWithSpecificSellerName("Warner Brothers");
    Invoice invoice3 = getRandomInvoiceWithSpecificSellerName("Warner Brothers");
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    String invoice3AsJson = mapper.writeValueAsString(invoice3);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), null);

    //when
    Iterable<Invoice> actualInvoicesBySellerName = inFileRepository.findAllBySellerName(invoice2.getSeller().getName());

    //then
    assertEquals(Arrays.asList(invoice2, invoice3), actualInvoicesBySellerName);
  }

  @Test
  @DisplayName("Should return empty list when findAllBySellerName is called and specified seller is missing.")
  void findAllBySellerNameShouldReturnEmptyListWhenSellerMissing() throws IOException, RepositoryOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithSpecificSellerName("Universal Studios");
    Invoice invoice2 = getRandomInvoiceWithSpecificSellerName("Walt Disney");
    Invoice invoice3 = getRandomInvoiceWithSpecificSellerName("Dreamworks");
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    String invoice3AsJson = mapper.writeValueAsString(invoice3);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), null);

    //when
    Iterable<Invoice> actualInvoicesBySellerName = inFileRepository.findAllBySellerName("XYZ");

    //then
    assertEquals(Collections.emptyList(), actualInvoicesBySellerName);
  }

  @Test
  @DisplayName("Should return empty list when findAllBySellerName is invoked and database file is empty.")
  void findAllBySellerNameShouldReturnEmptyListWhenDatabaseIsEmpty() throws RepositoryOperationException {
    //when
    Iterable<Invoice> actualInvoicesBySellerName = inFileRepository.findAllBySellerName(getRandomInvoice().getSeller().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoicesBySellerName);
  }

  @Test
  @DisplayName("Should return empty list when findAllBySellerName is invoked and database file contains invalid data.")
  void findAllBySellerNameShouldReturnEmptyListWhenDatabaseContainsInvalidData() throws RepositoryOperationException, IOException {
    //given
    FileUtils.writeLines(databaseFile, Collections.singletonList("xyz"), null);

    //when
    Iterable<Invoice> actualInvoicesBySellerName = inFileRepository.findAllBySellerName(getRandomInvoice().getSeller().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoicesBySellerName);
  }

  @Test
  @DisplayName("Should return all invoices by specified buyer name when findAllByBuyerName is called.")
  void shouldReturnAllInvoicesByBuyerName() throws IOException, RepositoryOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithSpecificBuyerName("Logitech");
    Invoice invoice2 = getRandomInvoiceWithSpecificBuyerName("Logitech");
    Invoice invoice3 = getRandomInvoiceWithSpecificBuyerName("Samsung");
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    String invoice3AsJson = mapper.writeValueAsString(invoice3);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), null);

    //when
    Iterable<Invoice> actualInvoicesByBuyerName = inFileRepository.findAllByBuyerName(invoice2.getBuyer().getName());

    //then
    assertEquals(Arrays.asList(invoice1, invoice2), actualInvoicesByBuyerName);
  }

  @Test
  @DisplayName("Should return empty list when findAllByBuyerName invoked and specified buyer is missing.")
  void findAllByBuyerNameShouldReturnEmptyListWhenBuyerIsMissing() throws IOException, RepositoryOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithSpecificBuyerName("Logitech");
    Invoice invoice2 = getRandomInvoiceWithSpecificBuyerName("Apple");
    Invoice invoice3 = getRandomInvoiceWithSpecificBuyerName("Samsung");
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    String invoice3AsJson = mapper.writeValueAsString(invoice3);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), null);

    //when
    Iterable<Invoice> actualInvoicesByBuyerName = inFileRepository.findAllBySellerName("Hitachi");

    //then
    assertEquals(Collections.emptyList(), actualInvoicesByBuyerName);
  }

  @Test
  @DisplayName("Should return empty list when findAllByBuyerName is called and database file is empty.")
  void findAllByBuyerNameShouldReturnEmptyListWhenDatabaseFileIsEmpty() throws RepositoryOperationException {
    //when
    Iterable<Invoice> actualInvoicesByBuyerName = inFileRepository.findAllByBuyerName(getRandomInvoice().getBuyer().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoicesByBuyerName);
  }

  @Test
  @DisplayName("Should return empty list when findAllByBuyerName is called and repository file contains invalid data.")
  void findAllByBuyerNameShouldReturnEmptyListWhenDatabaseFileContainsInvalidData() throws RepositoryOperationException, IOException {
    //given
    FileUtils.writeLines(databaseFile, Collections.singletonList("xyz"), null);

    //when
    Iterable<Invoice> actualInvoicesByBuyerName = inFileRepository.findAllByBuyerName(getRandomInvoice().getBuyer().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoicesByBuyerName);
  }

  @Test
  @DisplayName("Should return number of invoices in database when count is called.")
  void countShouldReturnProperNumberOfInvoices() throws IOException, RepositoryOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoice();
    Invoice invoice3 = getRandomInvoice();
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    String invoice3AsJson = mapper.writeValueAsString(invoice3);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), null);

    //when
    long actualInvoiceCount = inFileRepository.count();

    //then
    assertEquals(3L, actualInvoiceCount);
  }

  @Test
  @DisplayName("Should return 0 when count is called and database file is empty.")
  void countInvoicesShouldReturnZeroWhenDatabaseIsEmpty() throws RepositoryOperationException {
    //when
    long actualInvoiceCount = inFileRepository.count();

    //then
    assertEquals(0L, actualInvoiceCount);
  }

  @Test
  @DisplayName("Should return 0 when count is called and database file contains invalid data.")
  void countShouldReturnZeroWhenDatabaseContainsInvalidData() throws RepositoryOperationException, IOException {
    //given
    FileUtils.writeLines(databaseFile, Collections.singletonList("xyz"), null);

    //when
    long actualInvoiceCount = inFileRepository.count();

    //then
    assertEquals(0L, actualInvoiceCount);
  }

  @Test
  @DisplayName("Should return true when existsById is called and specified invoice is present in database.")
  void shouldReturnTrueWhenSpecifiedInvoiceExists() throws IOException, RepositoryOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoice();
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson), null);

    //when
    boolean actualResult = inFileRepository.existsById(invoice2.getId());

    //then
    assertTrue(actualResult);
  }

  @Test
  @DisplayName("Should return false when existsById is called and specified invoice is not present in database.")
  void shouldReturnFalseWhenSpecifiedInvoiceDoesNotExist() throws IOException, RepositoryOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoice();
    Invoice invoice3 = getRandomInvoice();
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson), null);

    //when
    boolean actualResult = inFileRepository.existsById(invoice3.getId());

    //then
    assertFalse(actualResult);
  }

  @Test
  @DisplayName("Should return false when existsById is called and database file is empty.")
  void existsByIdShouldReturnFalseWhenDatabaseIsEmpty() throws RepositoryOperationException {
    //when
    boolean actualResult = inFileRepository.existsById(getRandomInvoice().getId());

    //then
    assertFalse(actualResult);
  }

  @Test
  @DisplayName("Should return false when existsById is called and database file contains invalid data.")
  void existsByIdShouldReturnFalseWhenDatabaseContainsInvalidData() throws IOException, RepositoryOperationException {
    //given
    FileUtils.writeLines(databaseFile, Collections.singletonList("xyz"), null);

    //when
    boolean actualResult = inFileRepository.existsById(getRandomInvoice().getId());

    //then
    assertFalse(actualResult);
  }

  @Test
  @DisplayName("Should delete specified invoice when deleteById is invoked.")
  void shouldDeleteSpecifiedInvoice() throws IOException, RepositoryOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoice();
    Invoice invoice3 = getRandomInvoice();
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    String invoice3AsJson = mapper.writeValueAsString(invoice3);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), null);
    FileUtils.writeLines(expectedDatabaseFile, Arrays.asList(invoice1AsJson, invoice3AsJson), null);

    //when
    inFileRepository.deleteById(invoice2.getId());

    //then
    assertTrue(FileUtils.contentEquals(expectedDatabaseFile, databaseFile));
  }

  @Test
  @DisplayName("Should not alter database contents if deleteById invoked and specified invoice does not exist.")
  void deleteByIdShouldNotChangeDatabaseContentsWhenInvoiceDoesNotExist() throws IOException, RepositoryOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithSpecificId(10);
    Invoice invoice2 = getRandomInvoiceWithSpecificId(11);
    Invoice invoice3 = getRandomInvoiceWithSpecificId(12);
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    String invoice3AsJson = mapper.writeValueAsString(invoice3);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), null);
    FileUtils.writeLines(expectedDatabaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), null);

    //when
    inFileRepository.deleteById(123456789);

    //then
    assertTrue(FileUtils.contentEquals(expectedDatabaseFile, databaseFile));
  }

  @Test
  @DisplayName("Should not alter database file contents if deleteById invoked and database file is empty.")
  void deleteByIdShouldDoNothingWhenDatabaseFileIsEmpty() throws IOException, RepositoryOperationException {
    //when
    inFileRepository.deleteById(getRandomInvoice().getId());

    //then
    assertTrue(FileUtils.contentEquals(expectedDatabaseFile, databaseFile));
  }

  @Test
  @DisplayName("Should delete all invoices when deleteAll is invoked.")
  void shouldDeleteAllInvoices() throws IOException, RepositoryOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoice();
    Invoice invoice3 = getRandomInvoice();
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    String invoice3AsJson = mapper.writeValueAsString(invoice3);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), null);
    FileUtils.writeLines(expectedDatabaseFile, Arrays.asList(invoice1AsJson, invoice3AsJson), null);

    //when
    inFileRepository.deleteAll();

    //then
    assertEquals(0, databaseFile.length());
  }

  @Test
  @DisplayName("Should not alter database file contents if deleteAll invoked and database file is empty.")
  void deleteAllShouldDoNothingWhenDatabaseFileIsEmpty() throws IOException, RepositoryOperationException {
    //when
    inFileRepository.deleteAll();

    //then
    assertTrue(FileUtils.contentEquals(expectedDatabaseFile, databaseFile));
  }
}
