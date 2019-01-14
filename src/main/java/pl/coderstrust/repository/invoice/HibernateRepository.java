package pl.coderstrust.repository.invoice;

import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.model.Invoice;

@ConditionalOnProperty(name = "pl.coderstrust.repository", havingValue = "hibernate")
@Repository
public class HibernateRepository implements InvoiceRepository {

    private HibernateInvoiceRepository hibernateInvoiceRepository;

    @Autowired
    public HibernateRepository(HibernateInvoiceRepository hibernateInvoiceRepository){
        this.hibernateInvoiceRepository = hibernateInvoiceRepository;
    }

    @Override
    public Invoice save(@NonNull Invoice invoice) {
        return hibernateInvoiceRepository.save(invoice);
    }

    @Override
    public Optional<Invoice> findById(@NonNull String id){
        return hibernateInvoiceRepository.findById(id);
    }

    @Override
    public boolean existsById(@NonNull String id) {
        return hibernateInvoiceRepository.existsById(id);
    }

    @Override
    public Iterable<Invoice> findAll() {
        return hibernateInvoiceRepository.findAll();
    }

    @Override
    public long count(){
        return hibernateInvoiceRepository.count();
    }

    @Override
    public void deleteById(String id) {
        hibernateInvoiceRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        hibernateInvoiceRepository.deleteAll();
    }

    @Override
    public Iterable<Invoice> findAllBySellerName(@NonNull String sellerName) {
        return hibernateInvoiceRepository.findAll().stream().filter(invoice -> invoice.getSeller().getName() == sellerName).collect(Collectors.toList());
    }

    @Override
    public Iterable<Invoice> findAllByBuyerName(@NonNull String buyerName) {
        return hibernateInvoiceRepository.findAll().stream().filter(invoice -> invoice.getBuyer().getName() == buyerName).collect(Collectors.toList());
    }
}
