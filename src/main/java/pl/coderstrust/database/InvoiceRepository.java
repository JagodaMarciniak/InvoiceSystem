package pl.coderstrust.database;

import pl.coderstrust.model.Invoice;

public interface InvoiceRepository extends BaseRepository<Invoice, Integer> {

  Iterable<Invoice> findAllBySellerName(String sellerName) throws RepositoryOperationException;

  Iterable<Invoice> findAllByBuyerName(String buyerName) throws RepositoryOperationException;

}
