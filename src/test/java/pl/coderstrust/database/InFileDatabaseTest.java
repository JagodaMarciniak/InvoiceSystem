package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.coderstrust.generators.InvoiceGenerator.getRandomInvoice;
import static pl.coderstrust.generators.InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName;
import static pl.coderstrust.generators.InvoiceGenerator.getRandomInvoiceWithSpecificSellerName;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
import pl.coderstrust.configuration.Configuration;
import pl.coderstrust.helpers.FileHelper;
import pl.coderstrust.model.Invoice;

@ExtendWith(MockitoExtension.class)
class InFileDatabaseTest {

  private static ObjectMapper mapper = Configuration.getObjectMapper();

  @Mock
  private FileHelper fileHelperMock;

  private Database inFileDatabase;

  @BeforeEach
  void setUp() throws Exception {
    inFileDatabase = new InFileDatabase(fileHelperMock, mapper);
  }

  @Test
  @DisplayName("Should return saved invoice when saveInvoice invoked.")
  void shouldReturnSavedInvoice() throws DatabaseOperationException, IOException {
    //given
    final Invoice expectedInvoice = getRandomInvoice();
    final String invoiceAsJson = mapper.writeValueAsString(expectedInvoice);
    doNothing().when(fileHelperMock).writeLine(invoiceAsJson);

    //when
    Invoice actualInvoice = inFileDatabase.saveInvoice(expectedInvoice);

    //then
    assertEquals(expectedInvoice, actualInvoice);
    verify(fileHelperMock).writeLine(invoiceAsJson);
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when save invoice is invoked and fileHelper throws exception.")
  void saveInvoiceShouldThrowExceptionWhenFileHelperThrowsException() throws IOException {
    //given
    final Invoice invoice = getRandomInvoice();
    final String invoiceAsJson = mapper.writeValueAsString(invoice);
    doThrow(IOException.class).when(fileHelperMock).writeLine(invoiceAsJson);

    //then
    assertThrows(DatabaseOperationException.class, () -> inFileDatabase.saveInvoice(invoice));
  }

  @Test
  @DisplayName("Should return invoice with specified id when findOneInvoice is invoked.")
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
    Invoice actualInvoice = inFileDatabase.findOneInvoice(invoice2.getId());

    //then
    assertEquals(invoice2, actualInvoice);
  }

  @Test
  @DisplayName("Should return null when findOneInvoice is invoked and invoice does not exist.")
  void findOneInvoiceShouldReturnNullWhenInvoiceDoesNotExist() throws DatabaseOperationException, IOException {
    //given
    final String invoiceAsJson = mapper.writeValueAsString(getRandomInvoice());
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList(invoiceAsJson));

    //when
    Invoice actualInvoice = inFileDatabase.findOneInvoice("FV/XXXX/XX/XXXX");

