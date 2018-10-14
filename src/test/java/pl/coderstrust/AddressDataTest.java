package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AddressDataTest {

  @ParameterizedTest
  @ValueSource(strings = {"Ogrodowa 2", "Wojska Polskiego 12/5", "Szarych Szeregów 3b"})
  public void testForValidStreetName(String givenStreetAndHouseAddress) {
    //given
    AddressData addressData = new AddressData(givenStreetAndHouseAddress, "Example", "Example", "Example");

    //when
    String result = addressData.getStreetNameAndHouseNumber();

    //then
    assertEquals(givenStreetAndHouseAddress, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"88-388", "1-150", "10-200"})
  public void testForValidPostalCode(String givenPostalCode) {
    //given
    AddressData addressData = new AddressData("Example", givenPostalCode, "Example", "Example");

    //when
    String result = addressData.getPostalCode();

    //then
    assertEquals(givenPostalCode, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"Olsztyn", "Nowy Dwór", "Mąkowarsko"})
  public void testForValidCityName(String givenCityName) {
    //given
    AddressData addressData = new AddressData("Example", "Example", givenCityName, "Example");

    //when
    String result = addressData.getCity();

    //then
    assertEquals(givenCityName, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"Polska", "Bośnia i Hercegowina", "Wielka Brytania"})
  public void testForValidCountryName(String givenCountryName) {
    //given
    AddressData addressData = new AddressData("Example", "Example", "Example", givenCountryName);

    //when
    String result = addressData.getCountry();

    //then
    assertEquals(givenCountryName, result);
  }

  @Test
  public void testForExceptionWhenStreetAndHouseAddressIsNull() {
    assertThrows(NullPointerException.class, () -> {
      AddressData addressData = new AddressData(null, "Example", "Example", "Example");
    });
  }

  @Test
  public void testForExceptionWhenPostalCodeIsNull() {
    assertThrows(NullPointerException.class, () -> {
      AddressData addressData = new AddressData("Example", null, "Example", "Example");
    });
  }

  @Test
  public void testForExceptionWhenCityNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      AddressData addressData = new AddressData("Example", "Example", null, "Example");
    });
  }

  @Test
  public void testForExceptionWhenCountryNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      AddressData addressData = new AddressData("Example", "Example", "Example", null);
    });
  }
}
