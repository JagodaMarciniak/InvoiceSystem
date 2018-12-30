package pl.coderstrust.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import pl.coderstrust.configuration.InvoiceJpaConfig;
import pl.coderstrust.generators.CompanyGenerator;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Company;
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
    repository.save(invoice1);

    //when
    boolean invoiceExist = repository.existsById(3234);

    //then
    assertFalse(invoiceExist);
  }

  @Test
  @Transactional
  void shouldSaveIntoDatabase() throws RepositoryOperationException {
    //given
    Invoice expectedInvoice1 = InvoiceGenerator.getRandomInvoice();

    //when
    int id = repository.save(expectedInvoice1).getId();
    boolean existsById = repository.existsById(id);

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
    long expectedNumberOfInvoices = 5L;
    for (long i = 0; i < expectedNumberOfInvoices; i++) {
      repository.save(InvoiceGenerator.getRandomInvoice());
    }

    //when
    long actualNumberOfInvoices = repository.count();

    //then
    assertEquals(expectedNumberOfInvoices, actualNumberOfInvoices);
  }

  @Test
  @Transactional
  void shouldFindOneInvoice() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    Invoice expectedInvoice1 = repository.save(invoice1);
    Invoice expectedInvoice2 = repository.save(invoice2);

    //when
    Invoice invoiceFromDatabase1 = repository.findById(expectedInvoice1.getId()).get();
    Invoice invoiceFromDatabase2 = repository.findById(expectedInvoice2.getId()).get();

    //then
    assertEquals(expectedInvoice1, invoiceFromDatabase1);
    assertEquals(expectedInvoice2, invoiceFromDatabase2);
  }

  @Test
  @Transactional
  void shouldFindAllInvoices() throws RepositoryOperationException {
    //given
    List<Invoice> generatedInvoices = new ArrayList<>();
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice expectedInvoice1 = repository.save(invoice1);
    generatedInvoices.add(expectedInvoice1);
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    Invoice expectedInvoice2 =repository.save(invoice2);
    generatedInvoices.add(expectedInvoice2);

    //when
    Iterable<Invoice> actualInvoices = repository.findAll();
    List<Invoice> result = new ArrayList<>();
    actualInvoices.forEach(result::add);

    //then
    assertArrayEquals(generatedInvoices.toArray(), result.toArray());
  }

  @Test
  @Transactional
  void shouldFindAllInvoicesBySellerName() throws RepositoryOperationException {
    //given
    String sellerName = "sampleSellerABC";
    Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(sellerName);
    Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(sellerName);
    Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(sellerName);

    Invoice invoice4 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice5 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice6 = InvoiceGenerator.getRandomInvoice();

    Invoice expectedInvoice1 = repository.save(invoice1);
    Invoice expectedInvoice2 = repository.save(invoice2);
    Invoice expectedInvoice3 = repository.save(invoice3);
    repository.save(invoice4);
    repository.save(invoice5);
    repository.save(invoice6);

    List<Invoice> expectedInvoices = new ArrayList<>();
    expectedInvoices.add(expectedInvoice1);
    expectedInvoices.add(expectedInvoice2);
    expectedInvoices.add(expectedInvoice3);

    //when
    Iterable<Invoice> actualInvoices = repository.findAllBySellerName(sellerName);
    List<Invoice> result = new ArrayList<>();
    actualInvoices.forEach(result::add);

    //then
    assertArrayEquals(expectedInvoices.toArray(), result.toArray());
  }

  @Test
  @Transactional
  void findAllInvoicesByBuyerName() throws RepositoryOperationException {
    //given
    String buyerName = "sampleBuyerABC";
    Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName(buyerName);
    Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName(buyerName);
    Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName(buyerName);

    Invoice invoice4 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice5 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice6 = InvoiceGenerator.getRandomInvoice();

    Invoice expectedInvoice1 = repository.save(invoice1);
    Invoice expectedInvoice2 = repository.save(invoice2);
    Invoice expectedInvoice3 = repository.save(invoice3);
    repository.save(invoice4);
    repository.save(invoice5);
    repository.save(invoice6);

    List<Invoice> expectedInvoices = new ArrayList<>();
    expectedInvoices.add(expectedInvoice1);
    expectedInvoices.add(expectedInvoice2);
    expectedInvoices.add(expectedInvoice3);

    //when
    Iterable<Invoice> actualInvoices = repository.findAllByBuyerName(buyerName);
    List<Invoice> result = new ArrayList<>();
    actualInvoices.forEach(result::add);

    //then
    assertArrayEquals(expectedInvoices.toArray(), result.toArray());
  }

  @Test
  @Transactional
  void shouldUpdateExistingInvoice() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice1Update = repository.save(invoice1);

    //when
    repository.save(invoice1Update);
    Invoice updatedInvoiceFromDatabase = repository.findById(invoice1Update.getId()).get();

    //then
    assertEquals(invoice1Update.getId(), updatedInvoiceFromDatabase.getId());
    assertEquals(invoice1Update, updatedInvoiceFromDatabase);
  }

  @Test
  @Transactional
  void shouldCheckSellerNameWithTheSameIdWasUpdateInAllRecords() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    Company seller = CompanyGenerator.getSampleCompany();
    invoice1.setSeller(seller);
    invoice2.setSeller(seller);

    //when
    int sellerId = repository.save(invoice1).getSeller().getId();
    seller.setId(sellerId);
    seller.setName("Example Seller Name");
    repository.save(invoice2);
    Iterable<Invoice> getAllInvoices = repository.findAll();
    List<String> sellerFromDB = new ArrayList<>();
    getAllInvoices.forEach(invoice -> sellerFromDB.add(invoice.getSeller().toString().replaceAll("id=\\d+", "")));
    String expectedSellerToString = seller.toString().replaceAll("id=\\d+", "");

    //then
    assertEquals(sellerId, getAllInvoices.iterator().next().getSeller().getId());
    assertEquals(expectedSellerToString, sellerFromDB.get(0));
    assertEquals(expectedSellerToString, sellerFromDB.get(1));
  }
}
