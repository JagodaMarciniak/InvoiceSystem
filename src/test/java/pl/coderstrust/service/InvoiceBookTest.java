package pl.coderstrust.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import java.time.LocalDate;
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
class InvoiceBookTest {
  private final Invoice testInvoice = InvoiceGenerator.getRandomInvoice();
  private final String invoiceId = testInvoice.getId();

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
  void shouldTestGettingAllInvoicesInGivenDateRange() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    LocalDate startDate = LocalDate.of(2018, 10, 24);
    LocalDate endDate = LocalDate.of(2019, 10, 24);
    List<Invoice> invoices = InvoiceGenerator.getRandomInvoiceIssuedInSpecificDateRange(startDate, endDate);
    when(database.findAllInvoices()).thenReturn(invoices);

    //when
    List<Invoice> actual = invoiceService.getAllInvoicesInGivenDateRange(startDate, endDate);

    //then
    assertEquals(invoices, actual);
  }

  @Test
  void shouldTestAddingInvoice() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    Invoice expected = database.findOneInvoice(invoiceId);
    when(database.saveInvoice(testInvoice)).thenReturn(expected);

    //when
    Invoice actual = invoiceService.addInvoice(testInvoice);

    //then
    assertSame(expected, actual);
  }

  @Test
  void shouldTestInvoiceDeletion() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    doNothing().when(database).deleteInvoice(invoiceId);

    //when
    database.deleteInvoice(invoiceId);

    //then
    verify(database).deleteInvoice(invoiceId);
  }

  @Test
  void shouldTestGettingSingleInvoiceById() throws InvoiceBookOperationException, DatabaseOperationException {
    //given
    when(database.findOneInvoice(invoiceId)).thenReturn(testInvoice);

    //when
    Invoice actual = invoiceService.getSingleInvoiceById(invoiceId);

    //then
    assertSame(testInvoice, actual);
  }

  @Test
  void shouldTestInvoiceUpdating() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    when(database.saveInvoice(testInvoice)).thenReturn(testInvoice);

    //when
    invoiceService.updateInvoice(testInvoice);

    //then
    verify(database).saveInvoice(testInvoice);
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
    doThrow(DatabaseOperationException.class).when(database).findOneInvoice(invoiceId);

    //then
    assertThrows(InvoiceBookOperationException.class, () -> invoiceService.getSingleInvoiceById(invoiceId));
    verify(database).findOneInvoice(invoiceId);
  }

  @Test
  public void shouldThrowExceptionWhenAddInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.addInvoice(null));
  }

  @Test
  public void shouldThrowExceptionWhenAddingInvoiceWentWrong() throws DatabaseOperationException {
    //given
    doThrow(DatabaseOperationException.class).when(database).saveInvoice(testInvoice);

    //then
    assertThrows(InvoiceBookOperationException.class, () -> invoiceService.addInvoice(testInvoice));
    verify(database).saveInvoice(testInvoice);
  }

  @Test
  public void shouldThrowExceptionWhenUpdateInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.updateInvoice(null));
  }

  @Test
  public void shouldThrowExceptionWhenUpdatingInvoiceWentWrong() throws DatabaseOperationException {
    //given
    doThrow(DatabaseOperationException.class).when(database).saveInvoice(testInvoice);

    //then
    assertThrows(InvoiceBookOperationException.class, () -> invoiceService.updateInvoice(testInvoice));
    verify(database).saveInvoice(testInvoice);
  }

  @Test
  public void shouldThrowExceptionWhenDeleteInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceService.deleteInvoice(null));
  }

  @Test
  public void shouldThrowExceptionWhenDeleteInvoiceWentWrong() throws DatabaseOperationException {
    //given
    doThrow(DatabaseOperationException.class).when(database).deleteInvoice(invoiceId);

    //then
    assertThrows(InvoiceBookOperationException.class, () -> database.deleteInvoice(invoiceId));
  }

  @Test
  public void shouldThrowExceptionWhenGetAllInvoicesInGivenDateRangeInvokedWithNull() {
    //given
    LocalDate startDate = testInvoice.getIssueDate();
    LocalDate endDate = testInvoice.getIssueDate();

    //then
    assertThrows(IllegalArgumentException.class, () -> invoiceService.getAllInvoicesInGivenDateRange
        (null, endDate));
    assertThrows(IllegalArgumentException.class, () -> invoiceService.getAllInvoicesInGivenDateRange
        (startDate, null));
  }

  @Test
  public void shouldThrowExceptionWhenGettingAllInvoicesInGivenDateRangeWentWrong() throws DatabaseOperationException {
    //given
    LocalDate startDate = testInvoice.getIssueDate();
    LocalDate endDate = testInvoice.getIssueDate();
    doThrow(DatabaseOperationException.class).when(database).findAllInvoices();

    //then
    assertThrows(InvoiceBookOperationException.class, () -> invoiceService.getAllInvoicesInGivenDateRange(startDate, endDate));
    verify(database).findAllInvoices();
  }

  @Test
  public void shouldThrowExceptionWhenGivenDateRangeIsWrong() {
    //given
    LocalDate startDate = LocalDate.of(2019, 12, 01);
    LocalDate endDate = LocalDate.of(2019, 11, 01);

    //then
    assertThrows(IllegalArgumentException.class, () -> invoiceService.getAllInvoicesInGivenDateRange
        (startDate, endDate));
  }
}
