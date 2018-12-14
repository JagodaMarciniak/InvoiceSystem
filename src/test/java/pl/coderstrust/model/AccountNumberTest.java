package pl.coderstrust.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


class AccountNumberTest {

  @Test
  public void checkFullyInitialization() {
    //given
    String ibanNumber = "PL83620519463926400000847295";
    String expectedLocalNumber = "83620519463926400000847295";

    //when
    AccountNumber accountNumber = new AccountNumber(ibanNumber);

    //then
    assertEquals(ibanNumber, accountNumber.getIbanNumber());
    assertEquals(expectedLocalNumber, accountNumber.getLocalNumber());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "",
      "123",
      "          ",
      "12345612345678901234567890",
      "9123fsdkf1-329",
      "pl83620519463926400000847295"})
  public void shouldThrowExceptionWhenIbanNumberIsIncorrect(String ibanNumber) {
    assertThrows(IllegalArgumentException.class, () -> {
      new AccountNumber(ibanNumber);
    });
  }
}
