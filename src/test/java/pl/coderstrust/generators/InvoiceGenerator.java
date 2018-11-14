package pl.coderstrust.generators;

import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static pl.coderstrust.model.InvoiceType.STANDARD;

public class InvoiceGenerator {
  private static AtomicInteger atomicInteger = new AtomicInteger();

  public static Invoice getRandomInvoice() {
    Invoice sampleInvoice = getSampleInvoice();
    int sampleInvoiceId = sampleInvoice.getId()
        + atomicInteger.incrementAndGet();
    String sampleSellerName = sampleInvoice.getSeller().getName()
        + String.valueOf(atomicInteger.incrementAndGet());
    String sampleBuyerName = sampleInvoice.getBuyer().getName()
        + String.valueOf(atomicInteger.incrementAndGet());

    Company sampleSeller = new Company(sampleSellerName,
        sampleInvoice.getSeller().getTaxIdentificationNumber(),
        sampleInvoice.getSeller().getAccountNumber(),
        sampleInvoice.getSeller().getContactDetails());

    Company sampleBuyer = new Company(sampleBuyerName,
        sampleInvoice.getBuyer().getTaxIdentificationNumber(),
        sampleInvoice.getBuyer().getAccountNumber(),
        sampleInvoice.getBuyer().getContactDetails());

    return new Invoice(sampleInvoiceId, sampleInvoice.getType(),
        sampleInvoice.getIssueDate(), sampleInvoice.getDueDate(), sampleSeller,
        sampleBuyer, sampleInvoice.getEntries(), sampleInvoice.getTotalNetValue(),
        sampleInvoice.getTotalGrossValue(), sampleInvoice.getComments());
  }

  private static Invoice getSampleInvoice() {
    int sampleInvoiceId = 4;
    String sampleSellerName = "sampleSeller";
    String sampleBuyerName = "sampleBuyer";

    LocalDate sampleIssueDate = LocalDate.of(0001, 01, 01);
    LocalDate sampleDueDate = LocalDate.of(0001, 01, 01);

    BigDecimal sampleTotalNetValue = new BigDecimal(0);
    BigDecimal sampleTotalGrossValue = new BigDecimal(0);
    String sampleComments = "-";

    Company sampleSeller = CompanyGenerator.getSampleCompany(sampleSellerName);
    Company sampleBuyer = CompanyGenerator.getSampleCompany(sampleBuyerName);
    List<InvoiceEntry> sampleEntries = InvoiceEntriesGenerator.getSampleInvoiceEntries();

    Invoice invoice = new Invoice(sampleInvoiceId, STANDARD, sampleIssueDate,
        sampleDueDate, sampleSeller, sampleBuyer, sampleEntries, sampleTotalNetValue,
        sampleTotalGrossValue, sampleComments);
    return
        invoice;
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

  public static Invoice getRandomInvoiceWithSpecificId(int invoiceId) {
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

  public static List<Invoice> getInvoiceListWithSpecificDateRange(LocalDate startDate, LocalDate
      endDate) {
    List<Invoice> invoiceList = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      invoiceList.add(InvoiceGenerator.getRandomInvoiceWithSpecificIssueDate(startDate));
      invoiceList.add(InvoiceGenerator.getRandomInvoiceWithSpecificIssueDate(endDate));
    }
    return invoiceList;
  }

  public static List<Invoice> getRandomInvoiceList() {
    List<Invoice> randomInvoiceList = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      randomInvoiceList.add(InvoiceGenerator.getRandomInvoice());
    }
    return randomInvoiceList;
  }
}
