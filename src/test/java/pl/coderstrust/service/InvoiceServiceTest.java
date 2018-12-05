package pl.coderstrust.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {
  @Mock
  private Database database;

  private InvoiceService invoiceService;

  @BeforeEach
  void setUp() {
    invoiceService = new InvoiceService(database);
  }

  @Test
  void shouldReturnAllInvoices() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    List<Invoice> invoices = InvoiceGenerator.getRandomInvoiceList();
    when(database.findAllInvoices()).thenReturn(invoices);

    //when
    List<Invoice> actual = invoiceService.getAllInvoices();

    //then
    assertSame(invoices, actual);
    verify(database, times(1)).findAllInvoices();
  }

  @Test
  void shouldReturnAllInvoicesInGivenDateRange() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    LocalDate startDate = LocalDate.of(2018, 12, 3);
    LocalDate endDate = LocalDate.of(2018, 12, 5);

    List<Invoice> invoices = InvoiceGenerator.getRandomInvoiceIssuedInSpecificDateRange(startDate, endDate);
    when(database.findAllInvoices()).thenReturn(invoices);

    //when
    List<Invoice> actual = invoiceService.getAllInvoicesInGivenDateRange(startDate, endDate);

    //then
    assertEquals(invoices, actual);
  }

  @Test
  void shouldReturnEmptyListOfInvoicesWhenDatabaseIsEmpty() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    LocalDate startDate = LocalDate.of(2018, 12, 3);
    LocalDate endDate = LocalDate.of(2018, 12, 5);

    List<Invoice> emptyList = new ArrayList<>();
    when(database.findAllInvoices()).thenReturn(emptyList);

    //when
    List<Invoice> actual = invoiceService.getAllInvoicesInGivenDateRange(startDate, endDate);

    //then
    assertEquals(emptyList, actual);
  }

  @Test
  void shouldReturnEmptyListOfInvoicesWhenThereAreNoInvoicesInGivenDateRange() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    LocalDate startDate = LocalDate.of(2018, 12, 3);
    LocalDate endDate = LocalDate.of(2018, 12, 5);
    List<Invoice> emptyList = new ArrayList<>();
    when(database.findAllInvoices()).thenReturn(emptyList);

    //when
    List<Invoice> actual = invoiceService.getAllInvoicesInGivenDateRange(startDate, endDate);

    //then
    assertEquals(emptyList, actual);
  }

  @Test
  void shouldAddingInvoice() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    Invoice randomInvoice = InvoiceGenerator.getRandomInvoice();
    when(database.saveInvoice(randomInvoice)).thenReturn(randomInvoice);

    //when
    Invoice actual = invoiceService.addInvoice(randomInvoice);

    //then
    assertSame(randomInvoice, actual);
  }

  @Test
  void shouldDeleteInvoice() throws DatabaseOperationException {
    //given
    Invoice randomInvoice = InvoiceGenerator.getRandomInvoice();
    String id = randomInvoice.getId();
    doNothing().when(database).deleteInvoice(id);

    //when
    database.deleteInvoice(id);

    //then
    verify(database).deleteInvoice(id);
  }

  @Test
  void shouldReturnSingleInvoiceById() throws InvoiceBookOperationException, DatabaseOperationException {
    //given
    Invoice randomInvoice = InvoiceGenerator.getRandomInvoice();
    String id = randomInvoice.getId();
    when(database.findOneInvoice(id)).thenReturn(randomInvoice);

    //when
    Invoice actual = invoiceService.getSingleInvoiceById(id);

    //then
    assertSame(randomInvoice, actual);
  }

  @Test
  void shouldUpdateInvoice() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    Invoice randomInvoice = InvoiceGenerator.getRandomInvoice();
    when(database.saveInvoice(randomInvoice)).thenReturn(randomInvoice);

    //when
    invoiceService.updateInvoice(randomInvoice);

    //then
    verify(database).saveInvoice(randomInvoice);
  }

  @Test
  public void shouldThrowExceptionWhenGettingAllInvoicesWentWrong() throws DatabaseOperationException {
    //given
    doThrow(DatabaseOperationException.class).when(database).findAllInvoices();

    //then
    assertThrows(InvoiceBookOperationException.class, () -> invoiceService.getAllInvoices());
    verify(database).findAllInvoices();
  }

  @Test
  public void shouldThrowExceptionWhenGetSingleInvoiceByIdInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.getSingleInvoiceById(null));
  }

  @Test
  public void shouldThrowExceptionWhenGettingSingleInvoiceByIdWentWrong() throws DatabaseOperationException {
    //given
    Invoice randomInvoice = InvoiceGenerator.getRandomInvoice();
    String id = randomInvoice.getId();
    doThrow(DatabaseOperationException.class).when(database).findOneInvoice(id);

    //then
    assertThrows(InvoiceBookOperationException.class, () -> invoiceService.getSingleInvoiceById(id));
    verify(database).findOneInvoice(id);
  }

  @Test
  public void shouldThrowExceptionWhenAddInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.addInvoice(null));
  }

  @Test
  public void shouldThrowExceptionWhenAddingInvoiceWentWrong() throws DatabaseOperationException {
    //given
    Invoice randomInvoice = InvoiceGenerator.getRandomInvoice();
    doThrow(DatabaseOperationException.class).when(database).saveInvoice(randomInvoice);

    //then
    assertThrows(InvoiceBookOperationException.class, () -> invoiceService.addInvoice(randomInvoice));
    verify(database).saveInvoice(randomInvoice);
  }

  @Test
  public void shouldThrowExceptionWhenUpdateInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.updateInvoice(null));
  }

  @Test
  public void shouldThrowExceptionWhenUpdatingInvoiceWentWrong() throws DatabaseOperationException {
    //given
    Invoice randomInvoice = InvoiceGenerator.getRandomInvoice();
    doThrow(DatabaseOperationException.class).when(database).saveInvoice(randomInvoice);

    //then
    assertThrows(InvoiceBookOperationException.class, () -> invoiceService.updateInvoice(randomInvoice));
    verify(database).saveInvoice(randomInvoice);
  }

  @Test
  public void shouldThrowExceptionWhenDeleteInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.deleteInvoice(null));
  }

  @Test
  public void shouldThrowExceptionWhenDeleteInvoiceWentWrong() throws DatabaseOperationException {
    //given
    Invoice randomInvoice = InvoiceGenerator.getRandomInvoice();
    String id = randomInvoice.getId();
    doThrow(DatabaseOperationException.class).when(database).deleteInvoice(id);

    //then
    assertThrows(DatabaseOperationException.class, () -> database.deleteInvoice(id));
  }

  @Test
  public void shouldThrowExceptionWhenGetAllInvoicesInGivenDateRangeInvokedWithNull() {
    //given
    Invoice randomInvoice = InvoiceGenerator.getRandomInvoice();
    LocalDate startDate = randomInvoice.getIssueDate();
    LocalDate endDate = randomInvoice.getIssueDate();

    //then
    assertThrows(IllegalArgumentException.class, () -> invoiceService.getAllInvoicesInGivenDateRange(null, endDate));
    assertThrows(IllegalArgumentException.class, () -> invoiceService.getAllInvoicesInGivenDateRange(startDate, null));
  }

  @Test
  public void shouldThrowExceptionWhenGettingAllInvoicesInGivenDateRangeWentWrong() throws DatabaseOperationException {
    //given
    Invoice randomInvoice = InvoiceGenerator.getRandomInvoice();
    LocalDate startDate = randomInvoice.getIssueDate();
    LocalDate endDate = randomInvoice.getIssueDate();
    doThrow(DatabaseOperationException.class).when(database).findAllInvoices();

    //then
    assertThrows(InvoiceBookOperationException.class, () -> invoiceService.getAllInvoicesInGivenDateRange(startDate, endDate));
    verify(database).findAllInvoices();
  }

  @Test
  public void shouldThrowExceptionWhenGettingAllInvoicesInGivenDateRangeWithWrongDates() {
    //given
    LocalDate startDate = LocalDate.of(2019, 12, 01);
    LocalDate endDate = LocalDate.of(2019, 11, 01);

    //then
    assertThrows(IllegalArgumentException.class, () -> invoiceService.getAllInvoicesInGivenDateRange(startDate, endDate));
  }
}
