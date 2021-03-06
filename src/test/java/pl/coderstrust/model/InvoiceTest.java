package pl.coderstrust.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static pl.coderstrust.model.InvoiceType.STANDARD;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import pl.coderstrust.generators.CompanyGenerator;
import pl.coderstrust.generators.InvoiceEntriesGenerator;

class InvoiceTest {
  private final String id = "1";
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
  void checkFullInitializationWithAllArgumentsConstructor() {
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
  void checkFullInitializationWithRequiredArgumentsConstructor() {
    //when
    Invoice invoice = new Invoice(invoiceType, issueDate, dueDate, seller, buyer, entries,
        totalNetValue, totalGrossValue, comments);

    //then
    assertNull(invoice.getId());
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
}
