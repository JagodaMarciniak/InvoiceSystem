package pl.coderstrust.database.invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderstrust.model.Invoice;

public interface HibernateInvoiceRepository extends JpaRepository<Invoice, String> {
}
