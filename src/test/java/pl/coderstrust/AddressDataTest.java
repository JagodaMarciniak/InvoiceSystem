package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AddressDataTest {

  @ParameterizedTest
  @ValueSource(strings = {"Ogrodowa 2", "Wojska Polskiego 12/5", "Szarych Szeregów 3b"})
  public void testValidStreetName(String givenStreetAndHouseAddress) {
    //given
    AddressData addressData = new AddressData(givenStreetAndHouseAddress, "Test", "Test", "Test");

    //when
    String result = addressData.getStreetNameAndHouseNumber();

    //then
    assertEquals(givenStreetAndHouseAddress, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"88-388", "1-150", "10-200"})
  public void testValidPostalCode(String givenPostalCode) {
    //given
    AddressData addressData = new AddressData("Test", givenPostalCode, "Test", "Test");

    //when
    String result = addressData.getPostalCode();

    //then
    assertEquals(givenPostalCode, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"Olsztyn", "Nowy Dwór", "Mąkowarsko"})
  public void testValidCityName(String givenCityName) {
    //given
    AddressData addressData = new AddressData("Test", "Test", givenCityName, "Test");

    //when
    String result = addressData.getCity();

    //then
    assertEquals(givenCityName, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"Polska", "Bośnia i Hercegowina", "Wielka Brytania"})
  public void testValidCountryName(String givenCountryName) {
    //given
    AddressData addressData = new AddressData("Test", "Test", "Test", givenCountryName);

    //when
    String result = addressData.getCountry();

    //then
    assertEquals(givenCountryName, result);
  }

  @Test
  public void testExceptionWhenStreetAndHouseAddressIsNull() {
    assertThrows(NullPointerException.class, () -> {
      AddressData addressData = new AddressData(null, "Test", "Test", "Test");
    });
  }

  @Test
  public void testExceptionWhenPostalCodeIsNull() {
    assertThrows(NullPointerException.class, () -> {
      AddressData addressData = new AddressData("Test", null, "Test", "Test");
    });
  }

  @Test
  public void testExceptionWhenCityNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      AddressData addressData = new AddressData("Test", "Test", null, "Test");
    });
  }

  @Test
  public void testExceptionWhenCountryNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      AddressData addressData = new AddressData("Test", "Test", "Test", null);
    });
  }
}
