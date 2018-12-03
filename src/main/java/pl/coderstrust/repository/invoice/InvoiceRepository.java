package pl.coderstrust.repository.invoice;

import pl.coderstrust.model.Invoice;
import pl.coderstrust.repository.BaseRepository;
import pl.coderstrust.repository.RepositoryOperationException;

public interface InvoiceRepository extends BaseRepository<Invoice, Integer> {

  Iterable<Invoice> findAllBySellerName(String sellerName) throws RepositoryOperationException;

  Iterable<Invoice> findAllByBuyerName(String buyerName) throws RepositoryOperationException;

}
