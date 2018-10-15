package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FinancialDataTest {

  @ParameterizedTest
  @ValueSource(strings = {"725-18-01-126, 8451769793, 851-26-24-854"})
  public void testValidTaxIdentificationNumber(String givenTaxIdentificationNumber) {
    //given
    FinancialData financialData = new FinancialData(givenTaxIdentificationNumber, "Test");

    //when
    String result = financialData.getTaxId();

    //then
    assertEquals(givenTaxIdentificationNumber, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"2121 1009 0000 0002 3569 8741", "212110090000000235698741"})
  public void testValidBankAccountNumber(String givenBankAccountNumber) {
    //given
    FinancialData financialData = new FinancialData("Test", givenBankAccountNumber);

    //when
    String result = financialData.getAccountNumber();

    //then
    assertEquals(givenBankAccountNumber, result);
  }

  @Test
  public void testExceptionWhenTaxIdentificationNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      FinancialData financialData = new FinancialData(null, "Test");
    });
  }

  @Test
  public void testExceptionWhenBankAccountNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      FinancialData financialData = new FinancialData("Test", null);
    });
  }
}
