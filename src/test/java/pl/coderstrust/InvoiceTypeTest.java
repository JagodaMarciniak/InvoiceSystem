package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.coderstrust.InvoiceType.DEBIT_MEMO;
import static pl.coderstrust.InvoiceType.PRO_FORMA;
import static pl.coderstrust.InvoiceType.STANDARD;

import org.junit.jupiter.api.Test;

class InvoiceTypeTest {

  @Test
  void shouldReturnExpectedStringWhenGetValueOnStandardInvoked() {
    assertEquals("Standard", STANDARD.getValue());
  }

  @Test
  void shouldReturnExpectedStringWhenGetValueOnProFormaInvoked() {
    assertEquals("Pro-forma", PRO_FORMA.getValue());
  }

  @Test
  void shouldReturnExpectedStringWhenGetValueOnDebitMemoInvoked() {
    assertEquals("Debit memo", DEBIT_MEMO.getValue());
  }
}
