package pl.coderstrust.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Synchronized;
import pl.coderstrust.helpers.FileHelper;
import pl.coderstrust.model.Invoice;

public class InFileDatabase implements Database {

  @NonNull
  private FileHelper fileHelper;

  @NonNull
  private ObjectMapper mapper;

  public InFileDatabase(FileHelper fileHelper, ObjectMapper mapper) throws Exception {
    this.fileHelper = fileHelper;
    this.mapper = mapper;
    if (!fileHelper.exists()) {
      fileHelper.initialize();
    }
  }

  @Override
  @Synchronized
  public Invoice saveInvoice(@NonNull Invoice invoice) throws DatabaseOperationException {
    try {
      deleteInvoice(invoice.getId());
      fileHelper.writeLine(mapper.writeValueAsString(invoice));
      return invoice;
    } catch (IOException e) {
      throw new DatabaseOperationException(String.format("Encountered problems searching for invoice: %s", invoice), e);
    }
  }

  @Override
  @Synchronized
  public Invoice findOneInvoice(@NonNull String invoiceId) throws DatabaseOperationException {
    try {
      return fileHelper.readLines().stream()
          .map(this::deserializeJsonToInvoice)
          .filter(invoice -> invoice != null && invoice.getId().equals(invoiceId))
          .findFirst()
          .orElse(null);
    } catch (IOException e) {
      throw new DatabaseOperationException("Encountered problems while searching for invoice.", e);
    }
  }

  @Override
  @Synchronized
  public List<Invoice> findAllInvoices() throws DatabaseOperationException {
    try {
      return fileHelper.readLines().stream()
          .map(this::deserializeJsonToInvoice)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new DatabaseOperationException("Encountered problems while searching for invoices.", e);
    }
  }

  @Override
  @Synchronized
  public List<Invoice> findAllInvoicesBySellerName(@NonNull String sellerName) throws DatabaseOperationException {
    try {
      return fileHelper.readLines().stream()
          .map(this::deserializeJsonToInvoice)
          .filter(invoice -> invoice != null && invoice.getSeller().getName().equals(sellerName))
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new DatabaseOperationException("Encountered problems while searching for invoices.", e);
    }
  }

  @Override
  @Synchronized
  public List<Invoice> findAllInvoicesByBuyerName(@NonNull String buyerName) throws DatabaseOperationException {
    try {
      return fileHelper.readLines().stream()
          .map(this::deserializeJsonToInvoice)
          .filter(invoice -> invoice != null && invoice.getBuyer().getName().equals(buyerName))
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new DatabaseOperationException("Encountered problems while searching for invoices.", e);
    }
  }

  @Override
  @Synchronized
  public Long countInvoices() throws DatabaseOperationException {
    try {
      if (fileHelper.isEmpty()) {
        return 0L;
      }
      return (long) findAllInvoices().size();
    } catch (IOException e) {
      throw new DatabaseOperationException("Encountered problems while counting invoices.", e);
    }
  }

  @Override
  @Synchronized
  public void deleteInvoice(@NonNull String invoiceId) throws DatabaseOperationException {
    try {
      List<Invoice> invoices = findAllInvoices();
      Optional<Invoice> invoice = invoices.stream()
          .filter(i -> i.getId().equals(invoiceId))
          .findFirst();
      if (invoice.isPresent()) {
        fileHelper.removeLine(invoices.indexOf(invoice.get()) + 1);
      }
    } catch (Exception e) {
      throw new DatabaseOperationException("Encountered problem while deleting invoice.", e);
    }
  }

  @Override
  @Synchronized
  public boolean invoiceExists(@NonNull String invoiceId) throws DatabaseOperationException {
    try {
      return fileHelper.readLines().stream()
          .map(this::deserializeJsonToInvoice)
          .filter(Objects::nonNull)
          .anyMatch(invoice -> invoice.getId().equals(invoiceId));
    } catch (IOException e) {
      throw new DatabaseOperationException(String.format("Encountered problems looking for invoice: %s", invoiceId), e);
    }
  }

  private Invoice deserializeJsonToInvoice (String json){
    try {
      return mapper.readValue(json, Invoice.class);
    } catch (Exception e) {
      System.err.println("Unsuccessful JSON deserialization.");
      return null;
    }
  }
}
