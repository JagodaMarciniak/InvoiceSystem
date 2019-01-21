package pl.coderstrust.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.database.invoice.InvoiceDatabase;
import pl.coderstrust.model.Invoice;

@Slf4j
@Service
public class InvoiceService {

  private InvoiceDatabase invoiceDatabase;

  @Autowired
  public InvoiceService(@NonNull InvoiceDatabase invoiceDatabase) {
    this.invoiceDatabase = invoiceDatabase;
  }

  public List<Invoice> getAllInvoices() throws ServiceOperationException {
    try {
      log.info("Getting all invoices from database");
      List<Invoice> result = new ArrayList<>();
      invoiceDatabase.findAll().forEach(result::add);
      log.debug(String.format("Getting all invoices from database successful"));
      return result;
    } catch (DatabaseOperationException e) {
      log.error(String.format("An error occurred during getting all invoices from database"));
      throw new ServiceOperationException("An error occurred during getting all invoices", e);
    }
  }

  public Optional<Invoice> getInvoice(@NonNull String invoiceId) throws ServiceOperationException {
    try {
      log.info(String.format("Getting invoice by id from database"));
      log.debug(String.format("Getting invoice by id from database - successful. Id:%s", invoiceId));
      return invoiceDatabase.findById(invoiceId);
    } catch (DatabaseOperationException e) {
      log.error(String.format("An error occurred during getting single invoice from database by id. Invoice id: %s",
          invoiceId));
      throw new ServiceOperationException(String.format("An error occurred during getting single invoice by id from database. Invoice id: %s",
          invoiceId), e);
    }
  }

  public boolean invoiceExists(@NonNull String invoiceId) throws ServiceOperationException {
    try {
      log.info(String.format("Checking if invoice is existing in database"));
      boolean invoiceExist = invoiceDatabase.existsById(invoiceId);
      log.debug(String.format("Invoice with given id:%s exists: %s", invoiceExist));
      return invoiceExist;
    } catch (DatabaseOperationException e) {
      log.error(String.format("An error occurred during checking if invoice exists in database. Invoice id: %s",
          invoiceId));
      throw new ServiceOperationException(String.format("An error occurred during checking if invoice exists in database. Invoice id: %s",
          invoiceId), e);
    }
  }

  public Invoice addInvoice(@NonNull Invoice invoice) throws ServiceOperationException {
    try {
      log.info(String.format("Saving invoice to database"));
      return invoiceDatabase.save(invoice);
    } catch (DatabaseOperationException e) {
      log.error(String.format("An error occurred during checking if invoice exists in database. Invoice id: %s",
          invoice));
      throw new ServiceOperationException(String.format("An error occurred during adding new invoice. Invoice: %s", invoice), e);
    }
  }

  public void updateInvoice(@NonNull Invoice invoice) throws ServiceOperationException {
    try {
      log.info(String.format("Updating invoice to database. Invoice :%s",invoice));
      String id = invoice.getId();
      if (invoiceDatabase.existsById(id)) {
        log.debug(String.format("Successful update of invoice to database. Invoice :%s",invoice));
        invoiceDatabase.save(invoice);
      } else {
        log.error(String.format("Invoice with id %s does not exist", id));
        throw new ServiceOperationException(String.format("Invoice with id %s does not exist", id));
      }
    } catch (DatabaseOperationException e) {
      log.error(String.format("An error occurred during updating invoice. Invoice: %s", invoice));
      throw new ServiceOperationException(String.format("An error occurred during updating invoice. Invoice: %s", invoice), e);
    }
  }

  public void deleteInvoice(@NonNull String invoiceId) throws ServiceOperationException {
    try {
      log.info(String.format("Deleting invoice from database"));
      invoiceDatabase.deleteById(invoiceId);
      log.debug(String.format("Successful deleting invoice from database with id:%s", invoiceId));
    } catch (DatabaseOperationException e) {
      log.error(String.format("An error occurred during deleting invoice. Invoice: %s", invoiceId));
      throw new ServiceOperationException(String.format("An error occurred during deleting invoice. Invoice: %s", invoiceId), e);
    }
  }

  public void deleteAllInvoices() throws ServiceOperationException {
    try {
      log.info("Deleting of all invoices from database");
      invoiceDatabase.deleteAll();
      log.debug("Successful deleting of all invoices from database");
    } catch (DatabaseOperationException e) {
      log.error(String.format("\"An error occurred during deleting all invoices"));
      throw new ServiceOperationException(("An error occurred during deleting all invoices"), e);
    }
  }

  public List<Invoice> getAllInvoicesIssuedInGivenDateRange(@NonNull LocalDate startDate, @NonNull LocalDate endDate) throws
      ServiceOperationException {
    log.info("Getting of all invoices from database in given data range");
    if (startDate.until(endDate, ChronoUnit.DAYS) < 0) {
      log.error("The end date must be newer or equal to start date");
      throw new IllegalArgumentException("The end date must be newer or equal to start date");
    }
    log.debug("Getting all invoices in given data range from database successful");
    return getAllInvoices().stream()
        .filter(invoice -> invoice.getIssueDate().compareTo(startDate) >= 0 && invoice.getIssueDate().compareTo(endDate) <= 0)
        .collect(Collectors.toList());
  }
}
