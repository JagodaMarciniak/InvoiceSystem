package pl.coderstrust.integrationtests.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.coderstrust.configuration.Configuration;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.database.InFileDatabase;
import pl.coderstrust.helpers.FileHelperImpl;
import pl.coderstrust.model.Invoice;

public class InFileDatabaseIT {

  private final ObjectMapper mapper = Configuration.getObjectMapper();
  private final String expectedDatabaseFilePath = String.format("%1$s%2$ssrc%2$stest%2$sresources%2$sdatabase%2$s%3$s",
      System.getProperty("user.dir"), File.separator, "expected_invoice_database.txt");
  private final File databaseFile = new File(Configuration.DATABASE_FILE_PATH);
  private final File expectedDatabaseFile = new File(expectedDatabaseFilePath);
  private Database inFileDatabase;

  @BeforeEach
  void setUp() throws Exception {
    if (databaseFile.exists()) {
      databaseFile.delete();
    }
    inFileDatabase = new InFileDatabase(new FileHelperImpl(Configuration.DATABASE_FILE_PATH), mapper);
    if (expectedDatabaseFile.exists()) {
      expectedDatabaseFile.delete();
      expectedDatabaseFile.createNewFile();
    }
  }

  @Test
  @DisplayName("Should save new invoice to database when saveInvoice is invoked.")
  void saveInvoiceShouldSaveNewInvoiceToDatabase() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice = getRandomInvoice();
    String invoiceAsJson = mapper.writeValueAsString(invoice);
    FileUtils.writeLines(expectedDatabaseFile, Collections.singleton(invoiceAsJson), null);

    //when
    inFileDatabase.saveInvoice(invoice);

