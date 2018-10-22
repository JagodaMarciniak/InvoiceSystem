package pl.coderstrust;

import java.util.List;

public interface Database {

    List<Invoice> getInvoices();

    Invoice getInvoiceById(String invoiceId);

    List<Invoice> getInvoiceBySellerName(String sellerName);

    List<Invoice> getInvoiceByBuyerName(String buyerName);

    void saveInvoice(Invoice invoice);

    void updateInvoice(Invoice invoice);

    void removeInvoice(String invoiceId);
}
