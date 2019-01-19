package pl.coderstrust;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.InvoiceService;

//@SpringBootApplication
//public class Application {
//  public static void main(String[] args) {
//    SpringApplication.run(Application.class, args);
//  }
//}

@SpringBootApplication
public class Application implements CommandLineRunner {

  @Autowired
  InvoiceService invoiceService;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

//  @Override
//  public void run(String... args) throws Exception {
//    Invoice invoice = InvoiceGenerator.getRandomInvoiceWithNoId();
//    Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithNoId();
//    Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithNoId();
//    System.out.println("====================================================");
//    System.out.println("SAVED INVOICE: " + invoiceService.addInvoice(invoice));
//    System.out.println("INVOICES IN COLLECTION: " + invoiceService.getAllInvoices());
//    System.out.println("SAVED INVOICE: " + invoiceService.addInvoice(invoice2));
//    System.out.println("INVOICES IN COLLECTION: " + invoiceService.getAllInvoices());
//    System.out.println("INVOICE 1: " + invoiceService.getInvoice(invoice.getId()));
//    System.out.println("INVOICE 2 EXISTS: " + invoiceService.invoiceExists(invoice2.getId()));
//    System.out.println("RANDOM INVOICE EXISTS: " + invoiceService.invoiceExists("-1"));
//    System.out.println("INVOICE 2 UPDATE: ");
//    invoice.setComments("blah blah blah blah");
//    invoiceService.updateInvoice(invoice);
//    System.out.println("INVOICE 1 AFTER UPDATE: " + invoiceService.getInvoice(invoice.getId()));
//    System.out.println("INVOICES IN COLLECTION: " + invoiceService.getAllInvoices());
//    invoiceService.deleteInvoice(invoice2.getId());
//    System.out.println("AFTER DELETION OF INVOICE 2: " + invoiceService.getAllInvoices());
//    invoiceService.addInvoice(invoice3);
//    System.out.println("ADDED ONE MORE INVOICE: " + invoiceService.getAllInvoices());
//    invoiceService.deleteAllInvoices();
//    System.out.println("AFTER DELETION OF ALL INVOICES: " + invoiceService.getAllInvoices());
//    Invoice invoice4 = InvoiceGenerator.getRandomInvoiceWithNoId();
//    invoice4.setIssueDate(LocalDate.of(2016, 5, 1));
//    Invoice invoice5 = InvoiceGenerator.getRandomInvoiceWithNoId();
//    invoice5.setIssueDate(LocalDate.of(2017, 5, 1));
//    Invoice invoice6 = InvoiceGenerator.getRandomInvoiceWithNoId();
//    invoice6.setIssueDate(LocalDate.of(2016, 6, 1));
//    invoiceService.addInvoice(invoice4);
//    invoiceService.addInvoice(invoice5);
//    invoiceService.addInvoice(invoice6);
//    System.out.println("INVOICES IN COLLECTION: " + invoiceService.getAllInvoices());
//    System.out.println("Invoices Created in May 2016: " + invoiceService.getAllInvoicesIssuedInGivenDateRange(LocalDate.of(2016, 5, 1),
//        LocalDate.of(2016, 5, 30)));
//  }
//}

  @Override
  public void run(String... args) throws Exception {
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice3 = InvoiceGenerator.getRandomInvoice();
    System.out.println("====================================================");
    System.out.println("SAVED INVOICE: " + invoiceService.addInvoice(invoice));
    System.out.println("INVOICES IN COLLECTION: " + invoiceService.getAllInvoices());
    System.out.println("SAVED INVOICE: " + invoiceService.addInvoice(invoice2));
    System.out.println("INVOICES IN COLLECTION: " + invoiceService.getAllInvoices());
    System.out.println("INVOICE 1: " + invoiceService.getInvoice(invoice.getId()));
    System.out.println("INVOICE 2 EXISTS: " + invoiceService.invoiceExists(invoice2.getId()));
    System.out.println("RANDOM INVOICE EXISTS: " + invoiceService.invoiceExists("-1"));
    System.out.println("INVOICE 2 UPDATE: ");
    invoice.setComments("blah blah blah blah");
    invoiceService.updateInvoice(invoice);
    System.out.println("INVOICE 1 AFTER UPDATE: " + invoiceService.getInvoice(invoice.getId()));
    System.out.println("INVOICES IN COLLECTION: " + invoiceService.getAllInvoices());
    invoiceService.deleteInvoice(invoice2.getId());
    System.out.println("AFTER DELETION OF INVOICE 2: " + invoiceService.getAllInvoices());
    invoiceService.addInvoice(invoice3);
    System.out.println("ADDED ONE MORE INVOICE: " + invoiceService.getAllInvoices());
    invoiceService.deleteAllInvoices();
    System.out.println("AFTER DELETION OF ALL INVOICES: " + invoiceService.getAllInvoices());
    Invoice invoice4 = InvoiceGenerator.getRandomInvoice();
    invoice4.setIssueDate(LocalDate.of(2016, 5, 1));
    Invoice invoice5 = InvoiceGenerator.getRandomInvoice();
    invoice5.setIssueDate(LocalDate.of(2017, 5, 1));
    Invoice invoice6 = InvoiceGenerator.getRandomInvoice();
    invoice6.setIssueDate(LocalDate.of(2016, 6, 1));
    invoiceService.addInvoice(invoice4);
    invoiceService.addInvoice(invoice5);
    invoiceService.addInvoice(invoice6);
    System.out.println("INVOICES IN COLLECTION: " + invoiceService.getAllInvoices());
    System.out.println("Invoices Created in May 2016: " + invoiceService.getAllInvoicesIssuedInGivenDateRange(LocalDate.of(2016, 5, 1),
        LocalDate.of(2016, 5, 30)));
  }
}
