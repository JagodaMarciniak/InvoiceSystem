package pl.coderstrust.repository.invoice;

import org.springframework.data.jpa.repository.Query;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.repository.BaseRepository;
import pl.coderstrust.repository.RepositoryOperationException;

public interface InvoiceRepository extends BaseRepository<Invoice, Integer> {

  @Query(value = "from Invoice")
  Iterable<Invoice> findAllBySellerName(String sellerName) throws RepositoryOperationException;

  @Query(value = "from Invoice")
  Iterable<Invoice> findAllByBuyerName(String buyerName) throws RepositoryOperationException;

}
