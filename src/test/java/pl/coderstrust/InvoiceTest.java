package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.coderstrust.InvoiceType.STANDARD;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class InvoiceTest {
  private final String id = "FV/2018/10/234";
  private final InvoiceType invoiceType = STANDARD;
  private final LocalDate issueDate = LocalDate.of(2018, 10, 24);
  private final LocalDate dueDate = LocalDate.of(2018, 11, 23);
  private final Company seller = CompanyGenerator.getSampleCompany();
  private final Company buyer = CompanyGenerator.getSampleCompany();
  private final List<InvoiceEntry> entries = InvoiceEntriesGenerator.getSampleInvoiceEntries();
  private final BigDecimal totalNetValue = new BigDecimal(400);
  private final BigDecimal totalGrossValue = new BigDecimal(492);
  private final String comments = "A few lines of comments";

  @Test
  void checkFullInitialization() {
    //when
    Invoice invoice = new Invoice(id, invoiceType, issueDate, dueDate, seller, buyer, entries,
        totalNetValue, totalGrossValue, comments);

    //then
    assertEquals(id, invoice.getId());
    assertEquals(invoiceType, invoice.getType());
    assertEquals(issueDate, invoice.getIssueDate());
    assertEquals(dueDate, invoice.getDueDate());
    assertEquals(seller, invoice.getSeller());
    assertEquals(buyer, invoice.getBuyer());
    assertEquals(entries, invoice.getEntries());
    assertEquals(totalNetValue, invoice.getTotalNetValue());
    assertEquals(totalGrossValue, invoice.getTotalGrossValue());
    assertEquals(comments, invoice.getComments());
  }

  @Test
  public void shouldThrowExceptionWhenIdIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice(null, invoiceType, issueDate,
        dueDate, seller, buyer, entries, totalNetValue, totalGrossValue, comments));
  }

  @Test
  public void shouldThrowExceptionWhenInvoiceTypeIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice(id, null, issueDate, dueDate,
        seller, buyer, entries, totalNetValue, totalGrossValue, comments));
  }

  @Test
  public void shouldThrowExceptionWhenIssueDateIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice(id, invoiceType, null, dueDate,
        seller, buyer, entries, totalNetValue, totalGrossValue, comments));
  }

  @Test
  public void shouldThrowExceptionWhenDueDateIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice(id, invoiceType, issueDate, null,
        seller, buyer, entries, totalNetValue, totalGrossValue, comments));
  }

  @Test
  public void shouldThrowExceptionWhenSellerIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice(id, invoiceType, issueDate,
        dueDate, null, buyer, entries, totalNetValue, totalGrossValue, comments));
  }

  @Test
  public void shouldThrowExceptionWhenBuyerIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice(id, invoiceType, issueDate,
        dueDate, seller, null, entries, totalNetValue, totalGrossValue, comments));
  }

  @Test
  public void shouldThrowExceptionWhenEntriesIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice(id, invoiceType, issueDate,
        dueDate, seller, buyer, null, totalNetValue, totalGrossValue, comments));
  }

  @Test
  public void shouldThrowExceptionWhenTotalNetValueIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice(id, invoiceType, issueDate,
        dueDate, seller, buyer, entries, null, totalGrossValue, comments));
  }

  @Test
  public void shouldThrowExceptionWhenTotalGrossValueIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice(id, invoiceType, issueDate,
        dueDate, seller, buyer, entries, totalNetValue, null, comments));
  }
}
