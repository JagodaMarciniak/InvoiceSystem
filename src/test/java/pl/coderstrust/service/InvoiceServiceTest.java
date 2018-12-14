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
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.database.invoice.InvoiceDatabase;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;


@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {
  @Mock
  private InvoiceDatabase database;

  private InvoiceService invoiceService;

  @BeforeEach
  void setUp() {
    invoiceService = new InvoiceService(database);
  }

  @Test
  void shouldReturnAllInvoices() throws DatabaseOperationException, InvoiceServiceOperationException {
    //given
    List<Invoice> invoices = InvoiceGenerator.getRandomInvoices();
    when(database.findAll()).thenReturn(invoices);

    //when
    List<Invoice> actual = invoiceService.getAllInvoices();

    //then
    assertEquals(invoices, actual);
    verify(database).findAll();
  }

  @Test
  void shouldReturnEmptyListWhenDatabaseIsEmpty() throws DatabaseOperationException, InvoiceServiceOperationException {
    //given
    when(database.findAll()).thenReturn(new ArrayList<>());

    //when
    List<Invoice> actual = invoiceService.getAllInvoices();

    //then
    assertEquals(new ArrayList<>(), actual);
    verify(database).findAll();
  }

  @Test
  void shouldReturnAllInvoicesInGivenDateRange() throws DatabaseOperationException, InvoiceServiceOperationException {
    //given
    LocalDate startDate = LocalDate.of(2018, 12, 3);
    LocalDate endDate = LocalDate.of(2018, 12, 5);

    List<Invoice> expected = InvoiceGenerator.getRandomInvoicesIssuedInSpecificDateRange(startDate, endDate);
    List<Invoice> invoices = new ArrayList<>();
    invoices.add(InvoiceGenerator.getRandomInvoiceWithSpecificIssueDate(LocalDate.of(2016, 11, 3)));
    invoices.addAll(expected);
    invoices.add(InvoiceGenerator.getRandomInvoiceWithSpecificIssueDate(LocalDate.of(2017, 12, 3)));
    when(database.findAll()).thenReturn(invoices);

    //when
    Iterable<Invoice> actual = invoiceService.getAllInvoicesIssuedInGivenDateRange(startDate, endDate);

    //then
    assertEquals(expected, actual);
    verify(database).findAll();
  }

  @Test
  void shouldReturnEmptyListWhenGetAllInvoicesInGivenDateRangeIsInvokedAndDatabaseIsEmpty() throws DatabaseOperationException,
      InvoiceServiceOperationException {
    //given
    LocalDate startDate = LocalDate.of(2018, 12, 3);
    LocalDate endDate = LocalDate.of(2018, 12, 5);

    when(database.findAll()).thenReturn(new ArrayList<>());

    //when
    List<Invoice> actual = invoiceService.getAllInvoicesIssuedInGivenDateRange(startDate, endDate);

    //then
    assertEquals(new ArrayList<>(), actual);
    verify(database).findAll();
  }

  @Test
  void shouldAddInvoice() throws DatabaseOperationException, InvoiceServiceOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(database.save(invoice)).thenReturn(invoice);

    //when
    Invoice actual = invoiceService.addInvoice(invoice);

    //then
    assertEquals(invoice, actual);
    verify(database).save(invoice);
  }

  @Test
  void shouldDeleteInvoice() throws DatabaseOperationException, InvoiceServiceOperationException {
    //given
    String id = "3448";
    doNothing().when(database).deleteById(id);

    //when
    invoiceService.deleteInvoice(id);

    //then
    verify(database).deleteById(id);
  }

  @Test
  void shouldDeleteAllInvoices() throws DatabaseOperationException, InvoiceServiceOperationException {
    //given
    doNothing().when(database).deleteAll();

    //when
    invoiceService.deleteAllInvoices();

    //then
    verify(database).deleteAll();
  }

  @Test
  void shouldReturnInvoice() throws InvoiceServiceOperationException, DatabaseOperationException {
    //given
    Optional<Invoice> invoice = Optional.of(InvoiceGenerator.getRandomInvoice());
    String id = invoice.get().getId();
    when(database.findById(id)).thenReturn(invoice);

    //when
    Optional<Invoice> actual = invoiceService.getInvoice(id);

    //then
    assertEquals(invoice, actual);
    verify(database).findById(id);
  }

  @Test
  void shouldUpdateInvoice() throws DatabaseOperationException, InvoiceServiceOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    String id = invoice.getId();
    when(database.existsById(id)).thenReturn(true);
    when(database.save(invoice)).thenReturn(invoice);

    //when
    invoiceService.updateInvoice(invoice);

    //then
    verify(database).save(invoice);
    verify(database).existsById(id);
  }

  @Test
  void shouldThrowExceptionWhenGettingAllInvoicesWentWrong() throws DatabaseOperationException {
    //given
    doThrow(DatabaseOperationException.class).when(database).findAll();

    //then
    assertThrows(InvoiceServiceOperationException.class, () -> invoiceService.getAllInvoices());
    verify(database).findAll();
  }

  @Test
  void shouldThrowExceptionWhenGetInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.getInvoice(null));
  }

  @Test
  void shouldReturnEmptyOptionalIfInvoiceDoesNotExistInDatabase() throws DatabaseOperationException, InvoiceServiceOperationException {
    //given
    String id = "-1";
    when(database.findById(id)).thenReturn(Optional.empty());

    //then
    Optional<Invoice> actual = invoiceService.getInvoice(id);

    //then
    assertEquals(Optional.empty(), actual);
    verify(database).findById(id);
  }

  @Test
  void shouldThrowExceptionWhenGettingInvoiceWentWrong() throws DatabaseOperationException {
    //given
    String id = "234";
    doThrow(DatabaseOperationException.class).when(database).findById(id);

    //then
    assertThrows(InvoiceServiceOperationException.class, () -> invoiceService.getInvoice(id));
    verify(database).findById(id);
  }

  @Test
  void shouldThrowExceptionWhenAddInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.addInvoice(null));
  }

  @Test
  void shouldThrowExceptionWhenAddingInvoiceWentWrong() throws DatabaseOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    doThrow(DatabaseOperationException.class).when(database).save(invoice);

    //then
    assertThrows(InvoiceServiceOperationException.class, () -> invoiceService.addInvoice(invoice));
    verify(database).save(invoice);
  }

  @Test
  void shouldThrowExceptionWhenUpdateInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.updateInvoice(null));
  }

  @Test
  void shouldThrowExceptionWhenUpdatingInvoiceWentWrong() throws DatabaseOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    String id = invoice.getId();
    when(database.existsById(id)).thenReturn(true);
    doThrow(DatabaseOperationException.class).when(database).save(invoice);

    //then
    assertThrows(InvoiceServiceOperationException.class, () -> invoiceService.updateInvoice(invoice));
    verify(database).existsById(id);
    verify(database).save(invoice);
  }

  @Test
  void shouldThrowExceptionIfInvoiceWithGivenIdDoesNotExist() throws DatabaseOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    String id = invoice.getId();
    when(database.existsById(id)).thenReturn(false);

    //then
    assertThrows(InvoiceServiceOperationException.class, () -> invoiceService.updateInvoice(invoice));
    verify(database).existsById(id);
  }

  @Test
  void shouldThrowExceptionWhenDeleteInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.deleteInvoice(null));
  }

  @Test
  void shouldThrowExceptionWhenDeleteInvoiceWentWrong() throws DatabaseOperationException {
    //given
    String id = "-234";
    doThrow(DatabaseOperationException.class).when(database).deleteById(id);

    //then
    assertThrows(InvoiceServiceOperationException.class, () -> invoiceService.deleteInvoice(id));
    verify(database).deleteById(id);
  }

  @Test
  void shouldThrowExceptionWhenDeleteAllInvoicesWentWrong() throws DatabaseOperationException {
    //given
    doThrow(DatabaseOperationException.class).when(database).deleteAll();

    //then
    assertThrows(InvoiceServiceOperationException.class, () -> invoiceService.deleteAllInvoices());
    verify(database).deleteAll();
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
  void shouldThrowExceptionWhenGettingAllInvoicesIssuedInGivenDateRangeWentWrong() throws DatabaseOperationException {
    //given
    LocalDate startDate = LocalDate.of(2019, 12, 1);
    LocalDate endDate = LocalDate.of(2019, 12, 5);
    doThrow(DatabaseOperationException.class).when(database).findAll();

    //then
    assertThrows(InvoiceServiceOperationException.class, () -> invoiceService.getAllInvoicesIssuedInGivenDateRange(startDate, endDate));
    verify(database).findAll();
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
