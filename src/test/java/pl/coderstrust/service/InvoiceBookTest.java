package pl.coderstrust.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.generators.CompanyGenerator;
import pl.coderstrust.generators.InvoiceEntriesGenerator;
import pl.coderstrust.generators.InvoiceListGenerator;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class InvoiceBookTest {
  private Invoice invoice = new Invoice("5678", InvoiceType.STANDARD, LocalDate.of(2018, 10, 24), LocalDate
      .of(2019, 1, 1), CompanyGenerator.getSampleCompany(), CompanyGenerator.getSampleCompany(),
      InvoiceEntriesGenerator.getSampleInvoiceEntries(), new BigDecimal(400), new BigDecimal
      (492), "payment deadline: 12/12/2018");
  @Mock
  private Database database;

  @InjectMocks
  private InvoiceBook invoiceBook;

  @Test
  void shouldTestGettingAllInvoices() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    List<Invoice> invoices = InvoiceListGenerator.getSampleInvoiceList();
    when(database.findAllInvoices()).thenReturn(invoices);

    //when
    List<Invoice> actual = invoiceBook.getAllInvoices();

    //then
    assertEquals(invoices, actual);
    verify(database, times(1)).findAllInvoices();
  }

  @Test
  void ShouldTestGettingAllInvoicesInGivenDateRange() throws
      DatabaseOperationException, InvoiceBookOperationException {

    //given
    LocalDate startDate = LocalDate.of(2018, 10, 24);
    LocalDate endDate = LocalDate.of(2019, 10, 24);
    List<Invoice> invoices = InvoiceListGenerator.getSampleInvoiceList();
    when(database.findAllInvoices()).thenReturn(invoices);

    //when
    List<Invoice> actual = invoiceBook.getAllInvoicesInGivenDateRange(startDate, endDate);

    //then
    assertEquals(invoices, actual);
  }

  @Test
  void shouldTestAddingNewInvoice() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    Invoice expected = database.findOneInvoice(invoice.getId());
    when(database.saveInvoice(invoice)).thenReturn(expected);

    //when
    Invoice actual = invoiceBook.addNewInvoice(invoice);

    //then
    assertEquals(expected, actual);
  }

  @Test
  void shouldTestInvoiceDeletion() throws DatabaseOperationException {
    //given
    doCallRealMethod().when(database).deleteInvoice(invoice);
    String id = invoice.getId();
    when(database.invoiceExists(id)).thenReturn(false);
    boolean isInvoiceExistAfterDeletion = database.invoiceExists(id);

    //then
    assertFalse(isInvoiceExistAfterDeletion);
    verify(database, times(1)).deleteInvoice(invoice);
  }

  @Test
  void shouldTestGettingSingleInvoiceById() throws InvoiceBookOperationException,
      DatabaseOperationException {
    //given
    String id = invoice.getId();
    when(database.findOneInvoice(id)).thenReturn(invoice);

    //when
    Invoice actual = invoiceBook.getSingleInvoiceById(id);

    //then
    assertEquals(invoice, actual);
  }

  @Test
  void shouldTestInvoiceUpdating() throws DatabaseOperationException, InvoiceBookOperationException {
    //given
    String id = invoice.getId();
    doNothing().when(database).deleteInvoice(invoice);
    Invoice expected = database.findOneInvoice(invoice.getId());
    when(database.saveInvoice(invoice)).thenReturn(expected);

    //when
    Invoice actual = invoiceBook.getSingleInvoiceById(id);

    //then
    verify(database, times(1)).deleteInvoice(invoice);
    assertEquals(expected, actual);
  }

  @Test
  public void shouldThrowExceptionWhen() {
    assertThrows(IllegalArgumentException.class, () -> invoiceBook.getSingleInvoiceById(null));
  }

  @Test
  public void shouldThrowExceptionWhen2() throws DatabaseOperationException {
    String invoiceId = "inv1";
    when(database.findOneInvoice(invoiceId)).thenThrow(new DatabaseOperationException("message"));

    assertThrows(InvoiceBookOperationException.class, () -> invoiceBook.getSingleInvoiceById(invoiceId));
    verify(database, times(1)).findOneInvoice(invoiceId);
  }
}
