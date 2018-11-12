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

class InMemoryDatabaseTest {
  private Database testDatabase;

  @BeforeEach
  private void setup() {
    testDatabase = new InMemoryDatabase();
  }

  @Test
  void shouldReturnTrueIfInvoiceExistsInDatabase() throws DatabaseOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    testDatabase.saveInvoice(invoice);

    //when
    boolean invoiceExists = testDatabase.invoiceExists(invoice.getId());

    //then
    assertTrue(invoiceExists);
  }

  @Test
  void shouldReturnFalseIfInvoiceNotExistsInDatabase() throws DatabaseOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    testDatabase.saveInvoice(invoice1);

    //when
    boolean invoiceExist = testDatabase.invoiceExists(invoice2.getId());

    //then
    assertFalse(invoiceExist);
  }

  @Test
  void shouldSaveInvoiceIntoDatabase() throws DatabaseOperationException {
    //given
    Invoice expectedInvoice1 = InvoiceGenerator.getRandomInvoice();

    //when
    testDatabase.saveInvoice(expectedInvoice1);
    boolean invoiceExists = testDatabase.invoiceExists(expectedInvoice1.getId());

    //then
    assertTrue(invoiceExists);
  }

  @Test
  void shouldDeleteInvoiceFromDatabaseIfPresent() throws DatabaseOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    testDatabase.saveInvoice(invoice);

    //when
    testDatabase.deleteInvoice(invoice.getId());
    boolean invoiceExists = testDatabase.invoiceExists(invoice.getId());

    //then
    assertFalse(invoiceExists);
  }

  @Test
  void shouldDeleteInvoiceFromDatabaseIfAbsent() throws DatabaseOperationException {
    //given
    String invoiceId = "1";

    //when
    testDatabase.deleteInvoice(invoiceId);
    boolean invoiceExists = testDatabase.invoiceExists(invoiceId);

    //then
    assertFalse(invoiceExists);
  }

  @Test
  void shouldTestCountingInvoicesInDatabase() throws DatabaseOperationException {
    //given
    Long expectedNumberOfInvoices = 5L;
    for (int i = 0; i < expectedNumberOfInvoices; i++) {
      testDatabase.saveInvoice(InvoiceGenerator.getRandomInvoice());
    }

    //when
    Long actualNumberOfInvoices = testDatabase.countInvoices();

    //then
    assertEquals(expectedNumberOfInvoices, actualNumberOfInvoices);
  }

  @Test
  void shouldFindOneInvoice() throws DatabaseOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    testDatabase.saveInvoice(invoice1);
    testDatabase.saveInvoice(invoice2);

    //when
    Invoice invoiceFromDatabase1 = testDatabase.findOneInvoice(invoice1.getId());
    Invoice invoiceFromDatabase2 = testDatabase.findOneInvoice(invoice2.getId());

    //then
    assertEquals(invoice1, invoiceFromDatabase1);
    assertEquals(invoice2, invoiceFromDatabase2);
  }

  @Test
  void shouldFindAllInvoices() throws DatabaseOperationException {
    //given
    List<Invoice> generatedInvoices = new ArrayList<>();

    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    generatedInvoices.add(invoice1);
    testDatabase.saveInvoice(invoice1);

    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();

    generatedInvoices.add(invoice2);
    testDatabase.saveInvoice(invoice2);

    //when
    List<Invoice> actualInvoices = testDatabase.findAllInvoices();

    //then
    assertArrayEquals(generatedInvoices.toArray(), actualInvoices.toArray());
  }

  @Test
  void shouldFindAllInvoicesBySellerName() throws DatabaseOperationException {
    //given
    String sellerName = "sampleSellerABC";
    Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(sellerName);
    Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(sellerName);
    Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(sellerName);

    Invoice invoice4 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice5 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice6 = InvoiceGenerator.getRandomInvoice();

    testDatabase.saveInvoice(invoice1);
    testDatabase.saveInvoice(invoice2);
    testDatabase.saveInvoice(invoice3);
    testDatabase.saveInvoice(invoice4);
    testDatabase.saveInvoice(invoice5);
    testDatabase.saveInvoice(invoice6);

    List<Invoice> expectedInvoices = new ArrayList<>();
    expectedInvoices.add(invoice1);
    expectedInvoices.add(invoice2);
    expectedInvoices.add(invoice3);

    //when
    List<Invoice> actualInvoices = testDatabase.findAllInvoicesBySellerName(sellerName);

    //then
    assertArrayEquals(expectedInvoices.toArray(), actualInvoices.toArray());
  }

  @Test
  void findAllInvoicesByBuyerName() throws DatabaseOperationException {
    //given
    String buyerName = "sampleBuyerABC";
    Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName(buyerName);
    Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName(buyerName);
    Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName(buyerName);

    Invoice invoice4 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice5 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice6 = InvoiceGenerator.getRandomInvoice();

    testDatabase.saveInvoice(invoice1);
    testDatabase.saveInvoice(invoice2);
    testDatabase.saveInvoice(invoice3);
    testDatabase.saveInvoice(invoice4);
    testDatabase.saveInvoice(invoice5);
    testDatabase.saveInvoice(invoice6);

    List<Invoice> expectedInvoices = new ArrayList<>();
    expectedInvoices.add(invoice1);
    expectedInvoices.add(invoice2);
    expectedInvoices.add(invoice3);

    //when
    List<Invoice> actualInvoices = testDatabase.findAllInvoicesByBuyerName(buyerName);

    //then
    assertArrayEquals(expectedInvoices.toArray(), actualInvoices.toArray());
  }

  @Test
  void shouldUpdateExistingInvoice() throws DatabaseOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice1Update = InvoiceGenerator.getRandomInvoiceWithSpecificId(invoice1.getId());
    testDatabase.saveInvoice(invoice1);
    testDatabase.saveInvoice(invoice1Update);

    //when
    Invoice updatedInvoiceFromDatabase = testDatabase.findOneInvoice(invoice1.getId());

    //then
    assertEquals(invoice1.getId(), updatedInvoiceFromDatabase.getId());
    assertEquals(invoice1Update, updatedInvoiceFromDatabase);
  }

  @Test
  void shouldThrowExceptionIfMethodInvoiceExistInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> testDatabase.invoiceExists(null));
  }

  @Test
  void shouldThrowExceptionIfMethodSaveInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> testDatabase.saveInvoice(null));
  }

  @Test
  void shouldThrowExceptionIfMethodDeleteInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> testDatabase.deleteInvoice(null));
  }

  @Test
  void shouldThrowExceptionIfMethodFindOneInvoiceInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> testDatabase.findOneInvoice(null));
  }

  @Test
  void shouldThrowExceptionIfMethodFindAllInvoicesBySellerNameInvokedWithNull() {
    assertThrows(IllegalArgumentException.class,
        () -> testDatabase.findAllInvoicesBySellerName(null));
  }

  @Test
  void shouldThrowExceptionIfMethodFindAllInvoicesByBuyerNameInvokedWithNull() {
    assertThrows(IllegalArgumentException.class,
        () -> testDatabase.findAllInvoicesByBuyerName(null));
  }
}
