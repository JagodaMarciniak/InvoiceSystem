package pl.coderstrust.repository.invoice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import pl.coderstrust.model.Invoice;

@NoArgsConstructor
public class InMemoryInvoiceRepository implements InvoiceRepository {

  private List<Invoice> invoices = Collections.synchronizedList(new ArrayList<>());

  @Override
  public boolean existsById(Integer id) {
    return invoices
        .stream()
        .anyMatch(i -> i.getId() == id);
  }

  @Override
  public Invoice save(Invoice invoice) {
    deleteById(invoice.getId());
    invoices.add(invoice);
    return invoice;
  }

  @Override
  public void deleteById(Integer id) {
    invoices.removeIf(i -> i.getId() == id);
  }

  @Override
  public void deleteAll() {
    invoices.clear();
  }

  @Override
  public long count() {
    return (long) invoices.size();
  }

  @Override
  public Optional<Invoice> findById(Integer id) {
    return invoices
        .stream()
        .filter(invoice -> invoice.getId() == id)
        .findFirst();
  }

  @Override
  public Iterable<Invoice> findAll() {
    return invoices;
  }

  @Override
  public Iterable<Invoice> findAllBySellerName(String sellerName) {
    return invoices
        .stream()
        .filter(invoice -> invoice.getSeller().getName().equals(sellerName))
        .collect(Collectors.toList());
  }

  @Override
  public Iterable<Invoice> findAllByBuyerName(String buyerName) {
    return invoices
        .stream()
        .filter(invoice -> invoice.getBuyer().getName().equals(buyerName))
        .collect(Collectors.toList());
  }
}
