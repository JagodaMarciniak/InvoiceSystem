package pl.coderstrust.database.memory;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;


class InMemoryDatabaseTest {
  Database testDatabase;
  InvoiceGenerator randomInvoiceGenerator;

  @BeforeEach
  public void setup() {
    testDatabase = new InMemoryDatabase();
    randomInvoiceGenerator = new InvoiceGenerator();
  }

  @Test
  void shouldReturnTrueIfInvoiceExistsInDatabase() throws DatabaseOperationException {
    //given
    Invoice invoice = randomInvoiceGenerator.getRandomInvoice();

    //when
    testDatabase.saveInvoice(invoice);

    //then
    assertTrue(testDatabase.invoiceExists(invoice.getId()));
  }

  @Test
  void shouldReturnFalseIfInvoiceNotExistsInDatabase() throws DatabaseOperationException {
    //given
    Invoice invoice1 = randomInvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = randomInvoiceGenerator.getRandomInvoice();

    //when
    testDatabase.saveInvoice(invoice1);

    //then
    assertFalse(testDatabase.invoiceExists(invoice2.getId()));
  }

  @Test
  void shouldSaveInvoiceIntoDatabase() throws DatabaseOperationException {
    //given
    Invoice expectedInvoice1 = randomInvoiceGenerator.getRandomInvoice();

    //when
    testDatabase.saveInvoice(expectedInvoice1);

    //then
    assertTrue(testDatabase.invoiceExists(expectedInvoice1.getId()));
  }

  @Test
  void shouldDeleteInvoiceFromDatabaseIfPresent() throws DatabaseOperationException {
    //given
    Invoice invoice = randomInvoiceGenerator.getRandomInvoice();
    testDatabase.saveInvoice(invoice);

    //when
    testDatabase.deleteInvoice(invoice.getId());

    //then
    assertFalse(testDatabase.invoiceExists(invoice.getId()));
  }

  @Test
  void shouldDeleteInvoiceFromDatabaseIfAbsent() throws DatabaseOperationException {
    //given
    String invoiceId = "1";

    //when
    testDatabase.deleteInvoice(invoiceId);

    //then
    assertFalse(testDatabase.invoiceExists(invoiceId));
  }

  @Test
  void shouldTestCountingInvoicesInDatabase() throws DatabaseOperationException {
    //given
    Long expectedNumberOfInvoices = Long.valueOf(5);

    //when
    for (int i = 0; i < expectedNumberOfInvoices; i++) {
      testDatabase.saveInvoice(randomInvoiceGenerator.getRandomInvoice());
    }
    //then
    assertEquals(expectedNumberOfInvoices, testDatabase.countInvoices());
  }


  @Test
  void shouldFindOneInvoice() throws DatabaseOperationException {
    //given
    Invoice invoice1 = randomInvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = randomInvoiceGenerator.getRandomInvoice();

    //when
    testDatabase.saveInvoice(invoice1);
    testDatabase.saveInvoice(invoice2);

    //then
    assertEquals(invoice1, testDatabase.findOneInvoice(invoice1.getId()));
    assertEquals(invoice2, testDatabase.findOneInvoice(invoice2.getId()));
  }

  @Test
  void shouldFindAllInvoices() throws DatabaseOperationException {
    //given
    List<Invoice> generatedInvoiceList = new ArrayList<>();

    Invoice invoice1 = randomInvoiceGenerator.getRandomInvoice();
    generatedInvoiceList.add(invoice1);
    testDatabase.saveInvoice(invoice1);

    Invoice invoice2 = randomInvoiceGenerator.getRandomInvoice();
    generatedInvoiceList.add(invoice2);
    testDatabase.saveInvoice(invoice2);

    //when
    List<Invoice> actualInvoiceList = testDatabase.findAllInvoices();

    //then
    assertArrayEquals(generatedInvoiceList.toArray(), actualInvoiceList.toArray());
  }

  @Test
  void shouldFindAllInvoicesBySellerName() throws DatabaseOperationException {
    //given
    String sellerName = "sampleSellerABC";
    Invoice invoiceA = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(sellerName);
    Invoice invoiceB = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(sellerName);
    Invoice invoiceC = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(sellerName);

    Invoice invoiceD = InvoiceGenerator.getRandomInvoice();
    Invoice invoiceE = InvoiceGenerator.getRandomInvoice();
    Invoice invoiceF = InvoiceGenerator.getRandomInvoice();

    testDatabase.saveInvoice(invoiceA);
    testDatabase.saveInvoice(invoiceB);
    testDatabase.saveInvoice(invoiceC);
    testDatabase.saveInvoice(invoiceD);
    testDatabase.saveInvoice(invoiceE);
    testDatabase.saveInvoice(invoiceF);

    List<Invoice> expectedInvoiceListSameSeller = new ArrayList<>();
    expectedInvoiceListSameSeller.add(invoiceA);
    expectedInvoiceListSameSeller.add(invoiceB);
    expectedInvoiceListSameSeller.add(invoiceC);

    //when
    List<Invoice> actualInvoiceList = testDatabase.findAllInvoicesBySellerName(sellerName);

    //then
    assertArrayEquals(expectedInvoiceListSameSeller.toArray(), actualInvoiceList.toArray());
  }

  @Test
  void findAllInvoicesByBuyerName() throws DatabaseOperationException {
    //given
    String buyerName = "sampleBuyerABC";
    Invoice invoiceA = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName(buyerName);
    Invoice invoiceB = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName(buyerName);
    Invoice invoiceC = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName(buyerName);

    Invoice invoiceD = InvoiceGenerator.getRandomInvoice();
    Invoice invoiceE = InvoiceGenerator.getRandomInvoice();
    Invoice invoiceF = InvoiceGenerator.getRandomInvoice();

    testDatabase.saveInvoice(invoiceA);
    testDatabase.saveInvoice(invoiceB);
    testDatabase.saveInvoice(invoiceC);
    testDatabase.saveInvoice(invoiceD);
    testDatabase.saveInvoice(invoiceE);
    testDatabase.saveInvoice(invoiceF);

    List<Invoice> expectedInvoiceListSameBuyer = new ArrayList<>();
    expectedInvoiceListSameBuyer.add(invoiceA);
    expectedInvoiceListSameBuyer.add(invoiceB);
    expectedInvoiceListSameBuyer.add(invoiceC);

    //when
    List<Invoice> actualInvoiceList = testDatabase.findAllInvoicesByBuyerName(buyerName);

    //then
    assertArrayEquals(expectedInvoiceListSameBuyer.toArray(), actualInvoiceList.toArray());
  }

  @Test
  public void shouldVerifyUpdateOfInvoice() throws DatabaseOperationException {
    //given
    Invoice invoiceA = InvoiceGenerator.getRandomInvoice();
    Invoice invoiceAUpdate = InvoiceGenerator.getRandomInvoiceWithSpecificId(invoiceA.getId());
    assertEquals(invoiceA.getId(), invoiceAUpdate.getId());
    assertNotEquals(invoiceA, invoiceAUpdate);

    testDatabase.saveInvoice(invoiceA);
    assertTrue(testDatabase.findOneInvoice(invoiceA.getId()) == invoiceA);

    //when
    testDatabase.saveInvoice(invoiceAUpdate);

    //then
    assertEquals(invoiceA.getId(), invoiceAUpdate.getId());
  }
}