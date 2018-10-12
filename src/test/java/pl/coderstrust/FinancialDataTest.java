package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FinancialDataTest {

  @ParameterizedTest
  @ValueSource(strings = {"Sample Wide, Sample, Sample S.p z o.o"})
  public void testForValidCompanyName(String givenCompanyName) {
    //given
    FinancialData financialData = new FinancialData(givenCompanyName, "Example", "Example");

    //when
    String result = financialData.getCompanyName();

    //then
    assertSame(givenCompanyName, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"725-18-01-126, 8451769793, 851-26-24-854"})
  public void testForValidTaxIdentificationNumber(String givenTaxIdentificationNumber) {
    //given
    FinancialData financialData = new FinancialData("Example", givenTaxIdentificationNumber, "Example");

    //when
    String result = financialData.getTaxIdentificationNumber();

    //then
    assertSame(givenTaxIdentificationNumber, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"2121 1009 0000 0002 3569 8741", "212110090000000235698741"})
  public void testForValidBankAccountNumber(String givenBankAccountNumber) {
    //given
    FinancialData financialData = new FinancialData("Example", "Example", givenBankAccountNumber);

    //when
    String result = financialData.getBankAccountNumber();

    //then
    assertSame(givenBankAccountNumber, result);
  }

  @Test
  public void testForExceptionWhenCompanyNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      FinancialData financialData = new FinancialData(null, "Example", "Example");
    });
  }

  @Test
  public void testForExceptionWhenTaxIdentificationNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      FinancialData financialData = new FinancialData("Example", null, "Example");
    });
  }

  @Test
  public void testForExceptionWhenBankAccountNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      FinancialData financialData = new FinancialData("Example", "Example", null);
    });
  }
}
