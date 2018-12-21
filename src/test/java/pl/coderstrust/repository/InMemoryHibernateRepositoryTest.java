package pl.coderstrust.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    repository.save(invoice);

    //when
    boolean existsById = repository.existsById(invoice.getId());

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
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoiceWithSpecificId(20);

    //when
    Invoice asd = repository.save(expectedInvoice);
    System.out.println("asd: "+asd);
    boolean existsById = repository.existsById(expectedInvoice.getId());

    //then
    assertTrue(existsById);
  }

  @Test
  @Transactional
  void shouldUpdateIntoDatabase() throws RepositoryOperationException {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoiceWithSpecificId(5);

    //when
    Invoice added = repository.save(expectedInvoice);
    added.setComments("Asdasd");
    repository.save(added);
    System.out.println("asd: "+expectedInvoice.getId());
    boolean existsById = repository.existsById(expectedInvoice.getId());

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
}
