package pl.coderstrust.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
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
import pl.coderstrust.configuration.AppConfiguration;
import pl.coderstrust.helpers.FileHelper;
import pl.coderstrust.helpers.FileHelperException;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.repository.invoice.InFileInvoiceRepository;
import pl.coderstrust.repository.invoice.InvoiceRepository;

@ExtendWith(MockitoExtension.class)
class InFileInvoiceRepositoryTest {

  private static ObjectMapper mapper = new AppConfiguration().getObjectMapper();

  @Mock
  private FileHelper fileHelperMock;

  private InvoiceRepository inFileRepository;

  @BeforeEach
  void setUp() throws Exception {
    inFileRepository = new InFileInvoiceRepository(fileHelperMock, mapper);
  }

  @Test
  @DisplayName("Should return saved invoice when save invoked.")
  void shouldReturnSavedInvoice() throws RepositoryOperationException, IOException {
    //given
    final Invoice expectedInvoice = getRandomInvoice();
    final String invoiceAsJson = mapper.writeValueAsString(expectedInvoice);
    doNothing().when(fileHelperMock).writeLine(invoiceAsJson);

    //when
    Invoice actualInvoice = inFileRepository.save(expectedInvoice);

    //then
    assertEquals(expectedInvoice, actualInvoice);
    verify(fileHelperMock).writeLine(invoiceAsJson);
  }

  @Test
  @DisplayName("Should throw DatabaseOperationException when save is invoked and fileHelper throws exception.")
  void saveShouldThrowExceptionWhenFileHelperThrowsException() throws IOException {
    //given
    final Invoice invoice = getRandomInvoice();
    final String invoiceAsJson = mapper.writeValueAsString(invoice);
    doThrow(IOException.class).when(fileHelperMock).writeLine(invoiceAsJson);

    //then
    assertThrows(RepositoryOperationException.class, () -> inFileRepository.save(invoice));
    verify(fileHelperMock).writeLine(invoiceAsJson);
  }

  @Test
  @DisplayName("Should return invoice with specified id when findById is invoked.")
  void shouldReturnInvoiceWithSpecifiedId() throws RepositoryOperationException, IOException {
    //given
    final Invoice invoice1 = getRandomInvoice();
    final Invoice invoice2 = getRandomInvoice();
    final Invoice invoice3 = getRandomInvoice();
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    Optional<Invoice> actualInvoice = inFileRepository.findById(invoice2.getId());

    //then
    assertEquals(Optional.of(invoice2), actualInvoice);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty optional when findById is invoked and invoice does not exist.")
  void findByIdShouldReturnEmptyOptionalWhenInvoiceDoesNotExist() throws RepositoryOperationException, IOException {
    //given
    final String invoiceAsJson = mapper.writeValueAsString(getRandomInvoice());
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList(invoiceAsJson));

    //when
    Optional<Invoice> actualInvoice = inFileRepository.findById(123456789);

    //then
    assertEquals(Optional.empty(), actualInvoice);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty optional when findById is invoked and input stream is empty.")
  void findByIdShouldReturnEmptyOptionalWhenInputStreamIsEmpty() throws IOException, RepositoryOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.emptyList());

    //when
    Optional<Invoice> actualInvoice = inFileRepository.findById(123456789);

    //then
    assertEquals(Optional.empty(), actualInvoice);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty optional when findById is invoked and input stream contains invalid data.")
  void findByIdShouldReturnEmptyOptionalWhenInputStreamContainsInvalidData() throws IOException, RepositoryOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    Optional<Invoice> actualInvoice = inFileRepository.findById(123456789);

