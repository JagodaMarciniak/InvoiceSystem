package pl.coderstrust.database.invoice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

@ExtendWith(MockitoExtension.class)
class HibernateInvoiceDatabaseTest {

  @Mock
  HibernateInvoiceRepository hibernateInvoiceRepository;

  private InvoiceDatabase database;

  @BeforeEach
  void setUp() {
    database = new HibernateInvoiceDatabase(hibernateInvoiceRepository);
  }

  @Test
  void shouldSaveInvoiceToDatabase() throws DatabaseOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    when(hibernateInvoiceRepository.save(invoice1)).thenReturn(invoice2);

    //when
    Invoice actual = database.save(invoice1);

    //then
    assertEquals(invoice2, actual);
    verify(hibernateInvoiceRepository).save(invoice1);
  }

  @Test
  void shouldReturnFalseIfInvoiceNotExistsInDatabase() throws DatabaseOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    when(hibernateInvoiceRepository.existsById(invoice1.getId())).thenReturn(false);

    //when
    boolean existById = database.existsById(invoice1.getId());

    //then
    assertFalse(existById);
    verify(hibernateInvoiceRepository).existsById(invoice1.getId());
  }

  @Test
  void shouldReturnTrueIfInvoiceExistsInDatabase() throws DatabaseOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    when(hibernateInvoiceRepository.existsById(invoice1.getId())).thenReturn(true);

    //when
    boolean existById = database.existsById(invoice1.getId());

    //then
    assertTrue(existById);
    verify(hibernateInvoiceRepository).existsById(invoice1.getId());
  }

  @Test
  @Transactional
  void shouldDeleteAllInvoicesInDatabase() throws DatabaseOperationException {
    //given
    doNothing().when(hibernateInvoiceRepository).deleteAll();

    //when
    database.deleteAll();

    //then
    verify(hibernateInvoiceRepository).deleteAll();
  }

  @Test
  void shouldCountingInvoicesInDatabase() throws DatabaseOperationException {
    //given
    long numberOfInvoices = 5L;
    when(hibernateInvoiceRepository.count()).thenReturn(numberOfInvoices);

    //when
    long actualNumberOfInvoices = database.count();

    //then
    assertEquals(numberOfInvoices, actualNumberOfInvoices);
    verify(hibernateInvoiceRepository).count();
  }

  @Test
  @Transactional
  void shouldFindInvoice() throws DatabaseOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoiceWithSpecificId("d823bd11-0ba5-4474-a2dc-810ae027d7c1");
    when(hibernateInvoiceRepository.findById("d823bd11-0ba5-4474-a2dc-810ae027d7c1")).thenReturn(Optional.of(invoice));

    //when
    Optional result = database.findById("d823bd11-0ba5-4474-a2dc-810ae027d7c1");

    //then
    assertEquals(invoice, result.get());
    verify(hibernateInvoiceRepository).findById(invoice.getId());
  }

  @Test
  void shouldFindAllInvoices() throws DatabaseOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice3 = InvoiceGenerator.getRandomInvoice();
    when(hibernateInvoiceRepository.findAll()).thenReturn(Arrays.asList(invoice1, invoice2, invoice3));

    //when
    Iterable<Invoice> result = database.findAll();
    Iterator<Invoice> iterator = result.iterator();


    assertEquals(invoice1, iterator.next());
    assertEquals(invoice2, iterator.next());
    assertEquals(invoice3, iterator.next());
    verify(hibernateInvoiceRepository).findAll();
  }

  @Test
  void shouldFindAllInvoicesBySellerName() throws DatabaseOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName("SampleSeller");
    Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName("WrongSellerName");
    Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName("SampleSeller");
    when(hibernateInvoiceRepository.findAll()).thenReturn(Arrays.asList(invoice1, invoice2, invoice3));

    //when
    Iterable<Invoice> result = database.findAllBySellerName("SampleSeller");
    Iterator<Invoice> expectedInvoice = result.iterator();

    //
    assertEquals(invoice1, expectedInvoice.next());
    assertEquals(invoice3, expectedInvoice.next());
    verify(hibernateInvoiceRepository).findAll();
  }

  @Test
  void shouldFindAllInvoicesByBuyerName() throws DatabaseOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName("SampleBuyer");
    Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName("WrongBuyerName");
    Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName("SampleBuyer");
    when(hibernateInvoiceRepository.findAll()).thenReturn(Arrays.asList(invoice1, invoice2, invoice3));

    //when
    Iterable<Invoice> result = database.findAllByBuyerName("SampleBuyer");
    Iterator<Invoice> expectedInvoice = result.iterator();

    //
    assertEquals(invoice1, expectedInvoice.next());
    assertEquals(invoice3, expectedInvoice.next());
    verify(hibernateInvoiceRepository).findAll();
  }

  @Test
  void saveMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //given
    NonTransientDataAccessException mockedException = Mockito.mock(NonTransientDataAccessException.class);
    Invoice invoice = InvoiceGenerator.getRandomInvoice();

    //when
    doThrow(mockedException).when(hibernateInvoiceRepository).save(invoice);

    //then
    assertThrows(DatabaseOperationException.class, () -> database.save(invoice));
    verify(hibernateInvoiceRepository).save(invoice);
  }

  @Test
  void findByIdMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //given
    String id = "d823bd11-0ba5-4474-a2dc-810ae027d7c1";

    //when
    doThrow(NoSuchElementException.class).when(hibernateInvoiceRepository).findById(id);

    //then
    assertThrows(DatabaseOperationException.class, () -> database.findById(id));
    verify(hibernateInvoiceRepository).findById(id);
  }

  @Test
  void existByIdMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //given
    NonTransientDataAccessException mockedException = Mockito.mock(NonTransientDataAccessException.class);
    String id = "d823bd11-0ba5-4474-a2dc-810ae027d7c1";

    //when
    doThrow(mockedException).when(hibernateInvoiceRepository).existsById(id);

    //then
    assertThrows(DatabaseOperationException.class, () -> database.existsById(id));
    verify(hibernateInvoiceRepository).existsById(id);
  }

  @Test
  void findAllMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //given
    NonTransientDataAccessException mockedException = Mockito.mock(NonTransientDataAccessException.class);

    //when
    doThrow(mockedException).when(hibernateInvoiceRepository).findAll();

    //then
    assertThrows(DatabaseOperationException.class, () -> database.findAll());
    verify(hibernateInvoiceRepository).findAll();
  }

  @Test
  void countMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //given
    NonTransientDataAccessException mockedException = Mockito.mock(NonTransientDataAccessException.class);

    //when
    doThrow(mockedException).when(hibernateInvoiceRepository).count();

    //then
    assertThrows(DatabaseOperationException.class, () -> database.count());
    verify(hibernateInvoiceRepository).count();
  }

  @Test
  void deleteByIdMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //given
    String invoiceId = "33";

    //when
    doThrow(EmptyResultDataAccessException.class).when(hibernateInvoiceRepository).deleteById(invoiceId);

    //then
    assertThrows(DatabaseOperationException.class, () -> database.deleteById(invoiceId));
    verify(hibernateInvoiceRepository).deleteById(invoiceId);
  }

  @Test
  void deleteAllMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //given
    NonTransientDataAccessException mockedException = Mockito.mock(NonTransientDataAccessException.class);

    //when
    doThrow(mockedException).when(hibernateInvoiceRepository).deleteAll();

    //then
    assertThrows(DatabaseOperationException.class, () -> database.deleteAll());
    verify(hibernateInvoiceRepository).deleteAll();
  }

  @Test
  void findBySellerNameMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //given
    NonTransientDataAccessException mockedException = Mockito.mock(NonTransientDataAccessException.class);
    String name = "SampleSellerName";

    //when
    doThrow(mockedException).when(hibernateInvoiceRepository).findAll();

    //then
    assertThrows(DatabaseOperationException.class, () -> database.findAllBySellerName(name));
    verify(hibernateInvoiceRepository).findAll();
  }

  @Test
  void findByBuyerNameMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //given
    NonTransientDataAccessException mockedException = Mockito.mock(NonTransientDataAccessException.class);
    String name = "SampleBuyerName";

    //when
    doThrow(mockedException).when(hibernateInvoiceRepository).findAll();

    //then
    assertThrows(DatabaseOperationException.class, () -> database.findAllByBuyerName(name));
    verify(hibernateInvoiceRepository).findAll();
  }

  @Test
  void saveMethodShouldThrowExceptionWhenPassedArgumentIsNull() {
    assertThrows(IllegalArgumentException.class, () -> database.save(null));
  }

  @Test
  void findByIdMethodShouldThrowExceptionWhenPassedArgumentIsNull() {
    assertThrows(IllegalArgumentException.class, () -> database.findById(null));
  }

  @Test
  void existByIdMethodShouldThrowExceptionWhenPassedArgumentIsNull() {
    assertThrows(IllegalArgumentException.class, () -> database.existsById(null));
  }

  @Test
  void deleteByIdMethodShouldThrowExceptionWhenPassedArgumentIsNull() {
    assertThrows(IllegalArgumentException.class, () -> database.deleteById(null));
  }

  @Test
  void findAllBySellerNameMethodShouldThrowExceptionWhenPassedArgumentIsNull() {
    assertThrows(IllegalArgumentException.class, () -> database.findAllBySellerName(null));
  }

  @Test
  void findAllByBuyerNameMethodShouldThrowExceptionWhenPassedArgumentIsNull() {
    assertThrows(IllegalArgumentException.class, () -> database.findAllByBuyerName(null));
  }
}
