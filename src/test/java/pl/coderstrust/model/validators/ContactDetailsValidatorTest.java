package pl.coderstrust.model.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.coderstrust.generators.AddressGenerator;
import pl.coderstrust.model.Address;
import pl.coderstrust.model.ContactDetails;

class ContactDetailsValidatorTest {

  private static String validEmail = "email@email.pl";
  private static String validPhoneNumber = "12345680";
  private static String validWebsite = "http://www.abc.pl";
  private static Address validAddress = AddressGenerator.getSampleAddress();

  @ParameterizedTest
  @MethodSource(value = "argumentsForEmailValidationTest")
  void validateEmail(ContactDetails contactDetails, List<String> expectedResult) {
    assertEquals(expectedResult, ContactDetailsValidator.validateContactDetails(contactDetails));
  }

  private static Stream<Arguments> argumentsForEmailValidationTest() {
    return Stream.of(
        Arguments.of(new ContactDetails(null , validPhoneNumber, validWebsite, validAddress), Collections.singletonList("Email cannot be null")),
        Arguments.of(new ContactDetails("" , validPhoneNumber, validWebsite, validAddress), Collections.singletonList("Email cannot be empty")),
        Arguments.of(new ContactDetails(validEmail , validPhoneNumber, validWebsite, validAddress), Collections.emptyList())
    );
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForPhoneNumberValidationTest")
  void validatePhoneNumber(ContactDetails contactDetails, List<String> expectedResult) {
    System.out.println(validAddress);
    assertEquals(expectedResult, ContactDetailsValidator.validateContactDetails(contactDetails));
  }

  private static Stream<Arguments> argumentsForPhoneNumberValidationTest() {
    return Stream.of(
        Arguments.of(new ContactDetails(validEmail , null, validWebsite, validAddress), Collections.singletonList("Phone number cannot be null")),
        Arguments.of(new ContactDetails(validEmail, "", validWebsite, validAddress), Collections.singletonList("Phone number cannot be empty")),
        Arguments.of(new ContactDetails(validEmail , "abc123", validWebsite, validAddress), Collections.singletonList("Phone number can only be numbers")),
        Arguments.of(new ContactDetails(validEmail , validPhoneNumber, validWebsite, validAddress), Collections.emptyList())
    );
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForWebsiteValidationTest")
  void validateWebsite(ContactDetails contactDetails, List<String> expectedResult) {
    assertEquals(expectedResult, ContactDetailsValidator.validateContactDetails(contactDetails));
  }

  private static Stream<Arguments> argumentsForWebsiteValidationTest() {
    return Stream.of(
        Arguments.of(new ContactDetails(validEmail , validPhoneNumber, null, validAddress), Collections.singletonList("Website cannot be null")),
        Arguments.of(new ContactDetails(validEmail, validPhoneNumber, "", validAddress), Collections.singletonList("Website cannot be empty")),
        Arguments.of(new ContactDetails(validEmail , validPhoneNumber, "ww//sad(@82", validAddress), Collections.singletonList("Website has invalid format")),
        Arguments.of(new ContactDetails(validEmail , validPhoneNumber, validWebsite, validAddress), Collections.emptyList())
    );
  }


  @ParameterizedTest
  @MethodSource(value = "argumentsForAddressValidationTest")
  void validateAddress(ContactDetails contactDetails, List<String> expectedResult) {
    assertEquals(expectedResult, ContactDetailsValidator.validateContactDetails(contactDetails));
  }

  private static Stream<Arguments> argumentsForAddressValidationTest() {
    return Stream.of(
        Arguments.of(new ContactDetails(validEmail , validPhoneNumber, validWebsite, null ), Collections.singletonList("Address cannot be null")),
        Arguments.of(new ContactDetails(validEmail , validPhoneNumber, validWebsite, new Address("av", "1a", "2aa", "2", "a")), Collections.emptyList())
    );
  }

  @Test
  void validateIfContactDetailsIsNull() {
    //given
    List<String> expectedResult = Collections.singletonList("Contact details cannot be null");

    //then
    assertEquals(expectedResult, ContactDetailsValidator.validateContactDetails(null));
  }
}
