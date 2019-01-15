package pl.coderstrust.database.invoice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.coderstrust.generators.InvoiceGenerator.copyInvoice;
import static pl.coderstrust.generators.InvoiceGenerator.getRandomInvoice;
import static pl.coderstrust.generators.InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName;
import static pl.coderstrust.generators.InvoiceGenerator.getRandomInvoiceWithSpecificId;
import static pl.coderstrust.generators.InvoiceGenerator.getRandomInvoiceWithSpecificSellerName;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderstrust.configuration.ApplicationConfiguration;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.helpers.FileHelper;
import pl.coderstrust.helpers.FileHelperException;
import pl.coderstrust.model.Invoice;

@ExtendWith(MockitoExtension.class)
class InFileInvoiceDatabaseTest {

  private static ObjectMapper mapper = new ApplicationConfiguration().getObjectMapper();

  @Mock
  private FileHelper fileHelperMock;

  private InvoiceDatabase database;

  @BeforeEach
  void setUp() throws DatabaseOperationException {
    database = new InFileInvoiceDatabase(fileHelperMock, mapper);
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when FileHelper.initialize() throws IOException.")
  void constructorShouldThrowExceptionWhenFileHelperInitializeThrowsIoException() throws IOException, FileHelperException {
    //given
    doThrow(IOException.class).when(fileHelperMock).initialize();

    //then
    assertThrows(DatabaseOperationException.class, () -> new InFileInvoiceDatabase(fileHelperMock, mapper));
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when FileHelper.initialize() throws FileHelperException.")
  void constructorShouldThrowExceptionWhenFileHelperInitializeThrowsFileHelperException() throws IOException, FileHelperException {
    //given
    doThrow(FileHelperException.class).when(fileHelperMock).initialize();

    //then
    assertThrows(DatabaseOperationException.class, () -> new InFileInvoiceDatabase(fileHelperMock, mapper));
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when FileHelper.readLastLine() throws IOException.")
  void constructorShouldThrowExceptionWhenFileHelperReadLastLineThrowsException() throws IOException {
    //given
    doThrow(IOException.class).when(fileHelperMock).readLastLine();

    //then
    assertThrows(DatabaseOperationException.class, () -> new InFileInvoiceDatabase(fileHelperMock, mapper));
  }

  @Test
  @DisplayName("Should return saved invoice with new id when save is invoked and new invoice is passed as parameter.")
  void saveShouldReturnSavedInvoiceWithProperIdWhenNewInvoicePassed() throws DatabaseOperationException, IOException {
    //given
    Invoice invoice = getRandomInvoiceWithSpecificId("5");
    Invoice expectedInvoice = copyInvoice(invoice);
    expectedInvoice.setId("1");
    String expectedInvoiceAsJson = mapper.writeValueAsString(expectedInvoice);
    when(fileHelperMock.readLines()).thenReturn(Collections.emptyList());
    doNothing().when(fileHelperMock).writeLine(expectedInvoiceAsJson);

    //when
    Invoice actualInvoice = database.save(invoice);

    //then
    assertEquals(expectedInvoice, actualInvoice);
    verify(fileHelperMock).writeLine(expectedInvoiceAsJson);
  }

  @Test
  @DisplayName("Should return saved invoice with original id when save is invoked and invoice with existing id is passed as parameter.")
  void saveShouldReturnSavedInvoiceWithOriginalIdWhenExistingInvoicePassed() throws DatabaseOperationException, IOException, FileHelperException {
    //given
    Invoice invoice1 = getRandomInvoiceWithSpecificId("3");
    Invoice invoice2 = getRandomInvoiceWithSpecificId("5");
    Invoice invoice3 = getRandomInvoiceWithSpecificId("3");
    String invoice1AsJson = mapper.writeValueAsString(invoice1);
    String invoice2AsJson = mapper.writeValueAsString(invoice2);
    String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson));
    doNothing().when(fileHelperMock).writeLine(invoice3AsJson);
    doNothing().when(fileHelperMock).removeLine(1);

    //when
    Invoice savedInvoice = database.save(invoice3);

    //then
    assertNotEquals(invoice1, invoice3);
    assertEquals(invoice3, savedInvoice);
    verify(fileHelperMock).writeLine(invoice3AsJson);
    verify(fileHelperMock).removeLine(1);
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when save is invoked and fileHelper throws exception.")
  void saveShouldThrowExceptionWhenFileHelperThrowsException() throws IOException {
    //given
    final Invoice invoice = getRandomInvoiceWithSpecificId("1");
    final String invoiceAsJson = mapper.writeValueAsString(invoice);
    doThrow(IOException.class).when(fileHelperMock).writeLine(invoiceAsJson);

    //then
    assertThrows(DatabaseOperationException.class, () -> database.save(invoice));
    verify(fileHelperMock).writeLine(invoiceAsJson);
  }

  @Test
  @DisplayName("Should return invoice with specified id when findById is invoked.")
  void shouldReturnInvoiceWithSpecifiedId() throws DatabaseOperationException, IOException {
    //given
    final Invoice invoice1 = getRandomInvoice();
    final Invoice invoice2 = getRandomInvoice();
    final Invoice invoice3 = getRandomInvoice();
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    Optional<Invoice> actualInvoice = database.findById(invoice2.getId());

    //then
    assertEquals(Optional.of(invoice2), actualInvoice);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty optional when findById is invoked and invoice does not exist.")
  void findByIdShouldReturnEmptyOptionalWhenInvoiceDoesNotExist() throws DatabaseOperationException, IOException {
    //given
    final String invoiceAsJson = mapper.writeValueAsString(getRandomInvoice());
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList(invoiceAsJson));

    //when
    Optional<Invoice> actualInvoice = database.findById("-1");

    //then
    assertEquals(Optional.empty(), actualInvoice);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty optional when findById is invoked and input stream is empty.")
  void findByIdShouldReturnEmptyOptionalWhenInputStreamIsEmpty() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.emptyList());

    //when
    Optional<Invoice> actualInvoice = database.findById("-1");

    //then
    assertEquals(Optional.empty(), actualInvoice);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty optional when findById is invoked and input stream contains invalid data.")
  void findByIdShouldReturnEmptyOptionalWhenInputStreamContainsInvalidData() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    Optional<Invoice> actualInvoice = database.findById("-1");

    //then
    assertEquals(Optional.empty(), actualInvoice);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when findById is invoked and fileHelper throws exception.")
  void findByIdShouldThrowExceptionWhenFileHelperThrowsException() throws IOException {
    //given
    doThrow(IOException.class).when(fileHelperMock).readLines();

    //then
    assertThrows(DatabaseOperationException.class, () -> database.findById(getRandomInvoice().getId()));
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return all invoices from database when findAll is invoked.")
  void shouldReturnAllInvoices() throws DatabaseOperationException, IOException {
    //given
    final Invoice invoice1 = getRandomInvoice();
    final Invoice invoice2 = getRandomInvoice();
    final Invoice invoice3 = getRandomInvoice();
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    Iterable<Invoice> actualInvoices = database.findAll();

    //then
    assertEquals(Arrays.asList(invoice1, invoice2, invoice3), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty list when findAll is invoked and input stream is empty.")
  void findAllShouldReturnEmptyListWhenInputStreamIsEmpty() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.emptyList());

    //when
    Iterable<Invoice> actualInvoices = database.findAll();

    //then
    assertEquals(new ArrayList<>(), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty list when findAll is invoked and input stream contains invalid data.")
  void findAllShouldReturnEmptyListWhenInputStreamContainsInvalidData() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    Iterable<Invoice> actualInvoices = database.findAll();

    //then
    assertEquals(new ArrayList<>(), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when findAll is invoked and fileHelper.readLines throws exception.")
  void findAllShouldThrowExceptionWhenFileHelperReadLinesThrowsException() throws IOException {
    //given
    doThrow(IOException.class).when(fileHelperMock).readLines();

    //then
    assertThrows(DatabaseOperationException.class, () -> database.findAll());
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return all invoices associated with particular seller name.")
  void shouldReturnAllInvoicesBySellerName() throws DatabaseOperationException, IOException {
    //given
    final Invoice invoice1 = getRandomInvoiceWithSpecificSellerName("Company One");
    final Invoice invoice2 = getRandomInvoiceWithSpecificSellerName("Company One");
    final Invoice invoice3 = getRandomInvoiceWithSpecificSellerName("Company Two");
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    Iterable<Invoice> actualInvoices = database.findAllBySellerName(invoice1.getSeller().getName());

    //then
    assertEquals(Arrays.asList(invoice1, invoice2), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty list when findAllBySellerName is invoked and input stream is empty.")
  void findAllBySellerNameShouldReturnEmptyListWhenInputStreamEmpty() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.emptyList());

    //when
    Iterable<Invoice> actualInvoices = database.findAllBySellerName(getRandomInvoice().getSeller().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty list when findAllBySellerName is invoked and input stream contains invalid data.")
  void findAllInvoicesBySellerNameShouldReturnEmptyListWhenInputStreamContainsInvalidData() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    Iterable<Invoice> actualInvoices = database.findAllBySellerName("Sample Company");

    //then
    assertEquals(new ArrayList<>(), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should empty list when findAllBySellerName is invoked and requested seller is not present in database.")
  void findAllBySellerNameShouldReturnEmptyListWhenSellerMissing() throws IOException, DatabaseOperationException {
    //given
    final Invoice invoice1 = getRandomInvoiceWithSpecificSellerName("Company One");
    final Invoice invoice2 = getRandomInvoiceWithSpecificSellerName("Company Two");
    final Invoice invoice3 = getRandomInvoiceWithSpecificSellerName("Company Three");
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    Iterable<Invoice> actualInvoicesBySellerName = database.findAllBySellerName("A.C.M.E. Incorporated");

    //then
    assertEquals(Collections.emptyList(), actualInvoicesBySellerName);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when findAllBySellerName is invoked and fileHelper.readLines throws exception.")
  void findAllBySellerNameShouldThrowExceptionWhenFileHelperReadLinesThrowsException() throws IOException {
    //given
    Invoice invoice = getRandomInvoice();
    doThrow(IOException.class).when(fileHelperMock).readLines();

    //then
    assertThrows(DatabaseOperationException.class, () -> database.findAllBySellerName(invoice.getSeller().getName()));
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return all invoices associated with particular buyer name when findAllByBuyerName is invoked.")
  void shouldReturnAllInvoicesByBuyerName() throws DatabaseOperationException, IOException {
    //given
    final Invoice invoice1 = getRandomInvoiceWithSpecificBuyerName("Company One");
    final Invoice invoice2 = getRandomInvoiceWithSpecificBuyerName("Company Two");
    final Invoice invoice3 = getRandomInvoiceWithSpecificBuyerName("Company One");
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    Iterable<Invoice> actualInvoices = database.findAllByBuyerName(invoice1.getBuyer().getName());

    //then
    assertEquals(Arrays.asList(invoice1, invoice3), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty list when findAllByBuyerName is invoked and input stream is empty.")
  void shouldReturnEmptyListWhenFindAllByBuyerNameInvokedAndInputStreamIsEmpty() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.emptyList());

    //when
    Iterable<Invoice> actualInvoices = database.findAllByBuyerName(getRandomInvoice().getBuyer().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty list when findAllByBuyerName is invoked and input stream contains invalid data.")
  void findAllByBuyerNameShouldReturnEmptyListWhenInputStreamContainsInvalidData() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    Iterable<Invoice> actualInvoices = database.findAllByBuyerName(getRandomInvoice().getBuyer().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty list when findAllByBuyerName is invoked and specified buyer is missing.")
  void findAllByBuyerNameShouldReturnEmptyListWhenBuyerMissing() throws IOException, DatabaseOperationException {
    //given
    final Invoice invoice1 = getRandomInvoiceWithSpecificBuyerName("Company One");
    final Invoice invoice2 = getRandomInvoiceWithSpecificBuyerName("Company Two");
    final Invoice invoice3 = getRandomInvoiceWithSpecificBuyerName("Company Three");
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    Iterable<Invoice> actualInvoicesByBuyerName = database.findAllByBuyerName("A.C.M.E. Incorporated");

    //then
    assertEquals(Collections.emptyList(), actualInvoicesByBuyerName);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when findAllByBuyerName is invoked and fileHelper.readLines throws exception.")
  void findAllByBuyerNameShouldThrowExceptionWhenFileHelperReadLinesThrowsException() throws IOException {
    //given
    doThrow(IOException.class).when(fileHelperMock).readLines();

    //then
    assertThrows(DatabaseOperationException.class, () -> database.findAllByBuyerName(getRandomInvoice().getBuyer().getName()));
    verify(fileHelperMock).readLines();
  }

  @ParameterizedTest
  @DisplayName("Should return number of invoices in database.")
  @MethodSource("countInvoicesTestParameters")
  void shouldReturnInvoiceCount(List<String> invoices, Long expectedInvoiceCount) throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.isEmpty()).thenReturn(false);
    when(fileHelperMock.readLines()).thenReturn(invoices);

    //when
    Long actualInvoiceCount = database.count();

    //then
    assertEquals(expectedInvoiceCount, actualInvoiceCount);
    verify(fileHelperMock).isEmpty();
    verify(fileHelperMock).readLines();
  }

  private static Stream<Arguments> countInvoicesTestParameters() throws IOException {
    String invoice1AsJson = mapper.writeValueAsString(getRandomInvoice());
    String invoice2AsJson = mapper.writeValueAsString(getRandomInvoice());
    String invoice3AsJson = mapper.writeValueAsString(getRandomInvoice());
    return Stream.of(
        Arguments.of(Collections.singletonList(invoice1AsJson), 1L),
        Arguments.of(Arrays.asList(invoice1AsJson, invoice2AsJson), 2L),
        Arguments.of(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson), 3L)
    );
  }

  @Test
  @DisplayName("Should return 0 when database is empty and count is invoked.")
  void countShouldReturnZeroWhenDatabaseIsEmpty() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.isEmpty()).thenReturn(false);

    //when
    Long actualInvoiceCount = database.count();

    //then
    assertEquals(Long.valueOf(0), actualInvoiceCount);
    verify(fileHelperMock).isEmpty();
  }

  @Test
  @DisplayName("Should return 0 when database contains invalid data and count is invoked.")
  void countShouldReturnZeroWhenDatabaseContainsInvalidData() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    Long actualInvoiceCount = database.count();

    //then
    assertEquals(Long.valueOf(0), actualInvoiceCount);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return false when existsById is invoked and input stream contains invalid data.")
  void existsByIdShouldReturnFalseWhenInputStreamContainsInvalidData() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    boolean result = database.existsById("-1");

    //then
    assertFalse(result);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return true when existsById is invoked and given invoice is present in database.")
  void shouldReturnTrueWhenInvoiceExists() throws DatabaseOperationException, IOException {
    //given
    final Invoice invoice1 = getRandomInvoice();
    final Invoice invoice2 = getRandomInvoice();
    final Invoice invoice3 = getRandomInvoice();
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    boolean result = database.existsById(invoice2.getId());

    //then
    assertTrue(result);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return false when existsById is invoked and given invoice is not present in database.")
  void shouldReturnFalseWhenInvoiceDoesNotExist() throws DatabaseOperationException, IOException {
    //given
    final Invoice invoice1 = getRandomInvoice();
    final Invoice invoice2 = getRandomInvoice();
    final Invoice invoice3 = getRandomInvoice();
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    boolean result = database.existsById("-1");

    //then
    assertFalse(result);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when existsById invoked and fileHelper throws exception.")
  void existsByIdShouldThrowExceptionWhenFileHelperReadLinesThrowsException() throws IOException {
    //given
    doThrow(IOException.class).when(fileHelperMock).readLines();

    //then
    assertThrows(DatabaseOperationException.class, () -> database.existsById(getRandomInvoice().getId()));
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should pass proper line number to fileHelper.removeLine when deleteById with specified invoiceId is invoked.")
  void shouldPassProperLineNumberToFileHelperRemoveLineWhenDeleteByIdInvoked() throws Exception {
    //given
    final Invoice invoice1 = getRandomInvoice();
    final Invoice invoice2 = getRandomInvoice();
    final Invoice invoice3 = getRandomInvoice();
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));
    doNothing().when(fileHelperMock).removeLine(anyInt());

    //when
    database.deleteById(invoice2.getId());

    //then
    verify(fileHelperMock).removeLine(2);
  }

  @Test
  @DisplayName("Should not pass line number to fileHelper.removeLine when deleteById is invoked and invoice with particular id does not exist.")
  void shouldThrowExceptionWhenTryingPassLineNumberToFileHelperRemoveLineWhenDeleteByIdInvokedAndInvoiceDoesNotExist() throws Exception {
    //given
    final Invoice invoice1 = getRandomInvoice();
    final Invoice invoice2 = getRandomInvoice();
    final Invoice invoice3 = getRandomInvoice();
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //then
    assertThrows(DatabaseOperationException.class, () -> database.deleteById("-1"));
    verify(fileHelperMock, times(0)).removeLine(anyInt());
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when deleteById is invoked and fileHelper.removeLine throws exception.")
  void deleteByIdShouldThrowExceptionWhenFileHelperRemoveLineThrowsException() throws Exception {
    //given
    Invoice invoice = getRandomInvoice();
    String invoiceAsJson = mapper.writeValueAsString(invoice);
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList(invoiceAsJson));
    doThrow(FileHelperException.class).when(fileHelperMock).removeLine(anyInt());

    //then
    assertThrows(DatabaseOperationException.class, () -> database.deleteById(invoice.getId()));
    verify(fileHelperMock).readLines();
    verify(fileHelperMock).removeLine(anyInt());
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when deleteAll is invoked and fileHelper.clear throws exception.")
  void deleteAllShouldThrowExceptionWhenFileHelperClearThrowsException() throws IOException {
    //given
    doThrow(IOException.class).when(fileHelperMock).clear();

    //then
    assertThrows(DatabaseOperationException.class, () -> database.deleteAll());
    verify(fileHelperMock).clear();
  }

  @Test
  @DisplayName("Should clear database file when deleteAll is invoked.")
  void deleteAllShouldClearDatabaseFile() throws IOException, DatabaseOperationException {
    //given
    doNothing().when(fileHelperMock).clear();

    //when
    database.deleteAll();

    //then
    verify(fileHelperMock).clear();
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to save.")
  void shouldThrowExceptionWhenNullArgumentPassedToSave() {
    assertThrows(IllegalArgumentException.class, () -> database.save(null));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to findById.")
  void shouldThrowExceptionWhenNullArgumentPassedToFindById() {
    assertThrows(IllegalArgumentException.class, () -> database.findById(null));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to findAllBySellerName.")
  void shouldThrowExceptionWhenNullArgumentPassedToFindAllBySellerName() {
    assertThrows(IllegalArgumentException.class, () -> database.findAllBySellerName(null));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to findAllByBuyerName.")
  void shouldThrowExceptionWhenNullArgumentPassedToFindAllByBuyerName() {
    assertThrows(IllegalArgumentException.class, () -> database.findAllByBuyerName(null));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to deleteById.")
  void shouldThrowExceptionWhenNullArgumentPassedToDeleteById() {
    assertThrows(IllegalArgumentException.class, () -> database.deleteById(null));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to existsById.")
  void shouldThrowExceptionWhenNullArgumentPassedToExistsById() {
    assertThrows(IllegalArgumentException.class, () -> database.existsById(null));
  }
}
