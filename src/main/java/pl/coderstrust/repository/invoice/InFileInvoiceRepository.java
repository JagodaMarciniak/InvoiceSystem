package pl.coderstrust.repository.invoice;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.helpers.FileHelper;
import pl.coderstrust.helpers.FileHelperException;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.repository.RepositoryOperationException;

@ConditionalOnProperty(name = "pl.coderstrust.repository", havingValue = "in-file")
@Repository
public class InFileInvoiceRepository implements InvoiceRepository {

  private FileHelper fileHelper;
  private ObjectMapper mapper;
  private int lastInvoiceId;

  @Autowired
  public InFileInvoiceRepository(@NonNull FileHelper fileHelper, @NonNull ObjectMapper mapper) throws RepositoryOperationException {
    this.fileHelper = fileHelper;
    this.mapper = mapper;
    try {
      if (!fileHelper.exists()) {
        fileHelper.initialize();
      }
      lastInvoiceId = getLastInvoiceId();
    } catch (IOException | FileHelperException e) {
      throw new RepositoryOperationException("Encountered problems while initializing in-file invoice repository.", e);
    }
  }

  private int getLastInvoiceId() throws IOException {
    String lastInvoiceAsJson = fileHelper.readLastLine();
    if (lastInvoiceAsJson == null) {
      return 0;
    }
    Invoice lastInvoice = mapper.readValue(lastInvoiceAsJson, Invoice.class);
    return Integer.parseInt(lastInvoice.getId());
  }

  @Override
  @Synchronized
  public Invoice save(@NonNull Invoice invoice) throws RepositoryOperationException {
    if (existsById(invoice.getId())) {
      deleteById(invoice.getId());
    } else {
      invoice.setId(String.valueOf(getNextInvoiceId()));
    }
    try {
      fileHelper.writeLine(mapper.writeValueAsString(invoice));
    } catch (IOException e) {
      throw new RepositoryOperationException(String.format("Encountered problems saving invoice: %s", invoice), e);
    }
    return invoice;
  }

  @Synchronized
  public Optional<Invoice> findById(@NonNull String id) throws RepositoryOperationException {
    try {
      return getAllInvoices().stream()
          .filter(invoice -> invoice != null && invoice.getId().equals(id))
          .findFirst();
    } catch (IOException e) {
      throw new RepositoryOperationException(String.format("Encountered problems while searching for invoice:, %s", id), e);
    }
  }

  @Override
  @Synchronized
  public Iterable<Invoice> findAll() throws RepositoryOperationException {
    try {
      return getAllInvoices();
    } catch (IOException e) {
      throw new RepositoryOperationException("Encountered problems while searching for invoices.", e);
    }
  }

  @Override
  @Synchronized
  public Iterable<Invoice> findAllBySellerName(@NonNull String sellerName) throws RepositoryOperationException {
    try {
      return getAllInvoices().stream()
          .filter(invoice -> invoice != null && invoice.getSeller().getName().equals(sellerName))
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RepositoryOperationException(String.format("Encountered problems while searching for invoices with seller name: %s", sellerName), e);
    }
  }

  @Override
  @Synchronized
  public Iterable<Invoice> findAllByBuyerName(@NonNull String buyerName) throws RepositoryOperationException {
    try {
      return getAllInvoices().stream()
          .filter(invoice -> invoice != null && invoice.getBuyer().getName().equals(buyerName))
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RepositoryOperationException(String.format("Encountered problems while searching for invoices with buyer name: %s", buyerName), e);
    }
  }

  @Override
  @Synchronized
  public long count() throws RepositoryOperationException {
    try {
      if (fileHelper.isEmpty()) {
        return 0L;
      }
      return (long) getAllInvoices().size();
    } catch (IOException e) {
      throw new RepositoryOperationException("Encountered problems while counting invoices.", e);
    }
  }

  @Synchronized
  public void deleteById(@NonNull String id) throws RepositoryOperationException {
    try {
      List<Invoice> invoices = getAllInvoices();
      Optional<Invoice> invoice = invoices.stream()
          .filter(i -> i.getId().equals(id))
          .findFirst();
      if (invoice.isPresent()) {
        fileHelper.removeLine(invoices.indexOf(invoice.get()) + 1);
      } else {
        throw new RepositoryOperationException(String.format("There was no invoice in database with id %s", id));
      }
    } catch (IOException | FileHelperException e) {
      throw new RepositoryOperationException(String.format("Encountered problem while deleting invoice: %s", id), e);
    }
  }

  @Synchronized
  public boolean existsById(@NonNull String id) throws RepositoryOperationException {
    try {
      return getAllInvoices().stream()
          .filter(Objects::nonNull)
          .anyMatch(invoice -> invoice.getId().equals(id));
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

  private List<Invoice> getAllInvoices() throws IOException {
    return fileHelper.readLines().stream()
        .map(this::deserializeJsonToInvoice)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  private int getNextInvoiceId() {
    return ++lastInvoiceId;
  }
}
