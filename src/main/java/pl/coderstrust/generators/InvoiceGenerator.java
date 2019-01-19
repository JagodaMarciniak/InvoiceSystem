package pl.coderstrust.generators;

import static pl.coderstrust.model.InvoiceType.STANDARD;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

public class InvoiceGenerator {
  private static AtomicInteger atomicInteger = new AtomicInteger();

  public static Invoice getRandomInvoice() {
    Invoice sampleInvoice = getSampleInvoice();
    int newInvoiceIdAsNumber = Integer.parseInt(sampleInvoice.getId()) + atomicInteger.incrementAndGet();
    String newInvoiceNumber = "" + newInvoiceIdAsNumber;

    String sampleSellerName = sampleInvoice.getSeller().getName() + atomicInteger.incrementAndGet();
    String sampleBuyerName = sampleInvoice.getBuyer().getName() + atomicInteger.incrementAndGet();

    Company sampleSeller = new Company(sampleSellerName,
        sampleInvoice.getSeller().getTaxIdentificationNumber(),
        sampleInvoice.getSeller().getAccountNumber(),
        sampleInvoice.getSeller().getContactDetails());

    Company sampleBuyer = new Company(sampleBuyerName,
        sampleInvoice.getBuyer().getTaxIdentificationNumber(),
        sampleInvoice.getBuyer().getAccountNumber(),
        sampleInvoice.getBuyer().getContactDetails());

    return new Invoice(newInvoiceNumber, sampleInvoice.getType(),
        sampleInvoice.getIssueDate(), sampleInvoice.getDueDate(), sampleSeller,
        sampleBuyer, sampleInvoice.getEntries(), sampleInvoice.getTotalNetValue(),
        sampleInvoice.getTotalGrossValue(), sampleInvoice.getComments());
  }

  public static Invoice getRandomInvoiceWithNoId() {
    Invoice invoice = getRandomInvoice();
    return new Invoice(invoice.getType(), invoice.getIssueDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(),
        invoice.getEntries(), invoice.getTotalNetValue(), invoice.getTotalGrossValue(), invoice.getComments());
  }

  private static Invoice getSampleInvoice() {
    String sampleInvoiceId = "4";
    String sampleSellerName = "sampleSeller";
    String sampleBuyerName = "sampleBuyer";

    LocalDate sampleIssueDate = LocalDate.of(1, 1, 1);
    LocalDate sampleDueDate = LocalDate.of(1, 1, 1);

    BigDecimal sampleTotalNetValue = new BigDecimal(0);
    BigDecimal sampleTotalGrossValue = new BigDecimal(0);
    String sampleComments = "-";

    Company sampleSeller = CompanyGenerator.getSampleCompany(sampleSellerName);
    Company sampleBuyer = CompanyGenerator.getSampleCompany(sampleBuyerName);
    List<InvoiceEntry> sampleEntries = InvoiceEntriesGenerator.getSampleInvoiceEntries();

    return new Invoice(sampleInvoiceId, STANDARD, sampleIssueDate,
        sampleDueDate, sampleSeller, sampleBuyer, sampleEntries, sampleTotalNetValue,
        sampleTotalGrossValue, sampleComments);
  }

  public static Invoice getRandomInvoiceWithSpecificSellerName(String sellerName) {
    Invoice randomInvoice = getRandomInvoice();

    Company sampleSeller = new Company(sellerName,
        randomInvoice.getSeller().getTaxIdentificationNumber(),
        randomInvoice.getSeller().getAccountNumber(),
        randomInvoice.getSeller().getContactDetails());

    return new Invoice(randomInvoice.getId(), randomInvoice.getType(),
        randomInvoice.getIssueDate(), randomInvoice.getDueDate(), sampleSeller,
        randomInvoice.getBuyer(), randomInvoice.getEntries(),
        randomInvoice.getTotalNetValue(), randomInvoice.getTotalGrossValue(),
        randomInvoice.getComments());
  }

