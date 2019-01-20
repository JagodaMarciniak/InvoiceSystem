package pl.coderstrust.model.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.coderstrust.generators.AccountNumberGenerator;
import pl.coderstrust.generators.ContactDetailsGenerator;
import pl.coderstrust.model.AccountNumber;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.ContactDetails;

class CompanyValidatorTest {
  private static String validName = "CD Projekt";
  private static String validTaxIdentificationNumber = "ABC123abc123";
  private static AccountNumber validAccountNumber =  AccountNumberGenerator.getSampleAccountNumber();
  private static ContactDetails validContactDetails = ContactDetailsGenerator.getSampleContactDetails();

  @ParameterizedTest
  @MethodSource(value = "argumentsForNameValidationTest")
  void validateName(Company company, List<String> expectedResult) {
    assertEquals(expectedResult, CompanyValidator.validateCompany(company));
  }

  private static Stream<Arguments> argumentsForNameValidationTest() {
    return Stream.of(
        Arguments.of(new Company(null, validTaxIdentificationNumber, validAccountNumber, validContactDetails), Collections.singletonList("Name cannot be null")),
        Arguments.of(new Company("", validTaxIdentificationNumber, validAccountNumber, validContactDetails), Collections.singletonList("Name cannot be empty")),
        Arguments.of(new Company(validName, validTaxIdentificationNumber, validAccountNumber, validContactDetails), Collections.emptyList())
    );
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForTaxIdentificationNumberValidationTest")
  void validateTaxIdentificationNumber(Company company, List<String> expectedResult) {
    assertEquals(expectedResult, CompanyValidator.validateCompany(company));
  }

  private static Stream<Arguments> argumentsForTaxIdentificationNumberValidationTest() {
    return Stream.of(
        Arguments.of(new Company(validName, null , validAccountNumber, validContactDetails), Collections.singletonList("Tax identification number cannot be null")),
        Arguments.of(new Company(validName, "", validAccountNumber, validContactDetails), Collections.singletonList("Tax identification number cannot be null")),
        Arguments.of(new Company(validName, validTaxIdentificationNumber, validAccountNumber, validContactDetails), Collections.emptyList())
    );
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForAccountNumberValidationTest")
  void validateAccountNumber(Company company, List<String> expectedResult) {
    assertEquals(expectedResult, CompanyValidator.validateCompany(company));
  }

  private static Stream<Arguments> argumentsForAccountNumberValidationTest() {
    return Stream.of(
        Arguments.of(new Company(validName, validTaxIdentificationNumber, null, validContactDetails), Collections.singletonList("Account number cannot be null")),
        Arguments.of(new Company(validName, validTaxIdentificationNumber, validAccountNumber, validContactDetails), Collections.emptyList())
    );
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForContactDetailsValidationTest")
  void validateContactDetails(Company company, List<String> expectedResult) {
    assertEquals(expectedResult, CompanyValidator.validateCompany(company));
  }

  private static Stream<Arguments> argumentsForContactDetailsValidationTest() {
    return Stream.of(
        Arguments.of(new Company(validName, validTaxIdentificationNumber, validAccountNumber, null), Collections.singletonList("Contact details cannot be null")),
        Arguments.of(new Company(validName, validTaxIdentificationNumber, validAccountNumber, validContactDetails), Collections.emptyList())
    );
  }
}