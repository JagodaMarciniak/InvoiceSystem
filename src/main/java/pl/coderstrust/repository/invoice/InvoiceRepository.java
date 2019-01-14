package pl.coderstrust.repository.invoice;

import org.springframework.data.jpa.repository.Query;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.repository.BaseRepository;
import pl.coderstrust.repository.RepositoryOperationException;

public interface InvoiceRepository extends BaseRepository<Invoice, String> {

  @Query(value = "from Invoice where seller.name=?1")
  Iterable<Invoice> findAllBySellerName(String sellerName) throws RepositoryOperationException;

  @Query(value = "from Invoice where buyer.name=?1")
  Iterable<Invoice> findAllByBuyerName(String buyerName) throws RepositoryOperationException;

}
