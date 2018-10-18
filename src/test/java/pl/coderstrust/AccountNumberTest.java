package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AccountNumberTest {

  @Test
  public void checkFullyInitialization() {
    //given
    String ibanNumber = "PL83620519463926400000847295";

    //when
    AccountNumber accountNumber = new AccountNumber(ibanNumber);
    String expectedLocalNumber = "83620519463926400000847295";

    //then
    assertEquals(ibanNumber, accountNumber.getIban());
    assertEquals(expectedLocalNumber, accountNumber.getLocalNumber());
  }

  @Test
  public void shouldThrowExceptionWhenIbanNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new AccountNumber(null);
    });
  }
}
