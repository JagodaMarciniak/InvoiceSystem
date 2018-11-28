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
    invoiceBook = new InvoiceBook(testDatabase);
  }

  @Test
  void shouldReturnAllInvoices() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    List<Invoice> invoices = InvoiceGenerator.getRandomInvoiceList();
    when(testDatabase.findAllInvoices()).thenReturn(invoices);

    //when
    List<Invoice> actual = invoiceBook.getAllInvoices();

    //then
    assertSame(invoices, actual);
    verify(testDatabase, times(1)).findAllInvoices();
  }

  @Test
  void shouldTestGettingAllInvoicesInGivenDateRange() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    LocalDate startDate = LocalDate.of(2018, 10, 24);
    LocalDate endDate = LocalDate.of(2019, 10, 24);
    List<Invoice> invoices = InvoiceGenerator.getInvoiceListWithSpecificDateRange(startDate, endDate);
    when(testDatabase.findAllInvoices()).thenReturn(invoices);

    //when
    List<Invoice> actual = invoiceBook.getAllInvoicesInGivenDateRange(startDate, endDate);

    //then
    assertEquals(invoices, actual);
  }

  @Test
  void shouldTestAddingInvoice() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    Invoice expected = testDatabase.findOneInvoice(invoiceId);
    when(testDatabase.saveInvoice(testInvoice)).thenReturn(expected);

    //when
    Invoice actual = invoiceBook.addInvoice(testInvoice);

    //then
    assertSame(expected, actual);
  }

  @Test
  void shouldTestInvoiceDeletion() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    doNothing().when(testDatabase).deleteInvoice(invoiceId);

    //when
    invoiceBook.deleteInvoice(invoiceId);

    //then
    verify(testDatabase).deleteInvoice(invoiceId);
  }

  @Test
  void shouldTestGettingSingleInvoiceById() throws InvoiceBookOperationException, DatabaseOperationException {
    //given
    when(testDatabase.findOneInvoice(invoiceId)).thenReturn(testInvoice);

    //when
    Invoice actual = invoiceBook.getSingleInvoiceById(invoiceId);

    //then
    assertSame(testInvoice, actual);
  }

  @Test
  void shouldTestInvoiceUpdating() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    when(testDatabase.saveInvoice(testInvoice)).thenReturn(testInvoice);

    //when
    invoiceBook.updateInvoice(testInvoice);

    //then
    verify(testDatabase).saveInvoice(testInvoice);
  }

  @Test
  public void shouldThrowExceptionWhenGettingAllInvoicesWentWrong() throws DatabaseOperationException {
    //given
    doThrow(DatabaseOperationException.class).when(testDatabase).findAllInvoices();

    //then
    assertThrows(InvoiceBookOperationException.class, () -> invoiceBook.getAllInvoices());
    verify(testDatabase).findAllInvoices();
  }

  @Test
  public void shouldThrowExceptionWhenGetSingleInvoiceByIdInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceBook.getSingleInvoiceById(null));
  }

  @Test
  public void shouldThrowExceptionWhenGettingSingleInvoiceByIdWentWrong() throws DatabaseOperationException {
    //given
    doThrow(DatabaseOperationException.class).when(testDatabase).findOneInvoice(invoiceId);

    //then
    assertThrows(InvoiceBookOperationException.class, () -> invoiceBook.getSingleInvoiceById(invoiceId));
    verify(testDatabase).findOneInvoice(invoiceId);
  }

  @Test
  public void shouldThrowExceptionWhenAddInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceBook.addInvoice(null));
  }

  @Test
  public void shouldThrowExceptionWhenAddingInvoiceWentWrong() throws DatabaseOperationException {
    //given
    doThrow(DatabaseOperationException.class).when(testDatabase).saveInvoice(testInvoice);

    //then
    assertThrows(InvoiceBookOperationException.class, () -> invoiceBook.addInvoice(testInvoice));
    verify(testDatabase).saveInvoice(testInvoice);
  }

  @Test
  public void shouldThrowExceptionWhenUpdateInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceBook.updateInvoice(null));
  }

  @Test
  public void shouldThrowExceptionWhenUpdatingInvoiceWentWrong() throws DatabaseOperationException {
    //given
    doThrow(DatabaseOperationException.class).when(testDatabase).saveInvoice(testInvoice);

    //then
    assertThrows(InvoiceBookOperationException.class, () -> invoiceBook.updateInvoice(testInvoice));
    verify(testDatabase).saveInvoice(testInvoice);
  }

  @Test
  public void shouldThrowExceptionWhenDeleteInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> invoiceBook.deleteInvoice(null));
  }

  @Test
  public void shouldThrowExceptionWhenDeleteInvoiceWentWrong() throws DatabaseOperationException {
    //given
    doThrow(DatabaseOperationException.class).when(testDatabase).deleteInvoice(invoiceId);

    //then
    assertThrows(InvoiceBookOperationException.class, () -> invoiceBook.deleteInvoice(invoiceId));
  }

  @Test
  public void shouldThrowExceptionWhenGetAllInvoicesInGivenDateRangeInvokedWithNull() {
    //given
    LocalDate startDate = testInvoice.getIssueDate();
    LocalDate endDate = testInvoice.getIssueDate();

    //then
    assertThrows(IllegalArgumentException.class, () -> invoiceBook.getAllInvoicesInGivenDateRange(null, endDate));
    assertThrows(IllegalArgumentException.class, () -> invoiceBook.getAllInvoicesInGivenDateRange(startDate, null));
  }

  @Test
  public void shouldThrowExceptionWhenGettingAllInvoicesInGivenDateRangeWentWrong() throws DatabaseOperationException {
    //given
    LocalDate startDate = testInvoice.getIssueDate();
    LocalDate endDate = testInvoice.getIssueDate();
    doThrow(DatabaseOperationException.class).when(testDatabase).findAllInvoices();

    //then
    assertThrows(InvoiceBookOperationException.class, () -> invoiceBook.getAllInvoicesInGivenDateRange(startDate, endDate));
    verify(testDatabase).findAllInvoices();
  }

  @Test
  public void shouldThrowExceptionWhenGivenDateRangeIsWrong() {
    //given
    LocalDate startDate = LocalDate.of(2019, 12, 01);
    LocalDate endDate = LocalDate.of(2019, 11, 01);

    //then
    assertThrows(IllegalArgumentException.class, () -> invoiceBook.getAllInvoicesInGivenDateRange(startDate, endDate));
  }
}
