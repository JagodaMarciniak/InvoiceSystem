package pl.coderstrust.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

  public Invoice addNewInvoice(@NonNull Invoice invoice) throws InvoiceBookOperationException {
    try {
      return database.saveInvoice(invoice);
    } catch (DatabaseOperationException e) {
      throw new InvoiceBookOperationException("Database doesn't exist");
    }
  }

  public void updateInvoice(@NonNull Invoice invoice) throws
      InvoiceBookOperationException, DatabaseOperationException {
    database.saveInvoice(invoice);
  }

  public void deleteInvoice(@NonNull String invoiceId) throws InvoiceBookOperationException {
    try {
      if (database.invoiceExists(invoiceId)) {
        Invoice invoice = database.findOneInvoice(invoiceId);
        database.deleteInvoice(invoice);
      } else {
        throw new InvoiceBookOperationException("Invoice with id " + invoiceId + " does not exist");
      }
    } catch (DatabaseOperationException e) {
      throw new InvoiceBookOperationException("ExceptionŁ while deleting invoice", e);
    }
  }

  public List<Invoice> getAllInvoicesInGivenDateRange(LocalDate startDate, LocalDate endDate) throws InvoiceBookOperationException {
    try {
      return database.findAllInvoices().stream().filter(invoice -> invoice.getIssueDate()
          .compareTo(startDate) >= 0 && invoice.getIssueDate().compareTo(endDate) <= 0)
          .collect(Collectors.toList());
    } catch (DatabaseOperationException e) {
      throw new InvoiceBookOperationException("Database error");
    }
  }
}
