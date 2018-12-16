package pl.coderstrust.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.repository.RepositoryOperationException;
import pl.coderstrust.repository.invoice.InvoiceRepository;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {
  @Mock
  private InvoiceRepository repository;

  private InvoiceService invoiceService;

  @BeforeEach
  void setUp() {
    invoiceService = new InvoiceService(repository);
  }

  @Test
  void shouldReturnAllInvoices() throws RepositoryOperationException, InvoiceServiceOperationException {
    //given
    List<Invoice> invoices = InvoiceGenerator.getRandomInvoices();
    when(repository.findAll()).thenReturn(invoices);

    //when
    List<Invoice> actual = invoiceService.getAllInvoices();

    //then
    assertEquals(invoices, actual);
    verify(repository).findAll();
  }

  @Test
  void shouldReturnEmptyListWhenDatabaseIsEmpty() throws RepositoryOperationException, InvoiceServiceOperationException {
    //given
    when(repository.findAll()).thenReturn(new ArrayList<>());

    //when
    List<Invoice> actual = invoiceService.getAllInvoices();

    //then
    assertEquals(new ArrayList<>(), actual);
    verify(repository).findAll();
  }

  @Test
  void shouldReturnAllInvoicesInGivenDateRange() throws RepositoryOperationException, InvoiceServiceOperationException {
    //given
    LocalDate startDate = LocalDate.of(2018, 12, 3);
    LocalDate endDate = LocalDate.of(2018, 12, 5);

    List<Invoice> expected = InvoiceGenerator.getRandomInvoicesIssuedInSpecificDateRange(startDate, endDate);
    List<Invoice> invoices = new ArrayList<>();
    invoices.add(InvoiceGenerator.getRandomInvoiceWithSpecificIssueDate(LocalDate.of(2016, 11, 3)));
    invoices.addAll(expected);
    invoices.add(InvoiceGenerator.getRandomInvoiceWithSpecificIssueDate(LocalDate.of(2017, 12, 3)));
    when(repository.findAll()).thenReturn(invoices);

    //when
    Iterable<Invoice> actual = invoiceService.getAllInvoicesIssuedInGivenDateRange(startDate, endDate);

    //then
    assertEquals(expected, actual);
    verify(repository).findAll();
  }

  @Test
  void shouldReturnEmptyListWhenGetAllInvoicesInGivenDateRangeIsInvokedAndDatabaseIsEmpty() throws RepositoryOperationException,
      InvoiceServiceOperationException {
    //given
    LocalDate startDate = LocalDate.of(2018, 12, 3);
    LocalDate endDate = LocalDate.of(2018, 12, 5);

    when(repository.findAll()).thenReturn(new ArrayList<>());

    //when
    List<Invoice> actual = invoiceService.getAllInvoicesIssuedInGivenDateRange(startDate, endDate);

    //then
    assertEquals(new ArrayList<>(), actual);
    verify(repository).findAll();
  }

  @Test
  void shouldAddInvoice() throws RepositoryOperationException, InvoiceServiceOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(repository.save(invoice)).thenReturn(invoice);

    //when
    Invoice actual = invoiceService.addInvoice(invoice);

    //then
    assertEquals(invoice, actual);
    verify(repository).save(invoice);
  }

  @Test
  void shouldDeleteInvoice() throws RepositoryOperationException, InvoiceServiceOperationException {
    //given
    int id = 3448;
    doNothing().when(repository).deleteById(id);

    //when
    invoiceService.deleteInvoice(id);

    //then
    verify(repository).deleteById(id);
  }

  @Test
  void shouldDeleteAllInvoices() throws RepositoryOperationException, InvoiceServiceOperationException {
    //given
    doNothing().when(repository).deleteAll();

    //when
    invoiceService.deleteAllInvoices();

    //then
    verify(repository).deleteAll();
  }

  @Test
  void shouldReturnInvoice() throws InvoiceServiceOperationException, RepositoryOperationException {
    //given
    Optional<Invoice> invoice = Optional.of(InvoiceGenerator.getRandomInvoice());
    int id = invoice.get().getId();
    when(repository.findById(id)).thenReturn(invoice);

    //when
    Optional<Invoice> actual = invoiceService.getInvoice(id);

    //then
    assertEquals(invoice, actual);
    verify(repository).findById(id);
  }

  @Test
  void shouldUpdateInvoice() throws RepositoryOperationException, InvoiceServiceOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    int id = invoice.getId();
    when(repository.existsById(id)).thenReturn(true);
    when(repository.save(invoice)).thenReturn(invoice);

    //when
    invoiceService.updateInvoice(invoice);

    //then
    verify(repository).save(invoice);
    verify(repository).existsById(id);
  }

  @Test
  void shouldThrowExceptionWhenGettingAllInvoicesWentWrong() throws RepositoryOperationException {
    //given
    doThrow(RepositoryOperationException.class).when(repository).findAll();

    //then
    assertThrows(InvoiceServiceOperationException.class, () -> invoiceService.getAllInvoices());
    verify(repository).findAll();
  }

  @Test
  void shouldThrowExceptionWhenGetInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.getInvoice(Integer.parseInt((null))));
  }

  @Test
  void shouldReturnEmptyOptionalIfInvoiceDoesNotExistInDatabase() throws RepositoryOperationException, InvoiceServiceOperationException {
    //given
    int id = -1;
    when(repository.findById(id)).thenReturn(Optional.empty());

    //then
    Optional<Invoice> actual = invoiceService.getInvoice(id);

    //then
    assertEquals(Optional.empty(), actual);
    verify(repository).findById(id);
  }

  @Test
  void shouldThrowExceptionWhenGettingInvoiceWentWrong() throws RepositoryOperationException {
    //given
    int id = 234;
    doThrow(RepositoryOperationException.class).when(repository).findById(id);

    //then
    assertThrows(InvoiceServiceOperationException.class, () -> invoiceService.getInvoice(id));
    verify(repository).findById(id);
  }

  @Test
  void shouldThrowExceptionWhenAddInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.addInvoice(null));
  }

  @Test
  void shouldThrowExceptionWhenAddingInvoiceWentWrong() throws RepositoryOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    doThrow(RepositoryOperationException.class).when(repository).save(invoice);

    //then
    assertThrows(InvoiceServiceOperationException.class, () -> invoiceService.addInvoice(invoice));
    verify(repository).save(invoice);
  }

  @Test
  void shouldThrowExceptionWhenUpdateInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.updateInvoice(null));
  }

  @Test
  void shouldThrowExceptionWhenUpdatingInvoiceWentWrong() throws RepositoryOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    int id = invoice.getId();
    when(repository.existsById(id)).thenReturn(true);
    doThrow(RepositoryOperationException.class).when(repository).save(invoice);

    //then
    assertThrows(InvoiceServiceOperationException.class, () -> invoiceService.updateInvoice(invoice));
    verify(repository).existsById(id);
    verify(repository).save(invoice);
  }

  @Test
  void shouldThrowExceptionIfInvoiceWithGivenIdDoesNotExist() throws RepositoryOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    int id = invoice.getId();
    when(repository.existsById(id)).thenReturn(false);

    //then
    assertThrows(InvoiceServiceOperationException.class, () -> invoiceService.updateInvoice(invoice));
    verify(repository).existsById(id);
  }

  @Test
  void shouldThrowExceptionWhenDeleteInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.deleteInvoice(Integer.parseInt(null)));
  }

  @Test
  void shouldThrowExceptionWhenDeleteInvoiceWentWrong() throws RepositoryOperationException {
    //given
    int id = -234;
    doThrow(RepositoryOperationException.class).when(repository).deleteById(id);

    //then
    assertThrows(InvoiceServiceOperationException.class, () -> invoiceService.deleteInvoice(id));
    verify(repository).deleteById(id);
  }

  @Test
  void shouldThrowExceptionWhenDeleteAllInvoicesWentWrong() throws RepositoryOperationException {
    //given
    doThrow(RepositoryOperationException.class).when(repository).deleteAll();

    //then
    assertThrows(InvoiceServiceOperationException.class, () -> invoiceService.deleteAllInvoices());
    verify(repository).deleteAll();
  }

  @Test
  void shouldThrowExceptionWhenGetAllInvoicesIssuedInGivenDateRangeInvokedWithNull() {
    //given
    LocalDate startDate = LocalDate.of(2019, 12, 1);
    LocalDate endDate = LocalDate.of(2019, 11, 1);

    //then
    assertThrows(IllegalArgumentException.class, () -> invoiceService.getAllInvoicesIssuedInGivenDateRange(null, endDate));
    assertThrows(IllegalArgumentException.class, () -> invoiceService.getAllInvoicesIssuedInGivenDateRange(startDate, null));
  }

  @Test
  void shouldThrowExceptionWhenGettingAllInvoicesIssuedInGivenDateRangeWentWrong() throws RepositoryOperationException {
    //given
    LocalDate startDate = LocalDate.of(2019, 12, 1);
    LocalDate endDate = LocalDate.of(2019, 12, 5);
    doThrow(RepositoryOperationException.class).when(repository).findAll();

    //then
    assertThrows(InvoiceServiceOperationException.class, () -> invoiceService.getAllInvoicesIssuedInGivenDateRange(startDate, endDate));
    verify(repository).findAll();
  }

  @Test
  void shouldThrowExceptionWhenGettingAllInvoicesIssuedInGivenDateRangeWithWrongDates() {
    //given
    LocalDate startDate = LocalDate.of(2019, 12, 1);
    LocalDate endDate = LocalDate.of(2019, 11, 1);

    //then
    assertThrows(IllegalArgumentException.class, () -> invoiceService.getAllInvoicesIssuedInGivenDateRange(startDate, endDate));
  }
}
