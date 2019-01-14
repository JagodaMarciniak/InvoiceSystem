package pl.coderstrust.database.invoice;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;

public interface InvoiceDatabase extends Database<Invoice, String> {

  Iterable<Invoice> findAllBySellerName(String sellerName) throws DatabaseOperationException;

  Iterable<Invoice> findAllByBuyerName(String buyerName) throws DatabaseOperationException;
}
