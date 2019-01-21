package pl.coderstrust.model.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.coderstrust.model.AccountNumber;

class AccountNumberValidatorTest {
  private static String validIbanNumber = "FR11111122233334455667777789";
  private static String validLocalNumber = validIbanNumber.substring(2);

  @ParameterizedTest
  @MethodSource(value = "argumentsForIbanNumberValidationTest")
  void validateIbanNumber(AccountNumber accountNumber, List<String> expectedResult) {
    assertEquals(expectedResult, AccountNumberValidator.validateAccountNumber(accountNumber));
  }

  private static Stream<Arguments> argumentsForIbanNumberValidationTest() {
    return Stream.of(
        Arguments.of(new AccountNumber(null, validLocalNumber), Collections.singletonList("Iban number cannot be null")),
        Arguments.of(new AccountNumber("", validLocalNumber), Collections.singletonList("Iban number cannot be empty")),
        Arguments.of(new AccountNumber("ABC123", validLocalNumber), Collections.singletonList("Iban number is invalid"))
    );
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForLocalNumberValidationTest")
  void validateLocalNumber(AccountNumber accountNumber, List<String> expectedResult) {
    assertEquals(expectedResult, AccountNumberValidator.validateAccountNumber(accountNumber));
  }

  private static Stream<Arguments> argumentsForLocalNumberValidationTest() {
    return Stream.of(
        Arguments.of(new AccountNumber(validIbanNumber, null), Collections.singletonList("Local number cannot be null")),
        Arguments.of(new AccountNumber(validIbanNumber, ""), Collections.singletonList("Local number cannot be empty")),
        Arguments.of(new AccountNumber(validIbanNumber, "ABC123"), Collections.singletonList("Local number is invalid"))
    );
  }

  @Test
  void validateWhenIbanAndLocalNumberDoNotFit() {
    // Given
    List<String> expectedResult = Collections.singletonList("Iban number and local number do not fit");
    AccountNumber accountNumber = new AccountNumber(validIbanNumber, validLocalNumber.replace("1", "3"));

    // Then
    assertEquals(expectedResult, AccountNumberValidator.validateAccountNumber(accountNumber));
  }

  @Test
  void validateIfAccountNumberIsNull() {
    //given
    List<String> expectedResult = Collections.singletonList("Account number cannot be null");

    //then
    assertEquals(expectedResult, AccountNumberValidator.validateAccountNumber(null));
  }
}
