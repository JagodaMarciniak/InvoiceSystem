package pl.coderstrust.database;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import pl.coderstrust.model.Invoice;

public interface Database extends CrudRepository<Invoice, Integer> {

  List<Invoice> findAllBySellerName(String sellerName) throws DatabaseOperationException;

  List<Invoice> findAllByBuyerName(String buyerName) throws DatabaseOperationException;

}
