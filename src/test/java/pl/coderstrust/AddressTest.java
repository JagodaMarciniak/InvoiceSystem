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
    assertThrows(IllegalArgumentException.class, () -> {
      new Address(null, "4/6", "66-951", "Gdynia", "Poland");
    });
  }

  @Test
  public void shouldThrowExceptionWhenNumberIsNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Address("Wojska Polskiego", null, "66-951", "Gdynia", "Poland");
    });
  }

  @Test
  public void shouldThrowExceptionWhenPostalCodeIsNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Address("Wojska Polskiego", "4/6", null, "Gdynia", "Poland");
    });
  }

  @Test
  public void shouldThrowExceptionWhenCityNameIsNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Address("Wojska Polskiego", "4/6", "66-951", null, "Poland");
    });
  }

  @Test
  public void shouldThrowExceptionWhenCountryNameIsNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Address("Wojska Polskiego", "4/6", "66-951", "Gdynia", null);
    });
  }
}
