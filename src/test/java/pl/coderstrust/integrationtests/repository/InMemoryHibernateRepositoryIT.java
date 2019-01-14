package pl.coderstrust.integrationtests.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import pl.coderstrust.generators.CompanyGenerator;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.repository.RepositoryOperationException;
import pl.coderstrust.repository.invoice.HibernateInvoiceRepository;
import pl.coderstrust.repository.invoice.HibernateRepository;
import pl.coderstrust.repository.invoice.InvoiceRepository;

@ExtendWith(MockitoExtension.class)
class InMemoryHibernateRepositoryIT {

  @Mock
  HibernateInvoiceRepository hibernateInvoiceRepository;

  private InvoiceRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    repository = new HibernateRepository(hibernateInvoiceRepository);
  }

  @Test
  void shouldSaveInvoiceToDatabase() throws RepositoryOperationException {
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();

    when(hibernateInvoiceRepository.save(invoice1)).thenReturn(invoice2);

    Invoice actual = repository.save(invoice1);

    assertEquals(invoice2, actual);
    Mockito.verify(hibernateInvoiceRepository).save(invoice1);
  }

  @Test
  @Transactional
  void shouldReturnFalseIfInvoiceNotExistsInDatabase() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    repository.save(invoice1);

    //when
    boolean invoiceExist = repository.existsById("");

    //then
    assertFalse(invoiceExist);
  }

  @Test
  @Transactional
  void shouldSaveIntoDatabase() throws RepositoryOperationException {
    //given
    Invoice expectedInvoice1 = InvoiceGenerator.getRandomInvoice();

    //when
    String id = repository.save(expectedInvoice1).getId();
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
    String id = repository.save(invoice).getId();

    //when
    repository.deleteById(id);
    boolean existsById = repository.existsById(id);

    //then
    assertFalse(existsById);
  }

  @Test
  @Transactional
  void shouldThrowExceptionWhenTryingDeleteByIdFromDatabaseIfAbsent() {
    assertThrows(EmptyResultDataAccessException.class, () -> repository.deleteById("95cdbbad-61c4-4ba6-858f-445d7bcf048e"));
  }

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
    Invoice expectedInvoice2 = repository.save(invoice2);
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
    String sellerId = repository.save(invoice1).getSeller().getId();
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
