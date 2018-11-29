package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

class InMemoryInvoiceRepositoryTest {
  private InvoiceRepository testInvoiceRepository;

  @BeforeEach
  private void setup() {
    testInvoiceRepository = new InMemoryInvoiceRepository();
  }

  @Test
  void shouldReturnTrueIfInvoiceExistsInDatabase() throws RepositoryOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    testInvoiceRepository.saveInvoice(invoice);

    //when
    boolean invoiceExists = testInvoiceRepository.invoiceExists(invoice.getId());

    //then
    assertTrue(invoiceExists);
  }

  @Test
  void shouldReturnFalseIfInvoiceNotExistsInDatabase() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    testInvoiceRepository.saveInvoice(invoice1);

    //when
    boolean invoiceExist = testInvoiceRepository.invoiceExists(invoice2.getId());

    //then
    assertFalse(invoiceExist);
  }

  @Test
  void shouldSaveInvoiceIntoDatabase() throws RepositoryOperationException {
    //given
    Invoice expectedInvoice1 = InvoiceGenerator.getRandomInvoice();

    //when
    testInvoiceRepository.saveInvoice(expectedInvoice1);
    boolean invoiceExists = testInvoiceRepository.invoiceExists(expectedInvoice1.getId());

    //then
    assertTrue(invoiceExists);
  }

  @Test
  void shouldDeleteInvoiceFromDatabaseIfPresent() throws RepositoryOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    testInvoiceRepository.saveInvoice(invoice);

    //when
    testInvoiceRepository.deleteInvoice(invoice.getId());
    boolean invoiceExists = testInvoiceRepository.invoiceExists(invoice.getId());

    //then
    assertFalse(invoiceExists);
  }

  @Test
  void shouldDeleteInvoiceFromDatabaseIfAbsent() throws RepositoryOperationException {
    //given
    String invoiceId = "1";

    //when
    testInvoiceRepository.deleteInvoice(invoiceId);
    boolean invoiceExists = testInvoiceRepository.invoiceExists(invoiceId);

    //then
    assertFalse(invoiceExists);
  }

  @Test
  void shouldTestCountingInvoicesInDatabase() throws RepositoryOperationException {
    //given
    Long expectedNumberOfInvoices = 5L;
    for (int i = 0; i < expectedNumberOfInvoices; i++) {
      testInvoiceRepository.saveInvoice(InvoiceGenerator.getRandomInvoice());
    }

    //when
    Long actualNumberOfInvoices = testInvoiceRepository.countInvoices();

    //then
    assertEquals(expectedNumberOfInvoices, actualNumberOfInvoices);
  }

  @Test
  void shouldFindOneInvoice() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    testInvoiceRepository.saveInvoice(invoice1);
    testInvoiceRepository.saveInvoice(invoice2);

    //when
    Invoice invoiceFromDatabase1 = testInvoiceRepository.findOneInvoice(invoice1.getId());
    Invoice invoiceFromDatabase2 = testInvoiceRepository.findOneInvoice(invoice2.getId());

    //then
    assertEquals(invoice1, invoiceFromDatabase1);
    assertEquals(invoice2, invoiceFromDatabase2);
  }

  @Test
  void shouldFindAllInvoices() throws RepositoryOperationException {
    //given
    List<Invoice> generatedInvoices = new ArrayList<>();

    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    generatedInvoices.add(invoice1);
    testInvoiceRepository.saveInvoice(invoice1);

    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();

    generatedInvoices.add(invoice2);
    testInvoiceRepository.saveInvoice(invoice2);

    //when
    List<Invoice> actualInvoices = testInvoiceRepository.findAllInvoices();

    //then
    assertArrayEquals(generatedInvoices.toArray(), actualInvoices.toArray());
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

    testInvoiceRepository.saveInvoice(invoice1);
    testInvoiceRepository.saveInvoice(invoice2);
    testInvoiceRepository.saveInvoice(invoice3);
    testInvoiceRepository.saveInvoice(invoice4);
    testInvoiceRepository.saveInvoice(invoice5);
    testInvoiceRepository.saveInvoice(invoice6);

    List<Invoice> expectedInvoices = new ArrayList<>();
    expectedInvoices.add(invoice1);
    expectedInvoices.add(invoice2);
    expectedInvoices.add(invoice3);

    //when
    List<Invoice> actualInvoices = testInvoiceRepository.findAllBySellerName(sellerName);

    //then
    assertArrayEquals(expectedInvoices.toArray(), actualInvoices.toArray());
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

    testInvoiceRepository.saveInvoice(invoice1);
    testInvoiceRepository.saveInvoice(invoice2);
    testInvoiceRepository.saveInvoice(invoice3);
    testInvoiceRepository.saveInvoice(invoice4);
    testInvoiceRepository.saveInvoice(invoice5);
    testInvoiceRepository.saveInvoice(invoice6);

    List<Invoice> expectedInvoices = new ArrayList<>();
    expectedInvoices.add(invoice1);
    expectedInvoices.add(invoice2);
    expectedInvoices.add(invoice3);

    //when
    List<Invoice> actualInvoices = testInvoiceRepository.findAllByBuyerName(buyerName);

    //then
    assertArrayEquals(expectedInvoices.toArray(), actualInvoices.toArray());
  }

  @Test
  void shouldUpdateExistingInvoice() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice1Update = InvoiceGenerator.getRandomInvoiceWithSpecificId(invoice1.getId());
    testInvoiceRepository.saveInvoice(invoice1);
    testInvoiceRepository.saveInvoice(invoice1Update);

    //when
    Invoice updatedInvoiceFromDatabase = testInvoiceRepository.findOneInvoice(invoice1.getId());

    //then
    assertEquals(invoice1.getId(), updatedInvoiceFromDatabase.getId());
    assertEquals(invoice1Update, updatedInvoiceFromDatabase);
  }

  @Test
  void shouldThrowExceptionIfMethodInvoiceExistInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> testInvoiceRepository.invoiceExists(null));
  }

  @Test
  void shouldThrowExceptionIfMethodSaveInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> testInvoiceRepository.saveInvoice(null));
  }

  @Test
  void shouldThrowExceptionIfMethodDeleteInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> testInvoiceRepository.deleteInvoice(null));
  }

  @Test
  void shouldThrowExceptionIfMethodFindOneInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> testInvoiceRepository.findOneInvoice(null));
  }

  @Test
  void shouldThrowExceptionIfMethodFindAllInvoicesBySellerNameInvokedWithNull() {
    assertThrows(IllegalArgumentException.class,
        () -> testInvoiceRepository.findAllBySellerName(null));
  }

  @Test
  void shouldThrowExceptionIfMethodFindAllInvoicesByBuyerNameInvokedWithNull() {
    assertThrows(IllegalArgumentException.class,
        () -> testInvoiceRepository.findAllByBuyerName(null));
  }
}
