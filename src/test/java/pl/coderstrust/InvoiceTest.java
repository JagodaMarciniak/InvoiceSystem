package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.coderstrust.InvoiceType.STANDARD;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class InvoiceTest {

  @Test
  void checkFullInitialization() {
    //given
    String id = "FV/2018/10/234";
    InvoiceType invoiceType = STANDARD;
    LocalDate issueDate = LocalDate.of(2018, 10, 24);
    LocalDate dueDate = LocalDate.of(2018, 11, 23);
    Company seller = CompanyGenerator.getSampleCompany();
    Company buyer = CompanyGenerator.getSampleCompany();
    List<InvoiceEntry> entries = InvoiceEntriesGenerator.getSampleInvoiceEntries();
    BigDecimal totalNetValue = new BigDecimal(400);
    BigDecimal totalGrossValue = new BigDecimal(492);
    String comments = "A few lines of comments";

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
    assertThrows(IllegalArgumentException.class, () -> new Invoice(null, STANDARD,
        LocalDate.of(2018, 10, 24), LocalDate.of(2018, 11, 23),
        CompanyGenerator.getSampleCompany(), CompanyGenerator.getSampleCompany(),
        InvoiceEntriesGenerator.getSampleInvoiceEntries(), new BigDecimal(400), new BigDecimal(492),
        "A few lines of comments"));
  }

  @Test
  public void shouldThrowExceptionWhenInvoiceTypeIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice("FV/2018/10/234", null,
        LocalDate.of(2018, 10, 24), LocalDate.of(2018, 11, 23),
        CompanyGenerator.getSampleCompany(), CompanyGenerator.getSampleCompany(),
        InvoiceEntriesGenerator.getSampleInvoiceEntries(), new BigDecimal(400), new BigDecimal(492),
        "A few lines of comments"));
  }

  @Test
  public void shouldThrowExceptionWhenIssueDateIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice("FV/2018/10/234", STANDARD,
        null, LocalDate.of(2018, 11, 23), CompanyGenerator.getSampleCompany(),
        CompanyGenerator.getSampleCompany(), InvoiceEntriesGenerator.getSampleInvoiceEntries(),
        new BigDecimal(400), new BigDecimal(492), "A few lines of comments"));
  }

  @Test
  public void shouldThrowExceptionWhenDueDateIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice("FV/2018/10/234", STANDARD,
        LocalDate.of(2018, 10, 24), null, CompanyGenerator.getSampleCompany(),
        CompanyGenerator.getSampleCompany(), InvoiceEntriesGenerator.getSampleInvoiceEntries(),
        new BigDecimal(400), new BigDecimal(492), "A few lines of comments"));
  }

  @Test
  public void shouldThrowExceptionWhenSellerIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice("FV/2018/10/234", STANDARD,
        LocalDate.of(2018, 10, 24), LocalDate.of(2018, 11, 23), null,
        CompanyGenerator.getSampleCompany(), InvoiceEntriesGenerator.getSampleInvoiceEntries(),
        new BigDecimal(400), new BigDecimal(492), "A few lines of comments"));
  }

  @Test
  public void shouldThrowExceptionWhenBuyerIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice("FV/2018/10/234", STANDARD,
        LocalDate.of(2018, 10, 24), LocalDate.of(2018, 11, 23), CompanyGenerator.getSampleCompany(),
        null, InvoiceEntriesGenerator.getSampleInvoiceEntries(), new BigDecimal(400),
        new BigDecimal(492), "A few lines of comments"));
  }

  @Test
  public void shouldThrowExceptionWhenEntriesIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice("FV/2018/10/234", STANDARD,
        LocalDate.of(2018, 10, 24), LocalDate.of(2018, 11, 23), CompanyGenerator.getSampleCompany(),
        CompanyGenerator.getSampleCompany(), null, new BigDecimal(400), new BigDecimal(492),
        "A few lines of comments"));
  }

  @Test
  public void shouldThrowExceptionWhenTotalNetValueIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice("FV/2018/10/234", STANDARD,
        LocalDate.of(2018, 10, 24), LocalDate.of(2018, 11, 23), CompanyGenerator.getSampleCompany(),
        CompanyGenerator.getSampleCompany(), InvoiceEntriesGenerator.getSampleInvoiceEntries(),
        null, new BigDecimal(492), "A few lines of comments"));
  }

  @Test
  public void shouldThrowExceptionWhenTotalGrossValueIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Invoice("FV/2018/10/234", STANDARD,
        LocalDate.of(2018, 10, 24), LocalDate.of(2018, 11, 23), CompanyGenerator.getSampleCompany(),
        CompanyGenerator.getSampleCompany(), InvoiceEntriesGenerator.getSampleInvoiceEntries(),
        new BigDecimal(400), null, "A few lines of comments"));
  }
}
