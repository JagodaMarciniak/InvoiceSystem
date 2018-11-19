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
public class InvoiceBook {
  @NonNull
  private Database database;

  public List<Invoice> getAllInvoices() throws InvoiceBookOperationException {
    try {
      return database.findAllInvoices();
    } catch (DatabaseOperationException e) {
      throw new InvoiceBookOperationException("Exception while getting all invoices", e);
    }
  }

  public Invoice getSingleInvoiceById(@NonNull String invoiceId) throws InvoiceBookOperationException {
    try {
      return database.findOneInvoice(invoiceId);
    } catch (DatabaseOperationException e) {
      throw new InvoiceBookOperationException("Exception while getting single invoice by id", e);
    }
  }

  public Invoice addInvoice(@NonNull Invoice invoice) throws InvoiceBookOperationException {
    try {
      return database.saveInvoice(invoice);
    } catch (DatabaseOperationException e) {
      throw new InvoiceBookOperationException("Exception while adding new invoice", e);
    }
  }

  public void updateInvoice(@NonNull Invoice invoice) throws InvoiceBookOperationException {
    try {
      database.saveInvoice(invoice);
    } catch (DatabaseOperationException e) {
      throw new InvoiceBookOperationException("Exception while updating invoice", e);
    }
  }

  public void deleteInvoice(@NonNull String invoiceId) throws InvoiceBookOperationException {
    try {
      database.deleteInvoice(invoiceId);
    } catch (DatabaseOperationException e) {
      throw new InvoiceBookOperationException("Exception while deleting invoice", e);
    }
  }

  public List<Invoice> getAllInvoicesInGivenDateRange(@NonNull LocalDate startDate, @NonNull LocalDate endDate) throws InvoiceBookOperationException {
    if (startDate.until(endDate, ChronoUnit.DAYS) < 1) {
      throw new InvoiceBookOperationException("Wrong date range");
    }
    try {
      return database.findAllInvoices().stream()
          .filter(invoice -> invoice.getIssueDate().compareTo(startDate) >= 0 && invoice.getIssueDate().compareTo(endDate) <= 0)
          .collect(Collectors.toList());
    } catch (DatabaseOperationException e) {
      throw new InvoiceBookOperationException("Exception while getting all invoices in given date range", e);
    }
  }
}
