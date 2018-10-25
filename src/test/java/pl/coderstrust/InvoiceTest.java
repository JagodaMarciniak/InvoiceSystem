package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.coderstrust.InvoiceType.STANDARD;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class InvoiceTest {

  private String id = "FV/2018/10/234";
  private InvoiceType invoiceType = STANDARD;
  private LocalDate issueDate = LocalDate.of(2018, 10, 24);
  private LocalDate dueDate = LocalDate.of(2018, 11, 23);
  private Company seller = CompanyGenerator.getSampleCompany();
  private Company buyer = CompanyGenerator.getSampleCompany();
  private List<InvoiceEntry> entries = InvoiceEntriesGenerator.getSampleInvoiceEntries();
  private BigDecimal totalNetValue = new BigDecimal(300);
  private BigDecimal totalGrossValue = new BigDecimal(369);
  private String comments = "A few lines of comments";

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
    assertThrows(IllegalArgumentException.class, () -> new Invoice(id, null, issueDate,
        dueDate, seller, buyer, entries, totalNetValue, totalGrossValue, comments));
  }

  @Test
  public void shouldThrowExceptionWhenIssueDateIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice(id, invoiceType, null,
        dueDate, seller, buyer, entries, totalNetValue, totalGrossValue, comments));
  }

  @Test
  public void shouldThrowExceptionWhenDueDateIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice(id, invoiceType, issueDate,
        null, seller, buyer, entries, totalNetValue, totalGrossValue, comments));
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
