package pl.coderstrust.database.invoice;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

class InMemoryInvoiceDatabaseTest {
  private InvoiceDatabase database;

  @BeforeEach
  private void setup() {
    database = new InMemoryInvoiceDatabase();
  }

  @Test
  void shouldReturnTrueIfExistsByIdInDatabase() throws DatabaseOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    database.save(invoice);

    //when
    boolean existsById = database.existsById(invoice.getId());

    //then
    assertTrue(existsById);
  }

  @Test
  void shouldReturnFalseIfInvoiceNotExistsInDatabase() throws DatabaseOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    database.save(invoice1);

    //when
    boolean invoiceExist = database.existsById(invoice2.getId());

    //then
    assertFalse(invoiceExist);
  }

  @Test
  void shouldSaveIntoDatabase() throws DatabaseOperationException {
    //given
    Invoice expectedInvoice1 = InvoiceGenerator.getRandomInvoice();

    //when
    database.save(expectedInvoice1);
    boolean existsById = database.existsById(expectedInvoice1.getId());

    //then
    assertTrue(existsById);
  }

  @Test
  void shouldDeleteAllInvoicesInDatabase() throws DatabaseOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    database.save(invoice1);
    database.save(invoice2);

    //when
    database.deleteAll();

    //then
    assertEquals(0, database.count());
  }

  @Test
  void shouldPerformDeleteAllMethodEvenIfDatabaseIsEmpty() throws DatabaseOperationException {
    //when
    database.deleteAll();

    //then
    assertEquals(0, database.count());
  }

  @Test
  void shouldDeleteByIdFromDatabaseIfPresent() throws DatabaseOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    database.save(invoice);

    //when
    database.deleteById(invoice.getId());
    boolean existsById = database.existsById(invoice.getId());

    //then
    assertFalse(existsById);
  }

  @Test
  void shouldDeleteByIdFromDatabaseIfAbsent() throws DatabaseOperationException {
    //given
    String invoiceId = "1";

    //when
    database.deleteById(invoiceId);
    boolean existsById = database.existsById(invoiceId);

    //then
    assertFalse(existsById);
  }

  @Test
  void shouldCountingInvoicesInDatabase() throws DatabaseOperationException {
    //given
    Long expectedNumberOfInvoices = 5L;
    for (int i = 0; i < expectedNumberOfInvoices; i++) {
      database.save(InvoiceGenerator.getRandomInvoice());
    }

    //when
    Long actualNumberOfInvoices = database.count();

    //then
    assertEquals(expectedNumberOfInvoices, actualNumberOfInvoices);
  }

  @Test
  void shouldFindOneInvoice() throws DatabaseOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    database.save(invoice1);
    database.save(invoice2);

    //when
    Optional invoiceFromDatabase1 = database.findById(invoice1.getId());
    Optional invoiceFromDatabase2 = database.findById(invoice2.getId());

    //then
    assertEquals(invoice1, invoiceFromDatabase1.get());
    assertEquals(invoice2, invoiceFromDatabase2.get());
  }

  @Test
  void shouldFindAllInvoices() throws DatabaseOperationException {
    //given
    List<Invoice> generatedInvoices = new ArrayList<>();
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    generatedInvoices.add(invoice1);
    database.save(invoice1);
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    generatedInvoices.add(invoice2);
    database.save(invoice2);

    //when
    Iterable<Invoice> actualInvoices = database.findAll();
    List<Invoice> result = new ArrayList<>();
    actualInvoices.forEach(result::add);

    //then
    assertArrayEquals(generatedInvoices.toArray(), result.toArray());
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

    database.save(invoice1);
    database.save(invoice2);
    database.save(invoice3);
    database.save(invoice4);
    database.save(invoice5);
    database.save(invoice6);

    List<Invoice> expectedInvoices = new ArrayList<>();
    expectedInvoices.add(invoice1);
    expectedInvoices.add(invoice2);
    expectedInvoices.add(invoice3);

    //when
    Iterable<Invoice> actualInvoices = database.findAllBySellerName(sellerName);
    List<Invoice> result = new ArrayList<>();
    actualInvoices.forEach(result::add);

    //then
    assertArrayEquals(expectedInvoices.toArray(), result.toArray());
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

    database.save(invoice1);
    database.save(invoice2);
    database.save(invoice3);
    database.save(invoice4);
    database.save(invoice5);
    database.save(invoice6);

    List<Invoice> expectedInvoices = new ArrayList<>();
    expectedInvoices.add(invoice1);
    expectedInvoices.add(invoice2);
    expectedInvoices.add(invoice3);

    //when
    Iterable<Invoice> actualInvoices = database.findAllByBuyerName(buyerName);
    List<Invoice> result = new ArrayList<>();
    actualInvoices.forEach(result::add);

    //then
    assertArrayEquals(expectedInvoices.toArray(), result.toArray());
  }

  @Test
  void shouldUpdateExistingInvoice() throws DatabaseOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    database.save(invoice1);
    Invoice invoice1Update = InvoiceGenerator.getRandomInvoiceWithSpecificId(invoice1.getId());

    //when
    database.save(invoice1Update);
    Optional updatedInvoiceFromDatabase = database.findById(invoice1.getId());
    Invoice resultAfterUpdate = (Invoice) updatedInvoiceFromDatabase.get();

    //then
    assertEquals(invoice1.getId(), resultAfterUpdate.getId());
    assertNotEquals(invoice1, invoice1Update);
    assertEquals(invoice1Update, resultAfterUpdate);
  }

  @Test
  void shouldThrowExceptionIfMethodExistByIdInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> database.existsById(null));
  }

  @Test
  void shouldThrowExceptionIfMethodSaveInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> database.save(null));
  }

  @Test
  void shouldThrowExceptionIfMethodFindByIdIsInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> database.findById(null));
  }

  @Test
  void shouldThrowExceptionIfMethodDeleteByIdIsInvokedWithNull() {
    assertThrows(IllegalArgumentException.class, () -> database.deleteById(null));
  }

  @Test
  void shouldThrowExceptionIfMethodFindAllInvoicesBySellerNameInvokedWithNull() {
    assertThrows(IllegalArgumentException.class,
        () -> database.findAllBySellerName(null));
  }

  @Test
  void shouldThrowExceptionIfMethodFindAllInvoicesByBuyerNameInvokedWithNull() {
    assertThrows(IllegalArgumentException.class,
        () -> database.findAllByBuyerName(null));
  }
}
