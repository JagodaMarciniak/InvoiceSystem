package pl.coderstrust.database;

import java.util.List;
import pl.coderstrust.model.Invoice;

public interface Database {

  Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException;

  Invoice findOneInvoice(String invoiceId) throws DatabaseOperationException;

  List<Invoice> findAllInvoices() throws DatabaseOperationException;

  List<Invoice> findAllInvoicesBySellerName(String sellerName) throws DatabaseOperationException;

  List<Invoice> findAllInvoicesByBuyerName(String buyerName) throws DatabaseOperationException;

  Long countInvoices() throws DatabaseOperationException;

  void deleteInvoice(Invoice invoice) throws DatabaseOperationException;

  boolean invoiceExists(String invoiceId) throws DatabaseOperationException;
}
