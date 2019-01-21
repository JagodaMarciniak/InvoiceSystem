package pl.coderstrust.model.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.coderstrust.model.Address;


class AddressValidatorTest {

  private static String validStreet = "Wyzwolenia";
  private static String validNumber = "40";
  private static String validPostalCode = "44100";
  private static String validCity = "Pniewo";
  private static String validCountry = "Poland";

  @Test
  void testForAddressAsNull() {
    List<String> expectedResult = Collections.singletonList("Address cannot be null");
    assertEquals(expectedResult, AddressValidator.validateAddress(null));
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForStreetValidationTest")
  void validateStreet(Address address, List<String> expectedResult) {
    assertEquals(expectedResult, AddressValidator.validateAddress(address));
  }

  private static Stream<Arguments> argumentsForStreetValidationTest() {
    return Stream.of(
        Arguments.of(new Address(null, validNumber,validPostalCode, validCity, validCountry), Collections.singletonList("Street cannot be null")),
        Arguments.of(new Address("",validNumber,validPostalCode, validCity, validCountry), Collections.singletonList("Street cannot be empty")),
        Arguments.of(new Address(validStreet, validNumber, validPostalCode, validCity, validCountry), Collections.emptyList())
    );
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForNumberValidationTest")
  void validateNumber(Address address, List<String> expectedResult) {
    assertEquals(expectedResult, AddressValidator.validateAddress(address));
  }

  private static Stream<Arguments> argumentsForNumberValidationTest() {
    return Stream.of(
        Arguments.of(new Address(validStreet, null,validPostalCode, validCity, validCountry), Collections.singletonList("Number cannot be null")),
        Arguments.of(new Address(validStreet,"",validPostalCode, validCity, validCountry), Collections.singletonList("Number cannot be empty")),
        Arguments.of(new Address(validStreet,"a",validPostalCode, validCity, validCountry), Collections.emptyList()),
        Arguments.of(new Address(validStreet, validNumber, validPostalCode, validCity, validCountry), Collections.emptyList())
    );
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForStreetValidationTest")
  void validatePostalCode(Address address, List<String> expectedResult) {
    assertEquals(expectedResult, AddressValidator.validateAddress(address));
  }

  private static Stream<Arguments> argumentsForPostalCodeValidationTest() {
    return Stream.of(
        Arguments.of(new Address(validStreet, validNumber, null, validCity, validCountry), Collections.singletonList("Postal code cannot be null")),
        Arguments.of(new Address(validStreet,validNumber, "", validCity, validCountry), Collections.singletonList("Postal code cannot be empty")),
        Arguments.of(new Address(validStreet,validNumber, "2121", validCity, validCountry), Collections.singletonList("Postal code cannot be empty")),
        Arguments.of(new Address(validStreet, validNumber, validPostalCode, validCity, validCountry), Collections.emptyList())
    );
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForCityValidationTest")
  void validateCity(Address address, List<String> expectedResult) {
    assertEquals(expectedResult, AddressValidator.validateAddress(address));
  }

  private static Stream<Arguments> argumentsForCityValidationTest() {
    return Stream.of(
        Arguments.of(new Address(validStreet, validNumber,validPostalCode, null, validCountry), Collections.singletonList("City cannot be null")),
        Arguments.of(new Address(validStreet,validNumber,validPostalCode, "", validCountry), Collections.singletonList("City cannot be empty")),
        Arguments.of(new Address(validStreet, validNumber, validPostalCode, validCity, validCountry), Collections.emptyList())
    );
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForCountryValidationTest")
  void validateCountry(Address address, List<String> expectedResult) {
    assertEquals(expectedResult, AddressValidator.validateAddress(address));
  }

  private static Stream<Arguments> argumentsForCountryValidationTest() {
    return Stream.of(
        Arguments.of(new Address(validStreet, validNumber,validPostalCode, validCity, null), Collections.singletonList("Country cannot be null")),
        Arguments.of(new Address(validStreet,validNumber,validPostalCode, validCity, ""), Collections.singletonList("Country cannot be empty")),
        Arguments.of(new Address(validStreet, validNumber, validPostalCode, validCity, validCountry), Collections.emptyList())
    );
  }
}
