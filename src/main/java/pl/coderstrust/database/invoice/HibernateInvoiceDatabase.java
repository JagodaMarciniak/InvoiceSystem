package pl.coderstrust.database.invoice;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Repository;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;

@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "hibernate")
@Repository
public class HibernateInvoiceDatabase implements InvoiceDatabase {

  private HibernateInvoiceRepository hibernateInvoiceRepository;

  @Autowired
  public HibernateInvoiceDatabase(HibernateInvoiceRepository hibernateInvoiceRepository) {
    this.hibernateInvoiceRepository = hibernateInvoiceRepository;
  }

  @Override
  public Invoice save(@NonNull Invoice invoice) throws DatabaseOperationException {
    try {
      return hibernateInvoiceRepository.save(invoice);
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException(String.format("Encountered problems saving invoice: %s", invoice), e);
    }
  }

  @Override
  public Optional<Invoice> findById(@NonNull String id) throws DatabaseOperationException {
    try {
      return hibernateInvoiceRepository.findById(id);
    } catch (NoSuchElementException e) {
      throw new DatabaseOperationException(String.format("Encountered problems while searching for invoice:, %s", id), e);
    }
  }

  @Override
  public boolean existsById(@NonNull String id) throws DatabaseOperationException {
    try {
      return hibernateInvoiceRepository.existsById(id);
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException(String.format("Encountered problems looking for invoice: %s", id), e);
    }
  }

  @Override
  public Iterable<Invoice> findAll() throws DatabaseOperationException {
    try {
      return hibernateInvoiceRepository.findAll();
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException("Encountered problems while searching for invoices.", e);
    }
  }

  @Override
  public long count() throws DatabaseOperationException {
    try {
      return hibernateInvoiceRepository.count();
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException("Encountered problems while counting invoices.", e);
    }
  }

  @Override
  public void deleteById(String id) throws DatabaseOperationException {
    try {
      hibernateInvoiceRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new DatabaseOperationException(String.format("There was no invoice in database with id %s", id), e);
    }
  }

  @Override
  public void deleteAll() throws DatabaseOperationException {
    try {
      hibernateInvoiceRepository.deleteAll();
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException("Encountered problem while deleting invoices.", e);
    }
  }

  @Override
  public Iterable<Invoice> findAllBySellerName(@NonNull String sellerName) throws DatabaseOperationException {
    try {
      return hibernateInvoiceRepository.findAll()
          .stream()
          .filter(invoice -> invoice.getSeller().getName().equals(sellerName))
          .collect(Collectors.toList());
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException(String.format("Encountered problems while searching for invoices with seller name: %s", sellerName), e);
    }
  }

  @Override
  public Iterable<Invoice> findAllByBuyerName(@NonNull String buyerName) throws DatabaseOperationException {
    try {
      return hibernateInvoiceRepository.findAll()
          .stream()
          .filter(invoice -> invoice.getBuyer().getName().equals(buyerName))
          .collect(Collectors.toList());
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException(String.format("Encountered problems while searching for invoices with buyer name: %s", buyerName), e);
    }
  }
}
