package pl.coderstrust.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


class AddressTest {

  @Test
  public void checkFullyInitialization() {
    //given
    String id = "1";
    String street = "Szarych Szeregów";
    String number = "8b";
    String postalCode = "66-843";
    String city = "Tarnów";
    String country = "Poland";

    //when
    Address address = new Address(id, street, number, postalCode, city, country);

    //then
    assertEquals(street, address.getStreet());
    assertEquals(number, address.getNumber());
    assertEquals(postalCode, address.getPostalCode());
    assertEquals(city, address.getCity());
    assertEquals(country, address.getCountry());
  }
}
