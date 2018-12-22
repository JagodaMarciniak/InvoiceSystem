package pl.coderstrust.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import pl.coderstrust.configuration.InvoiceJpaConfig;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.repository.invoice.InvoiceRepository;

import javax.annotation.Resource;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = { InvoiceJpaConfig.class },
    loader = AnnotationConfigContextLoader.class)
@Rollback
@Transactional(transactionManager = "myTransactionManager")
class InMemoryHibernateRepositoryTest {

  @Resource
  private InvoiceRepository repository;

  @Test
  @Transactional
  void shouldReturnTrueIfExistsByIdInDatabase() throws RepositoryOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    int id = repository.save(invoice).getId();

    //when
    boolean existsById = repository.existsById(id);

    //then
    assertTrue(existsById);
  }

  @Test
  @Transactional
  void shouldReturnFalseIfInvoiceNotExistsInDatabase() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    repository.save(invoice1);

    //when
    boolean invoiceExist = repository.existsById(invoice2.getId());

    //then
    assertFalse(invoiceExist);
  }

  @Test
  @Transactional
  void shouldSaveIntoDatabase() throws RepositoryOperationException {
    //given
    Invoice expectedInvoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificId(1);

    //when
    repository.save(expectedInvoice1);
    boolean existsById = repository.existsById(expectedInvoice1.getId());

    //then
    assertTrue(existsById);
  }

  @Test
  @Transactional
  void shouldDeleteAllInvoicesInDatabase() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    repository.save(invoice1);
    repository.save(invoice2);

    //when
    repository.deleteAll();

    //then
    assertEquals(0, repository.count());
  }

  @Test
  @Transactional
  void shouldPerformDeleteAllMethodEvenIfDatabaseIsEmpty() throws RepositoryOperationException {
    //when
    repository.deleteAll();

    //then
    assertEquals(0, repository.count());
  }

  @Test
  @Transactional
  void shouldDeleteByIdFromDatabaseIfPresent() throws RepositoryOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    int id = repository.save(invoice).getId();

    //when
    repository.deleteById(id);
    boolean existsById = repository.existsById(id);

    //then
    assertFalse(existsById);
  }

//  @Test
//  @Transactional
//  void shouldDeleteByIdFromDatabaseIfAbsent() throws RepositoryOperationException {
//    //given
//    int invoiceId = 1;
//
//    //when
//    repository.deleteById(invoiceId);
//    boolean existsById = repository.existsById(invoiceId);
//
//    //then
//    assertFalse(existsById);
//  }

  @Test
  @Transactional
  void shouldCountingInvoicesInDatabase() throws RepositoryOperationException {
    //given
    Long expectedNumberOfInvoices = 5L;
    for (int i = 0; i < expectedNumberOfInvoices; i++) {
      repository.save(InvoiceGenerator.getRandomInvoice());
    }

    //when
    Long actualNumberOfInvoices = repository.count();

    //then
    assertEquals(expectedNumberOfInvoices, actualNumberOfInvoices);
  }

  @Test
  @Transactional
  void shouldFindOneInvoice() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificId(1);
    Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificId(2);
    repository.save(invoice1);
    repository.save(invoice2);

    //when
    Optional invoiceFromDatabase1 = repository.findById(invoice1.getId());
    Optional invoiceFromDatabase2 = repository.findById(invoice2.getId());

    //then
    assertEquals(invoice1, invoiceFromDatabase1.get());
    assertEquals(invoice2, invoiceFromDatabase2.get());
  }
}
