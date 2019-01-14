package pl.coderstrust.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.repository.RepositoryOperationException;
import pl.coderstrust.repository.invoice.InvoiceRepository;

@Service
public class InvoiceService {

  private InvoiceRepository invoiceRepository;

  @Autowired
  public InvoiceService(@NonNull InvoiceRepository invoiceRepository){
    this.invoiceRepository = invoiceRepository;
  }

  public List<Invoice> getAllInvoices() throws InvoiceServiceOperationException {
    try {
      List<Invoice> result = new ArrayList<>();
      invoiceRepository.findAll().forEach(result::add);
      return result;
    } catch (RepositoryOperationException e) {
      throw new InvoiceServiceOperationException("An error occurred during getting all invoices", e);
    }
  }

  public Optional<Invoice> getInvoice(@NonNull String invoiceId) throws InvoiceServiceOperationException {
    try {
      return invoiceRepository.findById(invoiceId);
    } catch (RepositoryOperationException e) {
      throw new InvoiceServiceOperationException(String.format("An error occurred during getting single invoice by id. Invoice id: %s",
          invoiceId), e);
    }
  }

  public Invoice addInvoice(@NonNull Invoice invoice) throws InvoiceServiceOperationException {
    try {
      return invoiceRepository.save(invoice);
    } catch (RepositoryOperationException e) {
      throw new InvoiceServiceOperationException(String.format("An error occurred during adding new invoice. Invoice: %s", invoice), e);
    }
  }

  public void updateInvoice(@NonNull Invoice invoice) throws InvoiceServiceOperationException {
    try {
      String id = invoice.getId();
      if (invoiceRepository.existsById(id)) {
        invoiceRepository.save(invoice);
      } else {
        throw new InvoiceServiceOperationException("Invoice with id " + id + " does not exist");
      }
    } catch (RepositoryOperationException e) {
      throw new InvoiceServiceOperationException(String.format("An error occurred during updating invoice. Invoice: %s", invoice), e);
    }
  }

  public void deleteInvoice(@NonNull String invoiceId) throws InvoiceServiceOperationException {
    try {
      invoiceRepository.deleteById(invoiceId);
    } catch (RepositoryOperationException e) {
      throw new InvoiceServiceOperationException(String.format("An error occurred during deleting invoice. Invoice: %s", invoiceId), e);
    }
  }

  public void deleteAllInvoices() throws InvoiceServiceOperationException {
    try {
      invoiceRepository.deleteAll();
    } catch (RepositoryOperationException e) {
      throw new InvoiceServiceOperationException(("An error occurred during deleting all invoices"), e);
    }
  }

  public List<Invoice> getAllInvoicesIssuedInGivenDateRange(@NonNull LocalDate startDate, @NonNull LocalDate endDate) throws
      InvoiceServiceOperationException {
    if (startDate.until(endDate, ChronoUnit.DAYS) < 0) {
      throw new IllegalArgumentException("The end date must be newer or equal to start date");
    }
    return getAllInvoices().stream()
        .filter(invoice -> invoice.getIssueDate().compareTo(startDate) >= 0 && invoice.getIssueDate().compareTo(endDate) <= 0)
        .collect(Collectors.toList());
  }
}
