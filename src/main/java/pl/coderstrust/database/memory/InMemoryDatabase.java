package pl.coderstrust.database.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

@NoArgsConstructor
public class InMemoryDatabase implements Database {

  private List<Invoice> invoices = Collections.synchronizedList(new ArrayList<>());

  @Override
  public boolean invoiceExists(@NonNull String invoiceId) {
    return invoices
        .stream()
        .anyMatch(i -> i.getId().equals(invoiceId));
  }

  @Override
  public Invoice saveInvoice(@NonNull Invoice newInvoice) {
    deleteInvoice(newInvoice.getId());
    invoices.add(newInvoice);
    return newInvoice;
  }

  @Override
  public void deleteInvoice(@NonNull String invoiceId) {
    invoices.removeIf(i -> i.getId().equals(invoiceId));
  }

  @Override
  public Long countInvoices() {
    return (long) invoices.size();
  }

  @Override
  public Invoice findOneInvoice(@NonNull String id) {
    return invoices
        .stream()
        .filter(invoice -> invoice.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  @Override
  public List<Invoice> findAllInvoices() {
    return invoices;
  }

  @Override
  public List<Invoice> findAllInvoicesBySellerName(@NonNull String sellerName) {
    return invoices
        .stream()
        .filter(invoice -> invoice.getSeller().getName().equals(sellerName))
        .collect(Collectors.toList());
  }

  @Override
  public List<Invoice> findAllInvoicesByBuyerName(@NonNull String buyerName) {
    return invoices
        .stream()
        .filter(invoice -> invoice.getBuyer().getName().equals(buyerName))
        .collect(Collectors.toList());
  }
}
