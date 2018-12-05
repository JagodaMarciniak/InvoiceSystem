package pl.coderstrust.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;

@RequiredArgsConstructor
public class InvoiceService {
  @NonNull
  private Database database;

  public List<Invoice> getAllInvoices() throws InvoiceBookOperationException {
    try {
      return database.findAllInvoices();
    } catch (DatabaseOperationException e) {
      throw new InvoiceBookOperationException("An error occurred during getting all invoices", e);
    }
  }

  public Invoice getSingleInvoiceById(@NonNull String invoiceId) throws InvoiceBookOperationException {
    try {
      return database.findOneInvoice(invoiceId);
    } catch (DatabaseOperationException e) {
      throw new InvoiceBookOperationException(String.format("An error occurred during getting single invoice by id. Invoice id: %s", invoiceId), e);
    }
  }

  public Invoice addInvoice(@NonNull Invoice invoice) throws InvoiceBookOperationException {
    try {
      return database.saveInvoice(invoice);
    } catch (DatabaseOperationException e) {
      throw new InvoiceBookOperationException(String.format("An error occurred during adding new invoice. Invoice: %s", invoice), e);
    }
  }

  public void updateInvoice(@NonNull Invoice invoice) throws InvoiceBookOperationException {
    try {
      database.saveInvoice(invoice);
    } catch (DatabaseOperationException e) {
      throw new InvoiceBookOperationException(String.format("An error occurred during updating invoice. Invoice: %s", invoice), e);
    }
  }

  public void deleteInvoice(@NonNull String invoiceId) throws InvoiceBookOperationException {
    try {
      database.deleteInvoice(invoiceId);
    } catch (DatabaseOperationException e) {
      throw new InvoiceBookOperationException(String.format("An error occurred during deleting " + "invoice. Invoice: %s", invoiceId), e);
    }
  }

  public List<Invoice> getAllInvoicesInGivenDateRange(@NonNull LocalDate startDate, @NonNull LocalDate endDate) throws InvoiceBookOperationException {
    if (startDate.until(endDate, ChronoUnit.DAYS) < 0) {
      throw new IllegalArgumentException("The end date must be older or equal to start date");
    }
    try {
      return database.findAllInvoices().stream()
          .filter(invoice -> invoice.getIssueDate().compareTo(startDate) >= 0 && invoice.getIssueDate().compareTo(endDate) <= 0)
          .collect(Collectors.toList());
    } catch (DatabaseOperationException e) {
      throw new InvoiceBookOperationException(
          String.format("An error occurred during getting all invoices in given date range. "
              + "Start date: %s, end date: %s", startDate, endDate), e);
    }
  }
}
