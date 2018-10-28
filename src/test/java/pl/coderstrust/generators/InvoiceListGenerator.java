package pl.coderstrust.generators;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceType;

public class InvoiceListGenerator {
  public static List<Invoice> getSampleInvoiceList () {
    List<Invoice> invoiceList = new ArrayList<>();

    invoiceList.add(new Invoice("1234",InvoiceType.STANDARD,LocalDate.of(2018, 10, 24),LocalDate
        .of(2019,1,1),CompanyGenerator.getSampleCompany(),CompanyGenerator.getSampleCompany(),
        InvoiceEntriesGenerator.getSampleInvoiceEntries(), new BigDecimal(400), new BigDecimal
        (492),"payment deadline: 12/12/2018"));

    invoiceList.add(new Invoice("5678",InvoiceType.STANDARD,LocalDate.of(2018, 10, 24),LocalDate
        .of(2019,1,1),CompanyGenerator.getSampleCompany(),CompanyGenerator.getSampleCompany(),
        InvoiceEntriesGenerator.getSampleInvoiceEntries(), new BigDecimal(400), new BigDecimal
        (492),"payment deadline: 12/12/2018"));
    invoiceList.add(new Invoice("5678",InvoiceType.STANDARD,LocalDate.of(2018, 11, 27),LocalDate
        .of(2019,1,1),CompanyGenerator.getSampleCompany(),CompanyGenerator.getSampleCompany(),
        InvoiceEntriesGenerator.getSampleInvoiceEntries(), new BigDecimal(400), new BigDecimal
        (492),"payment deadline: 12/12/2018"));

    return invoiceList;
  }
}
