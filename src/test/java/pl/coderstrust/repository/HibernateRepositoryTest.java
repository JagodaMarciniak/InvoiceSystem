package pl.coderstrust.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.repository.invoice.HibernateInvoiceRepository;
import pl.coderstrust.repository.invoice.HibernateRepository;
import pl.coderstrust.repository.invoice.InvoiceRepository;

@ExtendWith(MockitoExtension.class)
class HibernateRepositoryTest {

  @Mock
  HibernateInvoiceRepository hibernateInvoiceRepository;

  private InvoiceRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    repository = new HibernateRepository(hibernateInvoiceRepository);
  }

  @Test
  void shouldSaveInvoiceToDatabase() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    when(hibernateInvoiceRepository.save(invoice1)).thenReturn(invoice2);

    //when
    Invoice actual = repository.save(invoice1);

    //then
    assertEquals(invoice2, actual);
    verify(hibernateInvoiceRepository).save(invoice1);
  }

  @Test
  void shouldReturnFalseIfInvoiceNotExistsInDatabase() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    when(hibernateInvoiceRepository.existsById(invoice1.getId())).thenReturn(false);

    //when
    boolean existById = repository.existsById(invoice1.getId());

    //then
    assertFalse(existById);
    verify(hibernateInvoiceRepository).existsById(invoice1.getId());
  }

  @Test
  @Transactional
  void shouldDeleteAllInvoicesInDatabase() throws RepositoryOperationException {
    //given
    doNothing().when(hibernateInvoiceRepository).deleteAll();

    //when
    repository.deleteAll();

    //then
    verify(hibernateInvoiceRepository, atLeast(1)).deleteAll();
  }

  @Test
  void shouldCountingInvoicesInDatabase() throws RepositoryOperationException {
    //given
    when(hibernateInvoiceRepository.count()).thenReturn(0L);

    //when
    long actualNumberOfInvoices = repository.count();

    //then
    assertEquals(0, actualNumberOfInvoices);
    verify(hibernateInvoiceRepository).count();
  }

  @Test
  @Transactional
  void shouldFindOneInvoice() throws RepositoryOperationException {
    //given
    Invoice invoice = InvoiceGenerator.getRandomInvoiceWithSpecificId("d823bd11-0ba5-4474-a2dc-810ae027d7c1");
    when(hibernateInvoiceRepository.findById("d823bd11-0ba5-4474-a2dc-810ae027d7c1")).thenReturn(Optional.of(invoice));

    //when
    Optional result = repository.findById("d823bd11-0ba5-4474-a2dc-810ae027d7c1");

    //then
    assertEquals(invoice, result.get());
    verify(hibernateInvoiceRepository).findById(invoice.getId());
  }

  @Test
  void shouldFindAllInvoices() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
    Invoice invoice3 = InvoiceGenerator.getRandomInvoice();
    when(hibernateInvoiceRepository.findAll()).thenReturn(Arrays.asList(invoice1, invoice2, invoice3));
    Iterable<Invoice> result;

    //when
    result = repository.findAll();
    Iterator<Invoice> iterator = result.iterator();


    assertEquals(invoice1, iterator.next());
    assertEquals(invoice2, iterator.next());
    assertEquals(invoice3, iterator.next());
    verify(hibernateInvoiceRepository).findAll();
  }

  @Test
  void shouldThrowExceptionWhenDeleteByIdAndRepositoryIsEmpty()throws RepositoryOperationException{
    assertThrows(RepositoryOperationException.class, ()-> repository.deleteById("3"));
  }

  @Test
  void shouldFindAllInvoicesBySellerName() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName("SampleSeller");
    Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerName("SampleSeller");
    when(hibernateInvoiceRepository.findAll()).thenReturn(Arrays.asList(invoice1, invoice2));

    //when
    Iterable<Invoice> result = repository.findAllBySellerName("SampleSeller");
    Iterator<Invoice> expectedInvoice = result.iterator();

    //
    assertEquals(expectedInvoice.next(), invoice1);
    assertEquals(expectedInvoice.next(), invoice2);
    verify(hibernateInvoiceRepository).findAll();
  }

  @Test
  void shouldFindAllInvoicesByBuyerName() throws RepositoryOperationException {
    //given
    Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName("SampleBuyer");
    Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerName("SampleBuyer");
    when(hibernateInvoiceRepository.findAll()).thenReturn(Arrays.asList(invoice1, invoice2));

    //when
    Iterable<Invoice> result = repository.findAllByBuyerName("SampleBuyer");
    Iterator<Invoice> expectedInvoice = result.iterator();

    //
    assertEquals(expectedInvoice.next(), invoice1);
    assertEquals(expectedInvoice.next(), invoice2);
    verify(hibernateInvoiceRepository).findAll();
  }
}
