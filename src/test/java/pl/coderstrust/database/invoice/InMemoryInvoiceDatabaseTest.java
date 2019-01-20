package pl.coderstrust.database.invoice;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    Invoice addedInvoice = database.save(invoice);

    //when
    boolean existsById = database.existsById(addedInvoice.getId());

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
    Invoice addedInvoice = database.save(expectedInvoice1);
    boolean existsById = database.existsById(addedInvoice.getId());

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
    Invoice addedInvoice = database.save(invoice);

    //when
    database.deleteById(addedInvoice.getId());
    boolean existsById = database.existsById(addedInvoice.getId());

    //then
    assertFalse(existsById);
  }

  @Test
  void shouldThrowExceptionIfMethodDeleteByIdIsInvokedForNonExistingInvoice() {
    assertThrows(DatabaseOperationException.class, () -> database.deleteById("1"));
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
    Invoice addedInvoice1 = database.save(invoice1);
    Invoice addedInvoice2 = database.save(invoice2);

    //when
    Optional invoiceFromDatabase1 = database.findById(addedInvoice1.getId());
    Optional invoiceFromDatabase2 = database.findById(addedInvoice2.getId());

    //then
    assertEquals(addedInvoice1, invoiceFromDatabase1.get());
    assertEquals(addedInvoice2, invoiceFromDatabase2.get());
  }

  @Test
  void shouldFindAllInvoices() throws DatabaseOperationException {
    //given
    List<Invoice> addedInvoices = new ArrayList<>();
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    Invoice addedInvoice1 = database.save(invoice1);
    Invoice addedInvoice2 = database.save(invoice2);
    addedInvoices.add(addedInvoice1);
    addedInvoices.add(addedInvoice2);


    //when
    Iterable<Invoice> actualInvoices = database.findAll();
    List<Invoice> result = new ArrayList<>();
    actualInvoices.forEach(result::add);

    //then
    assertArrayEquals(addedInvoices.toArray(), result.toArray());
  }

  @Test
  void shouldFindAllInvoicesBySellerName() throws DatabaseOperationException {
    //given
    List<Invoice> expectedInvoices = new ArrayList<>();
    String sellerName = "sampleSellerABC";
    Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(sellerName);
    Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(sellerName);
    Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(sellerName);

    Invoice addedInvoice1 = database.save(invoice1);
    expectedInvoices.add(addedInvoice1);
    Invoice addedInvoice2 = database.save(invoice2);
    expectedInvoices.add(addedInvoice2);
    Invoice addedInvoice3 = database.save(invoice3);
    expectedInvoices.add(addedInvoice3);
    database.save(InvoiceGenerator.getRandomInvoice());
    database.save(InvoiceGenerator.getRandomInvoice());

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
    List<Invoice> expectedInvoices = new ArrayList<>();
    String buyerName = "sampleBuyerABC";
    Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName(buyerName);
    Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName(buyerName);
    Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName(buyerName);

    Invoice addedInvoice1 = database.save(invoice1);
    expectedInvoices.add(addedInvoice1);
    Invoice addedInvoice2 = database.save(invoice2);
    expectedInvoices.add(addedInvoice2);
    Invoice addedInvoice3 = database.save(invoice3);
    expectedInvoices.add(addedInvoice3);
    database.save(InvoiceGenerator.getRandomInvoice());
    database.save(InvoiceGenerator.getRandomInvoice());

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
    Invoice addedInvoice = database.save(invoice1);
    Invoice invoiceToUpdate = InvoiceGenerator.getRandomInvoiceWithSpecificId(addedInvoice.getId());

    //when
    Invoice updatedInvoice = database.save(invoiceToUpdate);

    //then
    assertEquals(invoiceToUpdate, updatedInvoice);
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
