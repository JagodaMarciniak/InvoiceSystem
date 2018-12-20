package pl.coderstrust.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.repository.invoice.InMemoryInvoiceRepository;
import pl.coderstrust.repository.invoice.InvoiceRepository;

class InMemoryInvoiceRepositoryTest {
  private InvoiceRepository testInvoiceRepository;

  @BeforeEach
  private void setup() {
    testInvoiceRepository = new InMemoryInvoiceRepository();
  }

  @Test
  void shouldReturnTrueIfExistsByIdInDatabase() throws RepositoryOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    testInvoiceRepository.save(invoice);

    //when
    boolean existsById = testInvoiceRepository.existsById(invoice.getId());

    //then
    assertTrue(existsById);
  }

  @Test
  void shouldReturnFalseIfInvoiceNotExistsInDatabase() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    testInvoiceRepository.save(invoice1);

    //when
    boolean invoiceExist = testInvoiceRepository.existsById(invoice2.getId());

    //then
    assertFalse(invoiceExist);
  }

  @Test
  void shouldSaveIntoDatabase() throws RepositoryOperationException {
    //given
    Invoice expectedInvoice1 = InvoiceGenerator.getRandomInvoice();

    //when
    testInvoiceRepository.save(expectedInvoice1);
    boolean existsById = testInvoiceRepository.existsById(expectedInvoice1.getId());

    //then
    assertTrue(existsById);
  }

  @Test
  void shouldDeleteAllInvoicesInDatabase() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    testInvoiceRepository.save(invoice1);
    testInvoiceRepository.save(invoice2);

    //when
    testInvoiceRepository.deleteAll();

    //then
    assertEquals(0, testInvoiceRepository.count());
  }

  @Test
  void shouldPerformDeleteAllMethodEvenIfDatabaseIsEmpty() throws RepositoryOperationException {
    //when
    testInvoiceRepository.deleteAll();

    //then
    assertEquals(0, testInvoiceRepository.count());
  }

  @Test
  void shouldDeleteByIdFromDatabaseIfPresent() throws RepositoryOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    testInvoiceRepository.save(invoice);

    //when
    testInvoiceRepository.deleteById(invoice.getId());
    boolean existsById = testInvoiceRepository.existsById(invoice.getId());

    //then
    assertFalse(existsById);
  }

  @Test
  void shouldDeleteByIdFromDatabaseIfAbsent() throws RepositoryOperationException {
    //given
    int invoiceId = 1;

    //when
    testInvoiceRepository.deleteById(invoiceId);
    boolean existsById = testInvoiceRepository.existsById(invoiceId);

    //then
    assertFalse(existsById);
  }

  @Test
  void shouldCountingInvoicesInDatabase() throws RepositoryOperationException {
    //given
    Long expectedNumberOfInvoices = 5L;
    for (int i = 0; i < expectedNumberOfInvoices; i++) {
      testInvoiceRepository.save(InvoiceGenerator.getRandomInvoice());
    }

    //when
    Long actualNumberOfInvoices = testInvoiceRepository.count();

    //then
    assertEquals(expectedNumberOfInvoices, actualNumberOfInvoices);
  }

  @Test
  void shouldFindOneInvoice() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    testInvoiceRepository.save(invoice1);
    testInvoiceRepository.save(invoice2);

    //when
    Optional invoiceFromDatabase1 = testInvoiceRepository.findById(invoice1.getId());
    Optional invoiceFromDatabase2 = testInvoiceRepository.findById(invoice2.getId());

    //then
    assertEquals(invoice1, invoiceFromDatabase1.get());
    assertEquals(invoice2, invoiceFromDatabase2.get());
  }

  @Test
  void shouldFindAllInvoices() throws RepositoryOperationException {
    //given
    List<Invoice> generatedInvoices = new ArrayList<>();
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    generatedInvoices.add(invoice1);
    testInvoiceRepository.save(invoice1);
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    generatedInvoices.add(invoice2);
    testInvoiceRepository.save(invoice2);

    //when
    Iterable<Invoice> actualInvoices = testInvoiceRepository.findAll();
    List<Invoice> result = new ArrayList<>();
    actualInvoices.forEach(result::add);

    //then
    assertArrayEquals(generatedInvoices.toArray(), result.toArray());
  }

  @Test
  void shouldFindAllInvoicesBySellerName() throws RepositoryOperationException {
    //given
    String sellerName = "sampleSellerABC";
    Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(sellerName);
    Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(sellerName);
    Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(sellerName);

    Invoice invoice4 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice5 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice6 = InvoiceGenerator.getRandomInvoice();

    testInvoiceRepository.save(invoice1);
    testInvoiceRepository.save(invoice2);
    testInvoiceRepository.save(invoice3);
    testInvoiceRepository.save(invoice4);
    testInvoiceRepository.save(invoice5);
    testInvoiceRepository.save(invoice6);

    List<Invoice> expectedInvoices = new ArrayList<>();
    expectedInvoices.add(invoice1);
    expectedInvoices.add(invoice2);
    expectedInvoices.add(invoice3);

    //when
    Iterable<Invoice> actualInvoices = testInvoiceRepository.findAllBySellerName(sellerName);
    List<Invoice> result = new ArrayList<>();
    actualInvoices.forEach(result::add);

    //then
    assertArrayEquals(expectedInvoices.toArray(), result.toArray());
  }

  @Test
  void findAllInvoicesByBuyerName() throws RepositoryOperationException {
    //given
    String buyerName = "sampleBuyerABC";
    Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName(buyerName);
    Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName(buyerName);
    Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName(buyerName);

    Invoice invoice4 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice5 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice6 = InvoiceGenerator.getRandomInvoice();

    testInvoiceRepository.save(invoice1);
    testInvoiceRepository.save(invoice2);
    testInvoiceRepository.save(invoice3);
    testInvoiceRepository.save(invoice4);
    testInvoiceRepository.save(invoice5);
    testInvoiceRepository.save(invoice6);

    List<Invoice> expectedInvoices = new ArrayList<>();
    expectedInvoices.add(invoice1);
    expectedInvoices.add(invoice2);
    expectedInvoices.add(invoice3);

    //when
    Iterable<Invoice> actualInvoices = testInvoiceRepository.findAllByBuyerName(buyerName);
    List<Invoice> result = new ArrayList<>();
    actualInvoices.forEach(result::add);

    //then
    assertArrayEquals(expectedInvoices.toArray(), result.toArray());
  }

  @Test
  void shouldUpdateExistingInvoice() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice1Update = InvoiceGenerator.getRandomInvoiceWithSpecificId(invoice1.getId());
    testInvoiceRepository.save(invoice1);

    //when
    testInvoiceRepository.save(invoice1Update);
    Optional updatedInvoiceFromDatabase = testInvoiceRepository.findById(invoice1.getId());
    Invoice resultAfterUpdate = (Invoice) updatedInvoiceFromDatabase.get();

    //then
    assertEquals(invoice1.getId(), resultAfterUpdate.getId());
    assertEquals(invoice1Update, updatedInvoiceFromDatabase.get());
  }
}
