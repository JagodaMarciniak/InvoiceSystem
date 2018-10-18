package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AddressTest {

  @Test
  public void checkFullyInitialization() {
    //given
    String street = "Szarych Szeregów";
    String number = "8b";
    String postalCode = "66-843";
    String city = "Tarnów";
    String country = "Poland";

    //when
    Address address = new Address(street, number, postalCode, city, country);

    //then
    assertEquals(street, address.getStreet());
    assertEquals(number, address.getNumber());
    assertEquals(postalCode, address.getPostalCode());
    assertEquals(city, address.getCity());
    assertEquals(country, address.getCountry());
  }

  @Test
  public void shouldThrowExceptionWhenStreetNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Address(null, "Test", "Test", "Test", "Test");
    });
  }

  @Test
  public void shouldThrowExceptionWhenNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Address("Test", null, "Test", "Test", "Test");
    });
  }

  @Test
  public void shouldThrowExceptionWhenPostalCodeIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Address("Test", "Test", null, "Test", "Test");
    });
  }

  @Test
  public void shouldThrowExceptionWhenCityNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Address("Test", "Test", "Test", null, "Test");
    });
  }

  @Test
  public void shouldThrowExceptionWhenCountryNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Address("Test", "Test", "Test", "Test", null);
    });
  }
}
