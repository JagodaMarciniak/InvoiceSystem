package pl.coderstrust.generators;

import static pl.coderstrust.model.InvoiceType.STANDARD;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

public class InvoiceGenerator {
  private static Random random = new Random();
  
  public static Invoice getRandomInvoice() {
    Invoice sampleInvoice = getSampleInvoice();
    String sampleInvoiceId = sampleInvoice.getId() + String.valueOf(random.nextInt(50));
    String sampleSellerName = sampleInvoice.getSeller().getName()
        + String.valueOf(random.nextInt(50));
    String sampleBuyerName = sampleInvoice.getBuyer().getName()
        + String.valueOf(random.nextInt(50));

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
    String sampleInvoiceId = "sampleID:";
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

  public static Invoice getRandomInvoiceWithSpecificId(String invoiceId) {
    Invoice randomInvoice = getRandomInvoice();

    return new Invoice(invoiceId, randomInvoice.getType(),
        randomInvoice.getIssueDate(), randomInvoice.getDueDate(),
        randomInvoice.getSeller(), randomInvoice.getBuyer(), randomInvoice.getEntries(),
        randomInvoice.getTotalNetValue(), randomInvoice.getTotalGrossValue(),
        randomInvoice.getComments());
  }
}
