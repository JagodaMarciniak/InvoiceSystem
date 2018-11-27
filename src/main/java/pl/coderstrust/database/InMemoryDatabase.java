package pl.coderstrust.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.coderstrust.model.Invoice;

@NoArgsConstructor
public class InMemoryDatabase implements Database {

  private List<Invoice> invoices = Collections.synchronizedList(new ArrayList<>());

  @Override
  public boolean existsById(@NonNull Integer id) {
    return invoices
        .stream()
        .anyMatch(i -> i.getId().equals(id));
  }

  @Override
  public Invoice save(@NonNull Invoice invoice) {
    delete(invoice);
    invoices.add(invoice);
    return invoice;
  }

  @Override
  public void delete(@NonNull Invoice invoice) {
    invoices.removeIf(i -> i.getId().equals(invoice.getId()));
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
  public Optional<Invoice> findById(@NonNull Integer id) {
    return Optional.of(invoices
        .stream()
        .filter(invoice -> invoice.getId().equals(id))
        .findFirst())
        .orElse(null);
  }

  @Override
  public List<Invoice> findAll() {
    return invoices;
  }

  @Override
  public List<Invoice> findAllBySellerName(@NonNull String sellerName) {
    return invoices
        .stream()
        .filter(invoice -> invoice.getSeller().getName().equals(sellerName))
        .collect(Collectors.toList());
  }

  @Override
  public List<Invoice> findAllByBuyerName(@NonNull String buyerName) {
    return invoices
        .stream()
        .filter(invoice -> invoice.getBuyer().getName().equals(buyerName))
        .collect(Collectors.toList());
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public <S extends Invoice> Iterable<S> saveAll(Iterable<S> iterable) {
    throw new UnsupportedOperationException("Not supported");
  }

  @Override
  public Iterable<Invoice> findAllById(Iterable<Integer> iterable) {
    throw new UnsupportedOperationException("Not supported");
  }

  @Override
  public void deleteAll(Iterable<? extends Invoice> iterable) {
    throw new UnsupportedOperationException("Not supported");
  }

  @Override
  public void deleteById(Integer integer) {
    throw new UnsupportedOperationException("Not supported");
  }
}
