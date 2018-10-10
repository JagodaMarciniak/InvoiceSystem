package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AddressDataPayerTest {

    @ParameterizedTest
    @ValueSource(strings = {"Ogrodowa", "Wojska Polskiego", "Szarych Szeregów"})
    public void testForValidStreetName(String givenStreetName){
        //given
        AddressDataPayer addressDataPayer = new AddressDataPayer(givenStreetName, "Example", "Example", "Example", "Example");

        //when
        String result = addressDataPayer.getStreetName();

        //then
        assertSame(givenStreetName, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"6", "0", "4/162"})
    public void testForValidHouseNumber(String givenHouseNumber){
        //given
        AddressDataPayer addressDataPayer = new AddressDataPayer("Example", givenHouseNumber, "Example", "Example", "Example");

        //when
        String result = addressDataPayer.getHouseNumber();

        //then
        assertSame(givenHouseNumber, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"88-388", "1-150", "10-200"})
    public void testForValidPostalCode(String givenPostalCode){
        //given
        AddressDataPayer addressDataPayer = new AddressDataPayer("Example", "Example", givenPostalCode, "Example", "Example");

        //when
        String result = addressDataPayer.getPostalCode();

        //then
        assertSame(givenPostalCode, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Olsztyn", "Nowy Dwór", "Mąkowarsko"})
    public void testForValidCityName(String givenCityName){
        //given
        AddressDataPayer addressDataPayer = new AddressDataPayer("Example", "Example", "Example", givenCityName, "Example");

        //when
        String result = addressDataPayer.getCity();

        //then
        assertSame(givenCityName, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Polska", "Bośnia i Hercegowina", "Wielka Brytania"})
    public void testForValidCountryName(String givenCountryName){
        //given
        AddressDataPayer addressDataPayer = new AddressDataPayer("Example", "Example", "Example", "Example", givenCountryName);

        //when
        String result = addressDataPayer.getCountry();

        //then
        assertSame(givenCountryName, result);
    }

    @Test
    public void testForExceptionWhenStreetNameIsNull(){
        assertThrows(NullPointerException.class, () -> {
            AddressDataPayer addressDataPayer = new AddressDataPayer(null, "Example", "Example", "Example", "Example");
        });
    }

    @Test
    public void testForExceptionWhenHouseNumberIsNull(){
        assertThrows(NullPointerException.class, () -> {
            AddressDataPayer addressDataPayer = new AddressDataPayer("Example", null, "Example", "Example", "Example");
        });
    }

    @Test
    public void testForExceptionWhenPostalCodeIsNull(){
        assertThrows(NullPointerException.class, () -> {
            AddressDataPayer addressDataPayer = new AddressDataPayer("Example", "Example", null, "Example", "Example");
        });
    }

    @Test
    public void testForExceptionWhenCityNameIsNull(){
        assertThrows(NullPointerException.class, () -> {
            AddressDataPayer addressDataPayer = new AddressDataPayer("Example", "Example", "Example", null, "Example");
        });
    }

    @Test
    public void testForExceptionWhenCountryNameIsNull(){
        assertThrows(NullPointerException.class, () -> {
            AddressDataPayer addressDataPayer = new AddressDataPayer("Example", "Example", "Example", "Example", null);
        });
    }
}