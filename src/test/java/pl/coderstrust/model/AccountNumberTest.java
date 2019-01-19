package pl.coderstrust.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AccountNumberTest {

  @Test
  public void checkFullyInitialization() {
    //given
    String ibanNumber = "PL83620519463926400000847295";
    String localNumber = ibanNumber.substring(2);

    //when
    AccountNumber accountNumber = new AccountNumber(ibanNumber, localNumber);

    //then
    assertEquals(ibanNumber, accountNumber.getIbanNumber());
    assertEquals(localNumber, accountNumber.getLocalNumber());
  }
}