  public static Invoice getRandomInvoiceWithNoIdAndSpecificSellerName(String sellerName) {
    Invoice invoice = getRandomInvoiceWithNoId();

    Company seller = new Company(sellerName,
        invoice.getSeller().getTaxIdentificationNumber(),
        invoice.getSeller().getAccountNumber(),
        invoice.getSeller().getContactDetails());

    return new Invoice(invoice.getType(), invoice.getIssueDate(), invoice.getDueDate(), seller, invoice.getBuyer(), invoice.getEntries(),
        invoice.getTotalNetValue(), invoice.getTotalGrossValue(), invoice.getComments());
  }

  public static Invoice getRandomInvoiceWithSpecificBuyerName(String buyerName) {
    Invoice randomInvoice = getRandomInvoice();

    Company sampleBuyer = new Company(buyerName,
        randomInvoice.getSeller().getTaxIdentificationNumber(),
        randomInvoice.getSeller().getAccountNumber(),
        randomInvoice.getSeller().getContactDetails());

    return new Invoice(randomInvoice.getId(), randomInvoice.getType(),
        randomInvoice.getIssueDate(), randomInvoice.getDueDate(),
        randomInvoice.getSeller(), sampleBuyer, randomInvoice.getEntries(),
        randomInvoice.getTotalNetValue(), randomInvoice.getTotalGrossValue(),
        randomInvoice.getComments());
  }

  public static Invoice getRandomInvoiceWithNoIdAndSpecificBuyerName(String buyerName) {
    Invoice invoice = getRandomInvoiceWithNoId();

    Company buyer = new Company(buyerName,
        invoice.getSeller().getTaxIdentificationNumber(),
        invoice.getSeller().getAccountNumber(),
        invoice.getSeller().getContactDetails());

    return new Invoice(invoice.getType(), invoice.getIssueDate(), invoice.getDueDate(), invoice.getSeller(), buyer, invoice.getEntries(),
        invoice.getTotalNetValue(), invoice.getTotalGrossValue(), invoice.getComments());
  }

  public static Invoice getRandomInvoiceWithSpecificId(String invoiceId) {
    Invoice randomInvoice = getRandomInvoice();

    return new Invoice(invoiceId, randomInvoice.getType(),
        randomInvoice.getIssueDate(), randomInvoice.getDueDate(),
        randomInvoice.getSeller(), randomInvoice.getBuyer(), randomInvoice.getEntries(),
        randomInvoice.getTotalNetValue(), randomInvoice.getTotalGrossValue(),
        randomInvoice.getComments());
  }

  public static Invoice getRandomInvoiceWithSpecificIssueDate(LocalDate issueDate) {
    Invoice randomInvoice = getRandomInvoice();

    return new Invoice(randomInvoice.getId(), randomInvoice.getType(),
        issueDate, randomInvoice.getDueDate(),
        randomInvoice.getSeller(), randomInvoice.getBuyer(), randomInvoice.getEntries(),
        randomInvoice.getTotalNetValue(), randomInvoice.getTotalGrossValue(),
        randomInvoice.getComments());
  }

  public static Invoice copyInvoice(Invoice invoice) {
    return new Invoice(invoice.getId(), invoice.getType(), invoice.getIssueDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(),
        invoice.getEntries(), invoice.getTotalNetValue(), invoice.getTotalGrossValue(), invoice.getComments());
  }

  public static List<Invoice> getRandomInvoicesIssuedInSpecificDateRange(LocalDate startDate, LocalDate endDate) {
    List<Invoice> invoices = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      invoices.add(InvoiceGenerator.getRandomInvoiceWithSpecificIssueDate(startDate));
      invoices.add(InvoiceGenerator.getRandomInvoiceWithSpecificIssueDate(endDate));
    }
    return invoices;
  }

  public static List<Invoice> getRandomInvoices() {
    List<Invoice> invoices = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      invoices.add(InvoiceGenerator.getRandomInvoice());
    }
    return invoices;
  }
}
