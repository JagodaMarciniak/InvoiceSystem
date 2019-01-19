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
  private static String validWebsite = "www.abc.pl";
  private static Address validAddress = AddressGenerator.getSampleAddress();

  @ParameterizedTest
  @MethodSource(value = "argumentsForEmailValidationTest")
  void validateStreet(Address address, List<String> expectedResult) {
    assertEquals(expectedResult, AddressValidator.validateAddress(address));
  }

  private static Stream<Arguments> argumentsForEmailValidationTest() {
    return Stream.of(
        Arguments.of(new ContactDetails(null , validPhoneNumber, validWebsite, validAddress), Collections.singletonList("Email cannot be null")),
        Arguments.of(new ContactDetails(null , validPhoneNumber, validWebsite, validAddress), Collections.singletonList("Email cannot be empty")),
        Arguments.of(new Address(validStreet, validNumber, validPostalCode, validCity, validCountry), Collections.emptyList())
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