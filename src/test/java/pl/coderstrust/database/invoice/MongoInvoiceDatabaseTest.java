package pl.coderstrust.database.invoice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pl.coderstrust.configuration.MongoDatabaseProperties;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

@ExtendWith(MockitoExtension.class)
class MongoInvoiceDatabaseTest {

  private InvoiceDatabase mongoInvoiceDatabase;
  private MongoDatabaseProperties properties = new MongoDatabaseProperties();

  @Mock
  private MongoTemplate mongoTemplate;

  @Mock
  private MongoCollection mongoCollection;

  @Mock
  private DeleteResult deleteResult;

  @BeforeEach
  void setUp() {
    mongoInvoiceDatabase = new MongoInvoiceDatabase(mongoTemplate, properties);
  }

  @Test
  void shouldSaveInvoiceToDatabase() throws DatabaseOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoiceWithNoId();

    //when
    Invoice actual = mongoInvoiceDatabase.save(invoice);

    //then
    assertEquals(invoice, actual);
    verify(mongoTemplate).save(invoice, properties.getCollectionName());
  }

  @Test
  void shouldReturnFalseIfInvoiceNotExistsInDatabase() throws DatabaseOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(mongoInvoiceDatabase.existsById(invoice.getId())).thenReturn(false);

    //when
    boolean existsById = mongoInvoiceDatabase.existsById(invoice.getId());

    //then
    assertFalse(existsById);
    verify(mongoTemplate).exists(Query.query(Criteria.where("_id").is(invoice.getId())), Invoice.class, properties.getCollectionName());
  }

  @Test
  void shouldReturnTrueIfInvoiceExistsInDatabase() throws DatabaseOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    when(mongoInvoiceDatabase.existsById(invoice.getId())).thenReturn(true);

    //when
    boolean existById = mongoInvoiceDatabase.existsById(invoice.getId());

    //then
    assertTrue(existById);
    verify(mongoTemplate).exists(Query.query(Criteria.where("_id").is(invoice.getId())), Invoice.class, properties.getCollectionName());
  }

  @Test
  void shouldDeleteAllInvoicesInDatabase() throws DatabaseOperationException {
    //when
    when(mongoTemplate.getCollection(properties.getCollectionName())).thenReturn(mongoCollection);
    when(mongoCollection.deleteMany(new Document())).thenReturn(deleteResult);

    //when
    mongoInvoiceDatabase.deleteAll();

    //then
    verify(mongoTemplate).getCollection(properties.getCollectionName());
    verify(mongoCollection).deleteMany(new Document());
  }

  @Test
  void shouldCountInvoicesInDatabase() throws DatabaseOperationException {
    //given
    when(mongoTemplate.getCollection(properties.getCollectionName())).thenReturn(mongoCollection);
    when(mongoCollection.count()).thenReturn(5L);

    //when
    long actualNumberOfInvoices = mongoInvoiceDatabase.count();

    //then
    assertEquals(5L, actualNumberOfInvoices);
    verify(mongoTemplate).getCollection(properties.getCollectionName());
    verify(mongoCollection).count();
  }

  @Test
  void shouldFindInvoice() throws DatabaseOperationException {
    //given
    String id = "d823bd11-0ba5-4474-a2dc-810ae027d7c1";
    Invoice invoice = InvoiceGenerator.getRandomInvoiceWithSpecificId(id);
    when(mongoTemplate.findById(id, Invoice.class, properties.getCollectionName())).thenReturn(invoice);

    //when
    Optional result = mongoInvoiceDatabase.findById("d823bd11-0ba5-4474-a2dc-810ae027d7c1");

    //then
    assertEquals(invoice, result.get());
    verify(mongoTemplate).findById(id, Invoice.class, properties.getCollectionName());
  }

  @Test
  void shouldFindAllInvoices() throws DatabaseOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice3 = InvoiceGenerator.getRandomInvoice();
    when(mongoTemplate.findAll(Invoice.class, properties.getCollectionName())).thenReturn(Arrays.asList(invoice1, invoice2, invoice3));

    //when
    Iterable<Invoice> result = mongoInvoiceDatabase.findAll();

    //then
    Iterator<Invoice> iterator = result.iterator();
    assertEquals(invoice1, iterator.next());
    assertEquals(invoice2, iterator.next());
    assertEquals(invoice3, iterator.next());
    verify(mongoTemplate).findAll(Invoice.class, properties.getCollectionName());
  }

  @Test
  void shouldFindAllInvoicesBySellerName() throws DatabaseOperationException {
    //given
    String sellerName = "SampleSeller";
    Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(sellerName);
    Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(sellerName);
    when(mongoTemplate.find(Query.query(Criteria.where("sellerName").is(sellerName)), Invoice.class, properties.getCollectionName()))
        .thenReturn(Arrays.asList(invoice1, invoice2));

    //when
    Iterable<Invoice> result = mongoInvoiceDatabase.findAllBySellerName(sellerName);

    //then
    Iterator<Invoice> expectedInvoice = result.iterator();
    assertEquals(invoice1, expectedInvoice.next());
    assertEquals(invoice2, expectedInvoice.next());
    verify(mongoTemplate).find(Query.query(Criteria.where("sellerName").is(sellerName)), Invoice.class, properties.getCollectionName());
  }

  @Test
  void shouldFindAllInvoicesByBuyerName() throws DatabaseOperationException {
    //given
    String buyerName = "buyerName";
    Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(buyerName);
    Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName(buyerName);
    when(mongoTemplate.find(Query.query(Criteria.where("sellerName").is(buyerName)), Invoice.class, properties.getCollectionName()))
        .thenReturn(Arrays.asList(invoice1, invoice2));

    //when
    Iterable<Invoice> result = mongoInvoiceDatabase.findAllBySellerName(buyerName);

    //then
    Iterator<Invoice> expectedInvoice = result.iterator();
    assertEquals(invoice1, expectedInvoice.next());
    assertEquals(invoice2, expectedInvoice.next());
    verify(mongoTemplate).find(Query.query(Criteria.where("sellerName").is(buyerName)), Invoice.class, properties.getCollectionName());
  }

  @Test
  void saveMethodShouldThrowExceptionWhenErrorOccursDuringExecution() {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoice();
    doThrow(MongoException.class).when(mongoTemplate).save(invoice, properties.getCollectionName());

    //then
    assertThrows(DatabaseOperationException.class, () -> mongoInvoiceDatabase.save(invoice));
    verify(mongoTemplate).save(invoice, properties.getCollectionName());
  }

  @Test
  void findByIdMethodShouldThrowExceptionWhenErrorOccursDuringExecution() {
    //given
    String id = "d823bd11-0ba5-4474-a2dc-810ae027d7c1";
    doThrow(MongoException.class).when(mongoTemplate).findById(id, Invoice.class, properties.getCollectionName());

    //then
    assertThrows(DatabaseOperationException.class, () -> mongoInvoiceDatabase.findById(id));
    verify(mongoTemplate).findById(id, Invoice.class, properties.getCollectionName());
  }

  @Test
  void existByIdMethodShouldThrowExceptionWhenErrorOccursDuringExecution() {
    //given
    String id = "d823bd11-0ba5-4474-a2dc-810ae027d7c1";
    doThrow(MongoException.class).when(mongoTemplate).exists(Query.query(Criteria.where("_id").is(id)), Invoice.class,
        properties.getCollectionName());

    //then
    assertThrows(DatabaseOperationException.class, () -> mongoInvoiceDatabase.existsById(id));
    verify(mongoTemplate).exists(Query.query(Criteria.where("_id").is(id)), Invoice.class, properties.getCollectionName());
  }

  @Test
  void findAllMethodShouldThrowExceptionWhenErrorOccursDuringExecution() {
    //given
    doThrow(MongoException.class).when(mongoTemplate).findAll(Invoice.class, properties.getCollectionName());

    //then
    assertThrows(DatabaseOperationException.class, () -> mongoInvoiceDatabase.findAll());
    verify(mongoTemplate).findAll(Invoice.class, properties.getCollectionName());
  }

  @Test
  void countMethodShouldThrowExceptionWhenErrorOccursDuringExecution() {
    //given
    when(mongoTemplate.getCollection(properties.getCollectionName())).thenReturn(mongoCollection);
    doThrow(MongoException.class).when(mongoCollection).count();

    //then
    assertThrows(DatabaseOperationException.class, () -> mongoInvoiceDatabase.count());
    verify(mongoTemplate).getCollection(properties.getCollectionName());
    verify(mongoCollection).count();
  }

  @Test
  void deleteByIdMethodShouldThrowExceptionWhenErrorOccursDuringExecution() {
    //given
    String id = "33";
    doThrow(MongoException.class).when(mongoTemplate).findAndRemove(Query.query(Criteria.where("_id").is(id)), Invoice.class,
        properties.getCollectionName());

    //then
    assertThrows(DatabaseOperationException.class, () -> mongoInvoiceDatabase.deleteById(id));
    verify(mongoTemplate).findAndRemove(Query.query(Criteria.where("_id").is(id)), Invoice.class, properties.getCollectionName());
  }

  @Test
  void deleteAllMethodShouldThrowExceptionWhenErrorOccursDuringExecution() {
    //given
    when(mongoTemplate.getCollection(properties.getCollectionName())).thenReturn(mongoCollection);
    doThrow(MongoException.class).when(mongoCollection).deleteMany(new Document());

    //then
    assertThrows(DatabaseOperationException.class, () -> mongoInvoiceDatabase.deleteAll());
    verify(mongoTemplate).getCollection(properties.getCollectionName());
    verify(mongoCollection).deleteMany(new Document());
  }

  @Test
  void findBySellerNameMethodShouldThrowExceptionWhenErrorOccursDuringExecution() {
    //given
    String sellerName = "SampleSellerName";
    doThrow(MongoException.class).when(mongoTemplate).find(Query.query(Criteria.where("sellerName").is(sellerName)), Invoice.class,
        properties.getCollectionName());

    //then
    assertThrows(DatabaseOperationException.class, () -> mongoInvoiceDatabase.findAllBySellerName(sellerName));
    verify(mongoTemplate).find(Query.query(Criteria.where("sellerName").is(sellerName)), Invoice.class, properties.getCollectionName());
  }

  @Test
  void findByBuyerNameMethodShouldThrowExceptionWhenErrorOccursDuringExecution() {
    //given
    String buyerName = "SampleBuyerName";
    doThrow(MongoException.class).when(mongoTemplate).find(Query.query(Criteria.where("buyerName").is(buyerName)), Invoice.class,
        properties.getCollectionName());

    //then
    assertThrows(DatabaseOperationException.class, () -> mongoInvoiceDatabase.findAllByBuyerName(buyerName));
    verify(mongoTemplate).find(Query.query(Criteria.where("buyerName").is(buyerName)), Invoice.class, properties.getCollectionName());
  }

  @Test
  void saveMethodShouldThrowExceptionWhenPassedArgumentIsNull() {
    assertThrows(IllegalArgumentException.class, () -> mongoInvoiceDatabase.save(null));
  }

  @Test
  void findByIdMethodShouldThrowExceptionWhenPassedArgumentIsNull() {
    assertThrows(IllegalArgumentException.class, () -> mongoInvoiceDatabase.findById(null));
  }

  @Test
  void existByIdMethodShouldThrowExceptionWhenPassedArgumentIsNull() {
    assertThrows(IllegalArgumentException.class, () -> mongoInvoiceDatabase.existsById(null));
  }

  @Test
  void deleteByIdMethodShouldThrowExceptionWhenPassedArgumentIsNull() {
    assertThrows(IllegalArgumentException.class, () -> mongoInvoiceDatabase.deleteById(null));
  }

  @Test
  void findAllBySellerNameMethodShouldThrowExceptionWhenPassedArgumentIsNull() {
    assertThrows(IllegalArgumentException.class, () -> mongoInvoiceDatabase.findAllBySellerName(null));
  }

  @Test
  void findAllByBuyerNameMethodShouldThrowExceptionWhenPassedArgumentIsNull() {
    assertThrows(IllegalArgumentException.class, () -> mongoInvoiceDatabase.findAllByBuyerName(null));
  }
}
