package pl.coderstrust.database;

import java.util.List;
import pl.coderstrust.model.Invoice;

public interface Database {

  boolean invoiceExists(String invoiceId) throws DatabaseOperationException;

  Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException;

  void deleteInvoice(String invoiceId) throws DatabaseOperationException;

  Long countInvoices() throws DatabaseOperationException;

  Invoice findOneInvoice(String invoiceId) throws DatabaseOperationException;

  List<Invoice> findAllInvoices() throws DatabaseOperationException;

  List<Invoice> findAllInvoicesBySellerName(String sellerName) throws DatabaseOperationException;

  List<Invoice> findAllInvoicesByBuyerName(String buyerName) throws DatabaseOperationException;

}
