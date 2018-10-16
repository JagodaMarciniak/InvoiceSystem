package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AddressTest {

  @ParameterizedTest
  @MethodSource("createArgumentsForAllGettersTest")
  public void testForAllGetters(String street, String number, String postalCode, String city, String country) {
    //when
    Address address = new Address(street, number, postalCode, city, country);

    //then
    assertEquals(street, address.getStreet());
    assertEquals(number, address.getNumber());
    assertEquals(postalCode, address.getPostalCode());
    assertEquals(city, address.getCity());
    assertEquals(country, address.getCountry());
  }

  private static Stream<Arguments> createArgumentsForAllGettersTest() {
    return Stream.of(
        Arguments.of("Ułanów", "4b", "88-100", "Gniezno", "Poland"),
        Arguments.of("Widokowa", "4/5", "11-996", "Rybnik", "Poland"));
  }

  @Test
  public void testExceptionWhenStreetNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      Address address = new Address(null, "Test", "Test", "Test", "Test");
    });
  }

  @Test
  public void testExceptionWhenNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      Address address = new Address("Test", null, "Test", "Test", "Test");
    });
  }

  @Test
  public void testExceptionWhenPostalCodeIsNull() {
    assertThrows(NullPointerException.class, () -> {
      Address address = new Address("Test", "Test", null, "Test", "Test");
    });
  }

  @Test
  public void testExceptionWhenCityNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      Address address = new Address("Test", "Test", "Test", null, "Test");
    });
  }

  @Test
  public void testExceptionWhenCountryNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      Address address = new Address("Test", "Test", "Test", "Test", null);
    });
  }
}
