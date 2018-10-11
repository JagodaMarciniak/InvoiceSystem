package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ContactDataPayerTest {

  @ParameterizedTest
  @ValueSource(strings = {"janusz@interia.pl", "adam-malysz@gmail.com", "Poldek78@wp.pl"})
  public void testForValidEmailAddress(String givenEmailAddress) {
    //given
    ContactDataPayer contactDataPayer = new ContactDataPayer(givenEmailAddress, "Example");

    //when
    String result = contactDataPayer.getEmail();

    //then
    assertSame(givenEmailAddress, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"5275325", "600100900", "42500326715"})
  public void testForValidPhoneNumber(String givenPhoneNumber) {
    //given
    ContactDataPayer contactDataPayer = new ContactDataPayer("Example", givenPhoneNumber);

    //when
    String result = contactDataPayer.getPhoneNumber();

    //then
    assertSame(givenPhoneNumber, result);
  }

  @Test
  public void testForExceptionWhenEmailAddressIsNull() {
    assertThrows(NullPointerException.class, () -> {
      ContactDataPayer contactDataPayer = new ContactDataPayer(null, "Example");
    });
  }

  @Test
  public void testForExceptionWhenPhoneNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      ContactDataPayer contactDataPayer = new ContactDataPayer("Example", null);
    });
  }
}