    //then
    assertNull(actualInvoice);
  }

  @Test
  @DisplayName("Should return null when findOneInvoice is invoked and input stream is empty.")
  void findOneInvoiceShouldReturnNullWhenInputStreamIsEmpty() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.emptyList());

    //when
    Invoice actualInvoice = inFileDatabase.findOneInvoice("FV/XXXX/XX/XXXX");

    //then
    assertNull(actualInvoice);
  }

  @Test
  @DisplayName("Should return null when findOneInvoice is invoked and input stream contains invalid data.")
  void findOneInvoiceShouldReturnNullWhenInputStreamContainsInvalidData() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    Invoice actualInvoice = inFileDatabase.findOneInvoice("FV/XXXX/XX/XXXX");

    //then
    assertNull(actualInvoice);
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when findOneInvoice is invoked and fileHelper throws exception.")
  void findOneInvoiceShouldThrowExceptionWhenFileHelperThrowsException() throws IOException {
    //given
    doThrow(IOException.class).when(fileHelperMock).readLines();

    //then
    assertThrows(DatabaseOperationException.class, () -> inFileDatabase.findOneInvoice(getRandomInvoice().getId()));
  }

  @Test
  @DisplayName("Should return all invoices from inFileDatabase when findAllInvoices is invoked.")
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
    List<Invoice> actualInvoices = inFileDatabase.findAllInvoices();

    //then
    assertEquals(Arrays.asList(invoice1, invoice2, invoice3), actualInvoices);
  }

  @Test
  @DisplayName("Should return empty list when findAllInvoices is invoked and input stream is empty.")
  void findAllInvoicesShouldReturnEmptyListWhenInputStreamIsEmpty() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.emptyList());

    //when
    List<Invoice> actualInvoices = inFileDatabase.findAllInvoices();

    //then
    assertEquals(new ArrayList<>(), actualInvoices);
  }

  @Test
  @DisplayName("Should return empty list when findAllInvoices is invoked and input stream contains invalid data.")
  void findAllInvoicesShouldReturnEmptyListWhenInputStreamContainsInvalidData() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    List<Invoice> actualInvoices = inFileDatabase.findAllInvoices();

    //then
    assertEquals(new ArrayList<>(), actualInvoices);
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when findAllInvoices is invoked and fileHelper.readLines throws exception.")
  void findAllInvoicesShouldThrowExceptionWhenFileHelperReadLinesThrowsException() throws IOException {
    //given
    doThrow(IOException.class).when(fileHelperMock).readLines();

    //then
    assertThrows(DatabaseOperationException.class, () -> inFileDatabase.findAllInvoices());
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
    List<Invoice> actualInvoices = inFileDatabase.findAllInvoicesBySellerName(invoice1.getSeller().getName());

    //then
    assertEquals(Arrays.asList(invoice1, invoice2), actualInvoices);
  }

  @Test
  @DisplayName("Should return empty list when findAllInvoicesBySellerName is invoked and input stream is empty.")
  void findAllInvoicesBySellerNameShouldReturnEmptyListWhenInputStreamEmpty() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.emptyList());

    //when
    List<Invoice> actualInvoices = inFileDatabase.findAllInvoicesBySellerName(getRandomInvoice().getSeller().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoices);
  }

  @Test
  @DisplayName("Should return empty list when findAllInvoicesBySellerName is invoked and input stream contains invalid data.")
  void findAllInvoicesBySellerNameShouldReturnEmptyListWhenInputStreamContainsInvalidData() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    List<Invoice> actualInvoices = inFileDatabase.findAllInvoices();

    //then
    assertEquals(new ArrayList<>(), actualInvoices);
  }

  @Test
  @DisplayName("Should empty list when findAllInvoicesBySellerName is invoked and requested seller is not present in inFileDatabase.")
  void findAllInvoicesBySellerNameShouldReturnEmptyListWhenSellerMissing() throws IOException, DatabaseOperationException {
    //given
    final Invoice invoice1 = getRandomInvoiceWithSpecificSellerName("Company One");
    final Invoice invoice2 = getRandomInvoiceWithSpecificSellerName("Company Two");
    final Invoice invoice3 = getRandomInvoiceWithSpecificSellerName("Company Three");
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    List<Invoice> actualInvoicesBySellerName = inFileDatabase.findAllInvoicesBySellerName("A.C.M.E. Incorporated");

    //then
    assertEquals(Collections.emptyList(), actualInvoicesBySellerName);
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when findAllInvoicesBySellerName is invoked and fileHelper.readLines throws exception.")
  void findAllInvoicesBySellerNameShouldThrowExceptionWhenFileHelperReadLinesThrowsException() throws IOException {
    //given
    Invoice invoice = getRandomInvoice();
    doThrow(IOException.class).when(fileHelperMock).readLines();

    //then
    assertThrows(DatabaseOperationException.class, () -> inFileDatabase.findAllInvoicesBySellerName(invoice.getSeller().getName()));
  }

  @Test
  @DisplayName("Should return all invoices associated with particular buyer name when findAllInvoicesByBuyerName is invoked.")
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
    List<Invoice> actualInvoices = inFileDatabase.findAllInvoicesByBuyerName(invoice1.getBuyer().getName());

    //then
    assertEquals(Arrays.asList(invoice1, invoice3), actualInvoices);
  }

  @Test
  @DisplayName("Should return empty list when findAllInvoicesByBuyerName is invoked and input stream is empty.")
  void shouldReturnEmptyListWhenFindAllInvoicesByBuyerNameInvokedAndInputStreamIsEmpty() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.emptyList());

    //when
    List<Invoice> actualInvoices = inFileDatabase.findAllInvoicesByBuyerName(getRandomInvoice().getBuyer().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoices);
  }

  @Test
  @DisplayName("Should return empty list when findAllInvoicesByBuyerName is invoked and input stream contains invalid data.")
  void findAllInvoicesByBuyerNameShouldReturnEmptyListWhenInputStreamContainsInvalidData() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    List<Invoice> actualInvoices = inFileDatabase.findAllInvoicesByBuyerName(getRandomInvoice().getBuyer().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoices);
  }

  @Test
  @DisplayName("Should return empty list when findAllInvoicesByBuyerName is invoked and specified buyer is missing.")
  void findAllInvoicesByBuyerNameShouldReturnEmptyListWhenBuyerMissing() throws IOException, DatabaseOperationException {
    //given
    final Invoice invoice1 = getRandomInvoiceWithSpecificBuyerName("Company One");
    final Invoice invoice2 = getRandomInvoiceWithSpecificBuyerName("Company Two");
    final Invoice invoice3 = getRandomInvoiceWithSpecificBuyerName("Company Three");
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    List<Invoice> actualInvoicesByBuyerName = inFileDatabase.findAllInvoicesByBuyerName("A.C.M.E. Incorporated");

    //then
    assertEquals(Collections.emptyList(), actualInvoicesByBuyerName);
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when findAllInvoicesByBuyerName is invoked and fileHelper.readLines throws exception.")
  void findAllInvoicesByBuyerNameShouldThrowExceptionWhenFileHelperReadLinesThrowsException() throws IOException {
    //given
    doThrow(IOException.class).when(fileHelperMock).readLines();

    //then
    assertThrows(DatabaseOperationException.class, () -> inFileDatabase.findAllInvoicesByBuyerName(getRandomInvoice().getBuyer().getName()));
  }

  @ParameterizedTest
  @DisplayName("Should return number of invoices in inFileDatabase.")
  @MethodSource("countInvoicesTestParameters")
  void shouldReturnInvoiceCount(List<String> invoices, Long expectedInvoiceCount) throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.isEmpty()).thenReturn(false);
    when(fileHelperMock.readLines()).thenReturn(invoices);

    //when
    Long actualInvoiceCount = inFileDatabase.countInvoices();

    //then
    assertEquals(expectedInvoiceCount, actualInvoiceCount);
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
  @DisplayName("Should return 0 when inFileDatabase is empty and countInvoices is invoked.")
  void countInvoicesShouldReturnZeroWhenDatabaseIsEmpty() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.isEmpty()).thenReturn(false);

    //when
    Long actualInvoiceCount = inFileDatabase.countInvoices();

    //then
    assertEquals(Long.valueOf(0), actualInvoiceCount);
  }

  @Test
  @DisplayName("Should return 0 when inFileDatabase contains invalid data and countInvoices is invoked.")
  void countInvoicesShouldReturnZeroWhenDatabaseContainsInvalidData() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    Long actualInvoiceCount = inFileDatabase.countInvoices();

    //then
    assertEquals(Long.valueOf(0), actualInvoiceCount);
  }

  @Test
  @DisplayName("Should return false when invoiceExists is invoked and input stream contains invalid data.")
  void invoiceExistsShouldReturnFalseWhenInputStreamContainsInvalidData() throws IOException, DatabaseOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    boolean result = inFileDatabase.invoiceExists("FV/XXXX/XX/XXXX");

    //then
    assertFalse(result);
  }

  @Test
  @DisplayName("Should return true when invoiceExists is invoked and given invoice is present in inFileDatabase.")
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
    boolean result = inFileDatabase.invoiceExists(invoice2.getId());

    //then
    assertTrue(result);
  }

  @Test
  @DisplayName("Should return false when invoiceExists is invoked given invoice is not present in inFileDatabase.")
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
    boolean result = inFileDatabase.invoiceExists("FV/XXXX/XX/XXXX");

    //then
    assertFalse(result);
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when invoiceExists invoked and fileHelper throws exception.")
  void shouldThrowExceptionWhenInvoiceExistsAndFileHelperThrowsException() throws IOException {
    //given
    doThrow(IOException.class).when(fileHelperMock).readLines();

    //then
    assertThrows(DatabaseOperationException.class, () -> inFileDatabase.invoiceExists(getRandomInvoice().getId()));
  }

  @Test
  @DisplayName("Should pass proper line number to fileHelper.removeLine when deleteInvoice with specified invoiceId is invoked.")
  void shouldPassProperLineNumberToFileHelperRemoveLineWhenDeleteInvoiceInvoked() throws Exception {
    //given
    final Invoice invoice1 = getRandomInvoice();
    final Invoice invoice2 = getRandomInvoice();
    final Invoice invoice3 = getRandomInvoice();
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));
    doNothing().when(fileHelperMock).removeLine(anyLong());

    //when
    inFileDatabase.deleteInvoice(invoice2.getId());

    //then
    verify(fileHelperMock).removeLine(2L);
  }

  @Test
  @DisplayName("Should not pass line number to fileHelper.removeLine when deleteInvoice is invoked and invoice with particular id does not exist.")
  void shouldNotPassLineNumberToFileHelperRemoveLineWhenDeleteInvoiceInvokedAndInvoiceDoesNotExist() throws Exception {
    //given
    final Invoice invoice1 = getRandomInvoice();
    final Invoice invoice2 = getRandomInvoice();
    final Invoice invoice3 = getRandomInvoice();
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    inFileDatabase.deleteInvoice("FV/XXXX/XX/XXXX");

    //then
    verify(fileHelperMock, times(0)).removeLine(anyLong());
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when deleteInvoice is invoked and fileHelper.removeLine throws exception.")
  void deleteInvoiceShouldThrowExceptionWhenFileHelperRemoveLineThrowsException() throws Exception {
    //given
    Invoice invoice = getRandomInvoice();
    String invoiceAsJson = mapper.writeValueAsString(invoice);
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList(invoiceAsJson));
    doThrow(Exception.class).when(fileHelperMock).removeLine(anyLong());

    //then
    assertThrows(DatabaseOperationException.class, () -> inFileDatabase.deleteInvoice(invoice.getId()));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to saveInvoice.")
  void shouldThrowExceptionWhenNullArgumentPassedToSaveInvoice() {
    assertThrows(IllegalArgumentException.class, () -> inFileDatabase.saveInvoice(null));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to findOneInvoice.")
  void shouldThrowExceptionWhenNullArgumentPassedToFindOneInvoice() {
    assertThrows(IllegalArgumentException.class, () -> inFileDatabase.findOneInvoice(null));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to findAllInvoicesBySellerName.")
  void shouldThrowExceptionWhenNullArgumentPassedToFindAllInvoicesBySellerName() {
    assertThrows(IllegalArgumentException.class, () -> inFileDatabase.findAllInvoicesBySellerName(null));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to findAllInvoicesByBuyerName.")
  void shouldThrowExceptionWhenNullArgumentPassedToFindAllInvoicesByBuyerName() {
    assertThrows(IllegalArgumentException.class, () -> inFileDatabase.findAllInvoicesByBuyerName(null));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to deleteInvoice.")
  void shouldThrowExceptionWhenNullArgumentPassedToDeleteInvoice() {
    assertThrows(IllegalArgumentException.class, () -> inFileDatabase.deleteInvoice(null));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to invoiceExists.")
  void shouldThrowExceptionWhenNullArgumentPassedToInvoiceExists() {
    assertThrows(IllegalArgumentException.class, () -> inFileDatabase.invoiceExists(null));
  }
}