    //then
    assertEquals(Optional.empty(), actualInvoice);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should throw RepositoryOperationException when findById is invoked and fileHelper throws exception.")
  void findByIdShouldThrowExceptionWhenFileHelperThrowsException() throws IOException {
    //given
    doThrow(IOException.class).when(fileHelperMock).readLines();

    //then
    assertThrows(RepositoryOperationException.class, () -> inFileRepository.findById(getRandomInvoice().getId()));
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return all invoices from inFileRepository when findAll is invoked.")
  void shouldReturnAllInvoices() throws RepositoryOperationException, IOException {
    //given
    final Invoice invoice1 = getRandomInvoice();
    final Invoice invoice2 = getRandomInvoice();
    final Invoice invoice3 = getRandomInvoice();
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    Iterable<Invoice> actualInvoices = inFileRepository.findAll();

    //then
    assertEquals(Arrays.asList(invoice1, invoice2, invoice3), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty list when findAll is invoked and input stream is empty.")
  void findAllShouldReturnEmptyListWhenInputStreamIsEmpty() throws IOException, RepositoryOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.emptyList());

    //when
    Iterable<Invoice> actualInvoices = inFileRepository.findAll();

    //then
    assertEquals(new ArrayList<>(), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty list when findAll is invoked and input stream contains invalid data.")
  void findAllShouldReturnEmptyListWhenInputStreamContainsInvalidData() throws IOException, RepositoryOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    Iterable<Invoice> actualInvoices = inFileRepository.findAll();

    //then
    assertEquals(new ArrayList<>(), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should throw RepositoryOperationException when findAll is invoked and fileHelper.readLines throws exception.")
  void findAllShouldThrowExceptionWhenFileHelperReadLinesThrowsException() throws IOException {
    //given
    doThrow(IOException.class).when(fileHelperMock).readLines();

    //then
    assertThrows(RepositoryOperationException.class, () -> inFileRepository.findAll());
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return all invoices associated with particular seller name.")
  void shouldReturnAllInvoicesBySellerName() throws RepositoryOperationException, IOException {
    //given
    final Invoice invoice1 = getRandomInvoiceWithSpecificSellerName("Company One");
    final Invoice invoice2 = getRandomInvoiceWithSpecificSellerName("Company One");
    final Invoice invoice3 = getRandomInvoiceWithSpecificSellerName("Company Two");
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    Iterable<Invoice> actualInvoices = inFileRepository.findAllBySellerName(invoice1.getSeller().getName());

    //then
    assertEquals(Arrays.asList(invoice1, invoice2), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty list when findAllBySellerName is invoked and input stream is empty.")
  void findAllBySellerNameShouldReturnEmptyListWhenInputStreamEmpty() throws IOException, RepositoryOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.emptyList());

    //when
    Iterable<Invoice> actualInvoices = inFileRepository.findAllBySellerName(getRandomInvoice().getSeller().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty list when findAllBySellerName is invoked and input stream contains invalid data.")
  void findAllInvoicesBySellerNameShouldReturnEmptyListWhenInputStreamContainsInvalidData() throws IOException, RepositoryOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    Iterable<Invoice> actualInvoices = inFileRepository.findAllBySellerName("Sample Company");

    //then
    assertEquals(new ArrayList<>(), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should empty list when findAllBySellerName is invoked and requested seller is not present in inFileRepository.")
  void findAllBySellerNameShouldReturnEmptyListWhenSellerMissing() throws IOException, RepositoryOperationException {
    //given
    final Invoice invoice1 = getRandomInvoiceWithSpecificSellerName("Company One");
    final Invoice invoice2 = getRandomInvoiceWithSpecificSellerName("Company Two");
    final Invoice invoice3 = getRandomInvoiceWithSpecificSellerName("Company Three");
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    Iterable<Invoice> actualInvoicesBySellerName = inFileRepository.findAllBySellerName("A.C.M.E. Incorporated");

    //then
    assertEquals(Collections.emptyList(), actualInvoicesBySellerName);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should throw RepositoryOperationException when findAllBySellerName is invoked and fileHelper.readLines throws exception.")
  void findAllBySellerNameShouldThrowExceptionWhenFileHelperReadLinesThrowsException() throws IOException {
    //given
    Invoice invoice = getRandomInvoice();
    doThrow(IOException.class).when(fileHelperMock).readLines();

    //then
    assertThrows(RepositoryOperationException.class, () -> inFileRepository.findAllBySellerName(invoice.getSeller().getName()));
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return all invoices associated with particular buyer name when findAllByBuyerName is invoked.")
  void shouldReturnAllInvoicesByBuyerName() throws RepositoryOperationException, IOException {
    //given
    final Invoice invoice1 = getRandomInvoiceWithSpecificBuyerName("Company One");
    final Invoice invoice2 = getRandomInvoiceWithSpecificBuyerName("Company Two");
    final Invoice invoice3 = getRandomInvoiceWithSpecificBuyerName("Company One");
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    Iterable<Invoice> actualInvoices = inFileRepository.findAllByBuyerName(invoice1.getBuyer().getName());

    //then
    assertEquals(Arrays.asList(invoice1, invoice3), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty list when findAllByBuyerName is invoked and input stream is empty.")
  void shouldReturnEmptyListWhenFindAllByBuyerNameInvokedAndInputStreamIsEmpty() throws IOException, RepositoryOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.emptyList());

    //when
    Iterable<Invoice> actualInvoices = inFileRepository.findAllByBuyerName(getRandomInvoice().getBuyer().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty list when findAllByBuyerName is invoked and input stream contains invalid data.")
  void findAllByBuyerNameShouldReturnEmptyListWhenInputStreamContainsInvalidData() throws IOException, RepositoryOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    Iterable<Invoice> actualInvoices = inFileRepository.findAllByBuyerName(getRandomInvoice().getBuyer().getName());

    //then
    assertEquals(Collections.emptyList(), actualInvoices);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return empty list when findAllByBuyerName is invoked and specified buyer is missing.")
  void findAllByBuyerNameShouldReturnEmptyListWhenBuyerMissing() throws IOException, RepositoryOperationException {
    //given
    final Invoice invoice1 = getRandomInvoiceWithSpecificBuyerName("Company One");
    final Invoice invoice2 = getRandomInvoiceWithSpecificBuyerName("Company Two");
    final Invoice invoice3 = getRandomInvoiceWithSpecificBuyerName("Company Three");
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    Iterable<Invoice> actualInvoicesByBuyerName = inFileRepository.findAllByBuyerName("A.C.M.E. Incorporated");

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
    assertThrows(RepositoryOperationException.class, () -> inFileRepository.findAllByBuyerName(getRandomInvoice().getBuyer().getName()));
    verify(fileHelperMock).readLines();
  }

  @ParameterizedTest
  @DisplayName("Should return number of invoices in inFileRepository.")
  @MethodSource("countInvoicesTestParameters")
  void shouldReturnInvoiceCount(List<String> invoices, Long expectedInvoiceCount) throws IOException, RepositoryOperationException {
    //given
    when(fileHelperMock.isEmpty()).thenReturn(false);
    when(fileHelperMock.readLines()).thenReturn(invoices);

    //when
    Long actualInvoiceCount = inFileRepository.count();

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
  @DisplayName("Should return 0 when inFileRepository is empty and count is invoked.")
  void countShouldReturnZeroWhenDatabaseIsEmpty() throws IOException, RepositoryOperationException {
    //given
    when(fileHelperMock.isEmpty()).thenReturn(false);

    //when
    Long actualInvoiceCount = inFileRepository.count();

    //then
    assertEquals(Long.valueOf(0), actualInvoiceCount);
    verify(fileHelperMock).isEmpty();
  }

  @Test
  @DisplayName("Should return 0 when inFileRepository contains invalid data and count is invoked.")
  void countShouldReturnZeroWhenDatabaseContainsInvalidData() throws IOException, RepositoryOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    Long actualInvoiceCount = inFileRepository.count();

    //then
    assertEquals(Long.valueOf(0), actualInvoiceCount);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return false when existsById is invoked and input stream contains invalid data.")
  void existsByIdShouldReturnFalseWhenInputStreamContainsInvalidData() throws IOException, RepositoryOperationException {
    //given
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList("xyz"));

    //when
    boolean result = inFileRepository.existsById(123456789);

    //then
    assertFalse(result);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return true when existsById is invoked and given invoice is present in inFileRepository.")
  void shouldReturnTrueWhenInvoiceExists() throws RepositoryOperationException, IOException {
    //given
    final Invoice invoice1 = getRandomInvoice();
    final Invoice invoice2 = getRandomInvoice();
    final Invoice invoice3 = getRandomInvoice();
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    boolean result = inFileRepository.existsById(invoice2.getId());

    //then
    assertTrue(result);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should return false when existsById is invoked and given invoice is not present in inFileRepository.")
  void shouldReturnFalseWhenInvoiceDoesNotExist() throws RepositoryOperationException, IOException {
    //given
    final Invoice invoice1 = getRandomInvoice();
    final Invoice invoice2 = getRandomInvoice();
    final Invoice invoice3 = getRandomInvoice();
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    boolean result = inFileRepository.existsById(123456789);

    //then
    assertFalse(result);
    verify(fileHelperMock).readLines();
  }

  @Test
  @DisplayName("Should throw RepositoryOperationException when existsById invoked and fileHelper throws exception.")
  void existsByIdShouldThrowExceptionWhenFileHelperReadLinesThrowsException() throws IOException {
    //given
    doThrow(IOException.class).when(fileHelperMock).readLines();

    //then
    assertThrows(RepositoryOperationException.class, () -> inFileRepository.existsById(getRandomInvoice().getId()));
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
    inFileRepository.deleteById(invoice2.getId());

    //then
    verify(fileHelperMock).removeLine(2);
  }

  @Test
  @DisplayName("Should not pass line number to fileHelper.removeLine when deleteById is invoked and invoice with particular id does not exist.")
  void shouldNotPassLineNumberToFileHelperRemoveLineWhenDeleteByIdInvokedAndInvoiceDoesNotExist() throws Exception {
    //given
    final Invoice invoice1 = getRandomInvoice();
    final Invoice invoice2 = getRandomInvoice();
    final Invoice invoice3 = getRandomInvoice();
    final String invoice1AsJson = mapper.writeValueAsString(invoice1);
    final String invoice2AsJson = mapper.writeValueAsString(invoice2);
    final String invoice3AsJson = mapper.writeValueAsString(invoice3);
    when(fileHelperMock.readLines()).thenReturn(Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson));

    //when
    inFileRepository.deleteById(123456789);

    //then
    verify(fileHelperMock, times(0)).removeLine(anyInt());
  }

  @Test
  @DisplayName("Should throw RepositoryOperationException when deleteById is invoked and fileHelper.removeLine throws exception.")
  void deleteByIdShouldThrowExceptionWhenFileHelperRemoveLineThrowsException() throws Exception {
    //given
    Invoice invoice = getRandomInvoice();
    String invoiceAsJson = mapper.writeValueAsString(invoice);
    when(fileHelperMock.readLines()).thenReturn(Collections.singletonList(invoiceAsJson));
    doThrow(FileHelperException.class).when(fileHelperMock).removeLine(anyInt());

    //then
    assertThrows(RepositoryOperationException.class, () -> inFileRepository.deleteById(invoice.getId()));
    verify(fileHelperMock).readLines();
    verify(fileHelperMock).removeLine(anyInt());
  }

  @Test
  @DisplayName("Should throw RepositoryOperationException when deleteAll is invoked and fileHelper.clear throws exception.")
  void deleteAllShouldThrowExceptionWhenFileHelperClearThrowsException() throws IOException {
    //given
    doThrow(IOException.class).when(fileHelperMock).clear();

    //then
    assertThrows(RepositoryOperationException.class, () -> inFileRepository.deleteAll());
    verify(fileHelperMock).clear();
  }

  @Test
  @DisplayName("Should clear database file when deleteAll is invoked.")
  void deleteAllShouldClearDatabaseFile() throws IOException, RepositoryOperationException {
    //given
    doNothing().when(fileHelperMock).clear();

    //when
    inFileRepository.deleteAll();

    //then
    verify(fileHelperMock).clear();
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to save.")
  void shouldThrowExceptionWhenNullArgumentPassedToSave() {
    assertThrows(IllegalArgumentException.class, () -> inFileRepository.save(null));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to findById.")
  void shouldThrowExceptionWhenNullArgumentPassedToFindById() {
    assertThrows(IllegalArgumentException.class, () -> inFileRepository.findById(null));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to findAllBySellerName.")
  void shouldThrowExceptionWhenNullArgumentPassedToFindAllBySellerName() {
    assertThrows(IllegalArgumentException.class, () -> inFileRepository.findAllBySellerName(null));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to findAllByBuyerName.")
  void shouldThrowExceptionWhenNullArgumentPassedToFindAllByBuyerName() {
    assertThrows(IllegalArgumentException.class, () -> inFileRepository.findAllByBuyerName(null));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to deleteById.")
  void shouldThrowExceptionWhenNullArgumentPassedToDeleteById() {
    assertThrows(IllegalArgumentException.class, () -> inFileRepository.deleteById(null));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when null is passed as argument to existsById.")
  void shouldThrowExceptionWhenNullArgumentPassedToExistsById() {
    assertThrows(IllegalArgumentException.class, () -> inFileRepository.existsById(null));
  }
}