    //then
    assertTrue(FileUtils.contentEquals(expectedDatabaseFile, databaseFile));
  }

  @Test
  @DisplayName("Should replace invoice in database file when saveInvoice is called and invoiceId is already present in database.")
  void saveInvoiceShouldReplaceInvoiceInDatabase() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice = getRandomInvoiceWithSpecificId("FV/2018/11/23");
    Invoice alteredInvoice = getRandomInvoiceWithSpecificId("FV/2018/11/23");
    String invoiceAsJson = mapper.writeValueAsString(invoice);
    String alteredInvoiceAsJson = mapper.writeValueAsString(alteredInvoice);
    FileUtils.writeLines(expectedDatabaseFile, Collections.singleton(alteredInvoiceAsJson), null);
    FileUtils.writeLines(databaseFile, Collections.singleton(invoiceAsJson), null);

    //when
    inFileDatabase.saveInvoice(alteredInvoice);

    //then
    assertTrue(FileUtils.contentEquals(expectedDatabaseFile, databaseFile));
  }

  @Test
  @DisplayName("Should return specified invoice when findOneInvoice is invoked.")
  void findOneInvoiceShouldReturnSpecifiedInvoice() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoice();
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson), null);

    //when
    Invoice actualInvoice = inFileDatabase.findOneInvoice(invoice1.getId());

    //then
    assertEquals(invoice1, actualInvoice);
  }

  @Test
  @DisplayName("Should return null when findOneInvoice is invoked and invoice that is searched for is missing.")
  void findOneInvoiceShouldReturnNullWhenInvoiceIsMissing() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoice();
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson), null);

    //when
    Invoice actualInvoice = inFileDatabase.findOneInvoice("FV/XXXX/XX/XXXX");

    //then
    assertNull(actualInvoice);
  }

  @Test
  @DisplayName("Should return null when findOneInvoice is called and database file is empty.")
  void shouldReturnNullWhenFinOneInvoiceCalledAndDatabaseFileIsEmpty() throws DatabaseOperationException, IOException {
    //when
    Invoice actualInvoice = inFileDatabase.findOneInvoice(getRandomInvoice().getId());

    //then
    assertNull(actualInvoice);
  }

  @Test
  @DisplayName("Should return null when findOneInvoice is called and database file contains invalid data.")
  void shouldReturnNullWhenFinOneInvoiceCalledAndDatabaseFileContainsInvalidData() throws DatabaseOperationException, IOException {
    //given
    FileUtils.writeLines(databaseFile, Collections.singletonList("xyz"), null);

    //when
    Invoice actualInvoice = inFileDatabase.findOneInvoice(getRandomInvoice().getId());

    //then
    assertNull(actualInvoice);
  }

  @Test
  @DisplayName("Should return all invoices from database when findAllInvoices is invoked.")
  void shouldReturnAllInvoices() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoice();
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson), null);

    //when
    List<Invoice> actualInvoices = inFileDatabase.findAllInvoices();

    //then
    assertEquals(Arrays.asList(invoice1, invoice2), actualInvoices);
  }

  @Test
  @DisplayName("Should return empty list when findAllInvoices is invoked and database file is empty.")
  void findAllInvoicesShouldReturnEmptyListWhenDatabaseIsEmpty() throws DatabaseOperationException {
    //when
    List<Invoice> actualInvoices = inFileDatabase.findAllInvoices();

    //then
    assertEquals(Collections.emptyList(), actualInvoices);
  }

  @Test
  @DisplayName("Should return all invoices by specified seller name when findAllInvoicesBySellerName is invoked.")
  void shouldReturnAllInvoicesBySpecifiedSellerName() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoiceWithSpecificSellerName("Warner Brothers");
    Invoice invoice3 = getRandomInvoiceWithSpecificSellerName("Warner Brothers");
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    String invoice3AsJson = mapper.writeValueAsString(invoice3);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), null);

    //when
    List<Invoice> actualInvoicesBySellerName = inFileDatabase.findAllInvoicesBySellerName(invoice2.getSeller().getName());

    //then
    assertEquals(Arrays.asList(invoice2, invoice3), actualInvoicesBySellerName);
  }

  @Test
  @DisplayName("Should return empty list when findAllInvoicesBySellerName is called and specified seller is missing.")
  void findAllInvoicesBySellerNameShouldReturnEmptyListWhenSellerMissing() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithSpecificSellerName("Universal Studios");
    Invoice invoice2 = getRandomInvoiceWithSpecificSellerName("Walt Disney");
    Invoice invoice3 = getRandomInvoiceWithSpecificSellerName("Dreamworks");
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    String invoice3AsJson = mapper.writeValueAsString(invoice3);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), null);

    //when
    List<Invoice> actualInvoicesBySellerName = inFileDatabase.findAllInvoicesBySellerName("XYZ");

    //then
    assertEquals(Collections.emptyList(), actualInvoicesBySellerName);
  }

  @Test
  @DisplayName("Should return empty list when findAllInvoicesBySellerName is invoked and database file is empty.")
  void findAllInvoicesBySellerNameShouldReturnEmptyListWhenDatabaseIsEmpty() throws DatabaseOperationException, IOException {
    //when
    List<Invoice> actualInvoicesBySellerName = inFileDatabase.findAllInvoicesBySellerName(getRandomInvoice().getSeller().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoicesBySellerName);
  }

  @Test
  @DisplayName("Should return empty list when findAllInvoicesBySellerName is invoked and database file contains invalid data.")
  void findAllInvoicesBySellerNameShouldReturnEmptyListWhenDatabaseContainsInvalidData() throws DatabaseOperationException, IOException {
    //given
    FileUtils.writeLines(databaseFile, Collections.singletonList("xyz"), null);

    //when
    List<Invoice> actualInvoicesBySellerName = inFileDatabase.findAllInvoicesBySellerName(getRandomInvoice().getSeller().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoicesBySellerName);
  }

  @Test
  @DisplayName("Should return all invoices by specified buyer name when findAllInvoicesByBuyerName is called.")
  void shouldReturnAllInvoicesByBuyerName() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithSpecificBuyerName("Logitech");
    Invoice invoice2 = getRandomInvoiceWithSpecificBuyerName("Logitech");
    Invoice invoice3 = getRandomInvoiceWithSpecificBuyerName("Samsung");
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    String invoice3AsJson = mapper.writeValueAsString(invoice3);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), null);

    //when
    List<Invoice> actualInvoicesByBuyerName = inFileDatabase.findAllInvoicesByBuyerName(invoice2.getBuyer().getName());

    //then
    assertEquals(Arrays.asList(invoice1, invoice2), actualInvoicesByBuyerName);
  }

  @Test
  @DisplayName("Should return empty list when findAllInvoicesByBuyerName invoked and specified buyer is missing.")
  void findAllInvoicesByBuyerNameShouldReturnEmptyListWhenBuyerIsMissing() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithSpecificBuyerName("Logitech");
    Invoice invoice2 = getRandomInvoiceWithSpecificBuyerName("Apple");
    Invoice invoice3 = getRandomInvoiceWithSpecificBuyerName("Samsung");
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    String invoice3AsJson = mapper.writeValueAsString(invoice3);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), null);

    //when
    List<Invoice> actualInvoicesByBuyerName = inFileDatabase.findAllInvoicesBySellerName("Hitachi");

    //then
    assertEquals(Collections.emptyList(), actualInvoicesByBuyerName);
  }

  @Test
  @DisplayName("Should return empty list when findAllInvoicesByBuyerName is called and database file is empty.")
  void findAllInvoicesByBuyerNameShouldReturnEmptyListWhenDatabaseFileIsEmpty() throws DatabaseOperationException, IOException {
    //when
    List<Invoice> actualInvoicesByBuyerName = inFileDatabase.findAllInvoicesByBuyerName(getRandomInvoice().getBuyer().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoicesByBuyerName);
  }

  @Test
  @DisplayName("Should return empty list when findAllInvoicesByBuyerName is called and database file contains invalid data.")
  void findAllInvoicesByBuyerNameShouldReturnEmptyListWhenDatabaseFileContainsInvalidData() throws DatabaseOperationException, IOException {
    //given
    FileUtils.writeLines(databaseFile, Collections.singletonList("xyz"), null);

    //when
    List<Invoice> actualInvoicesByBuyerName = inFileDatabase.findAllInvoicesByBuyerName(getRandomInvoice().getBuyer().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoicesByBuyerName);
  }

  @Test
  @DisplayName("Should return number of invoices in database when countInvoices is called.")
  void countInvoicesShouldReturnProperNumberOfInvoices() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoice();
    Invoice invoice3 = getRandomInvoice();
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    String invoice3AsJson = mapper.writeValueAsString(invoice3);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), null);

    //when
    long actualInvoiceCount = inFileDatabase.countInvoices();

    //then
    assertEquals(3L, actualInvoiceCount);
  }

  @Test
  @DisplayName("Should return 0 when countInvoices is called and database file is empty.")
  void countInvoicesShouldReturnZeroWhenDatabaseIsEmpty() throws DatabaseOperationException, IOException {
    //when
    long actualInvoiceCount = inFileDatabase.countInvoices();

    //then
    assertEquals(0L, actualInvoiceCount);
  }

  @Test
  @DisplayName("Should return 0 when countInvoices is called and database file contains invalid data.")
  void countInvoicesShouldReturnZeroWhenDatabaseContainsInvalidData() throws DatabaseOperationException, IOException {
    //given
    FileUtils.writeLines(databaseFile, Collections.singletonList("xyz"), null);

    //when
    long actualInvoiceCount = inFileDatabase.countInvoices();

    //then
    assertEquals(0L, actualInvoiceCount);
  }

  @Test
  @DisplayName("Should return true when invoiceExists is called and specified invoice is present in database.")
  void shouldReturnTrueWhenSpecifiedInvoiceExists() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoice();
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson), null);

    //when
    boolean actualResult = inFileDatabase.invoiceExists(invoice2.getId());

    //then
    assertTrue(actualResult);
  }

  @Test
  @DisplayName("Should return false when invoiceExists is called and specified invoice is not present in database.")
  void shouldReturnFalseWhenSpecifiedInvoiceDoesNotExist() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoice();
    Invoice invoice2 = getRandomInvoice();
    Invoice invoice3 = getRandomInvoice();
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson), null);

    //when
    boolean actualResult = inFileDatabase.invoiceExists(invoice3.getId());

    //then
    assertFalse(actualResult);
  }

  @Test
  @DisplayName("Should return false when invoiceExists is called and database file is empty.")
  void invoiceExistsShouldReturnFalseWhenDatabaseIsEmpty() throws DatabaseOperationException {
    //when
    boolean actualResult = inFileDatabase.invoiceExists(getRandomInvoice().getId());

    //then
    assertFalse(actualResult);
  }

  @Test
  @DisplayName("Should return false when invoiceExists is called and database file contains invalid data.")
  void invoiceExistsShouldReturnFalseWhenDatabaseContainsInvalidData() throws IOException, DatabaseOperationException {
    //given
    FileUtils.writeLines(databaseFile, Collections.singletonList("xyz"), null);

    //when
    boolean actualResult = inFileDatabase.invoiceExists(getRandomInvoice().getId());

    //then
    assertFalse(actualResult);
  }

  @Test
  @DisplayName("Should delete specified invoice when deleteInvoice is invoked.")
  void shouldDeleteSpecifiedInvoice() throws IOException, DatabaseOperationException {
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
    inFileDatabase.deleteInvoice(invoice2.getId());

    //then
    assertTrue(FileUtils.contentEquals(expectedDatabaseFile, databaseFile));
  }

  @Test
  @DisplayName("Should not alter database contents if deleteInvoice invoked and specified invoice does not exist.")
  void deleteInvoiceShouldNotChangeDatabaseContentsWhenInvoiceDoesNotExist() throws IOException, DatabaseOperationException {
    //given
    Invoice invoice1 = getRandomInvoiceWithSpecificId("FV/3/1");
    Invoice invoice2 = getRandomInvoiceWithSpecificId("FV/3/2");
    Invoice invoice3 = getRandomInvoiceWithSpecificId("FV/3/3");
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    String invoice3AsJson = mapper.writeValueAsString(invoice3);
    FileUtils.writeLines(databaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), null);
    FileUtils.writeLines(expectedDatabaseFile, Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), null);

    //when
    inFileDatabase.deleteInvoice("FV/X/X");

    //then
    assertTrue(FileUtils.contentEquals(expectedDatabaseFile, databaseFile));
  }

  @Test
  @DisplayName("Should not alter database file contents if deleteInvoice invoked and database file is empty.")
  void deleteInvoiceShouldDoNothingWhenDatabaseFileIsEmpty() throws IOException, DatabaseOperationException {
    //when
    inFileDatabase.deleteInvoice(getRandomInvoice().getId());

    //then
    assertTrue(FileUtils.contentEquals(expectedDatabaseFile, databaseFile));
  }
}
