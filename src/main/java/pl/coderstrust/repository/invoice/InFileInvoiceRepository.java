package pl.coderstrust.repository.invoice;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.Synchronized;
import pl.coderstrust.helpers.FileHelper;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.repository.RepositoryOperationException;

public class InFileInvoiceRepository implements InvoiceRepository {

  private FileHelper fileHelper;
  private ObjectMapper mapper;

  public InFileInvoiceRepository(@NonNull FileHelper fileHelper, @NonNull ObjectMapper mapper) throws Exception {
    this.fileHelper = fileHelper;
    this.mapper = mapper;
    if (!fileHelper.exists()) {
      fileHelper.initialize();
    }
  }

  @Override
  @Synchronized
  public Invoice save(@NonNull Invoice invoice) throws RepositoryOperationException {
    try {
      this.deleteById(invoice.getId());
      fileHelper.writeLine(mapper.writeValueAsString(invoice));
      return invoice;
    } catch (IOException e) {
      throw new RepositoryOperationException(String.format("Encountered problems searching for invoice: %s", invoice), e);
    }
  }

  @Synchronized
  public Optional<Invoice> findById(@NonNull Integer id) throws RepositoryOperationException {
    try {
      return fileHelper.readLines().stream()
          .map(this::deserializeJsonToInvoice)
          .filter(invoice -> invoice != null && invoice.getId() == id)
          .findFirst();
    } catch (IOException e) {
      throw new RepositoryOperationException("Encountered problems while searching for invoice.", e);
    }
  }

  @Override
  @Synchronized
  public List<Invoice> findAll() throws RepositoryOperationException {
    try {
      return fileHelper.readLines().stream()
          .map(this::deserializeJsonToInvoice)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RepositoryOperationException("Encountered problems while searching for invoices.", e);
    }
  }

  @Override
  @Synchronized
  public List<Invoice> findAllBySellerName(@NonNull String sellerName) throws RepositoryOperationException {
    try {
      return fileHelper.readLines().stream()
          .map(this::deserializeJsonToInvoice)
          .filter(invoice -> invoice != null && invoice.getSeller().getName().equals(sellerName))
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RepositoryOperationException("Encountered problems while searching for invoices.", e);
    }
  }

  @Override
  @Synchronized
  public List<Invoice> findAllByBuyerName(@NonNull String buyerName) throws RepositoryOperationException {
    try {
      return fileHelper.readLines().stream()
          .map(this::deserializeJsonToInvoice)
          .filter(invoice -> invoice != null && invoice.getBuyer().getName().equals(buyerName))
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RepositoryOperationException("Encountered problems while searching for invoices.", e);
    }
  }

  @Override
  @Synchronized
  public long count() throws RepositoryOperationException {
    try {
      if (fileHelper.isEmpty()) {
        return 0L;
      }
      return (long) findAll().size();
    } catch (IOException e) {
      throw new RepositoryOperationException("Encountered problems while counting invoices.", e);
    }
  }

  @Synchronized
  public void deleteById(@NonNull Integer id) throws RepositoryOperationException {
    try {
      List<Invoice> invoices = findAll();
      Optional<Invoice> invoice = invoices.stream()
          .filter(i -> i.getId() == id)
          .findFirst();
      if (invoice.isPresent()) {
        fileHelper.removeLine(invoices.indexOf(invoice.get()) + 1);
      }
    } catch (Exception e) {
      throw new RepositoryOperationException("Encountered problem while deleting invoice.", e);
    }
  }

  @Synchronized
  public boolean existsById(@NonNull Integer id) throws RepositoryOperationException {
    try {
      return fileHelper.readLines().stream()
          .map(this::deserializeJsonToInvoice)
          .filter(Objects::nonNull)
          .anyMatch(invoice -> invoice.getId() == id);
    } catch (IOException e) {
      throw new RepositoryOperationException(String.format("Encountered problems looking for invoice: %s", id), e);
    }
  }

  @Synchronized
  public void deleteAll() throws RepositoryOperationException {
    try {
      fileHelper.clear();
    } catch (IOException e) {
      throw new RepositoryOperationException("Encountered problem while deleting invoices.", e);
    }
  }

  private Invoice deserializeJsonToInvoice(String json) {
    try {
      return mapper.readValue(json, Invoice.class);
    } catch (Exception e) {
      System.err.println("Unsuccessful JSON deserialization.");
      return null;
    }
  }
}
