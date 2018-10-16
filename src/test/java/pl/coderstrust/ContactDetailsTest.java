package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ContactDetailsTest {

  @ParameterizedTest
  @MethodSource("createArgumentsForAllGettersTest")
  public void testForAllGetters(String email, String phoneNumber, String website, String information) {
    //when
    ContactDetails contactDetails = new ContactDetails(email, phoneNumber, website, information,
        new Address("Test", "Test", "Test", "Test", "Test"));

    //then
    assertEquals(email, contactDetails.getEmail());
    assertEquals(phoneNumber, contactDetails.getPhoneNumber());
    assertEquals(website, contactDetails.getWebsite());
    assertEquals(information, contactDetails.getAdditionalInformation());
  }

  private static Stream<Arguments> createArgumentsForAllGettersTest() {
    return Stream.of(
        Arguments.of("janusz@wp.pl", "523965542", "www.sample.com", "Fuel WGR 12345"),
        Arguments.of("maria-ta@gmail.com", "886-765-009", "www.sample-next.com", "empty"));
  }

  @Test
  public void testExceptionWhenEmailAddressIsNull() {
    assertThrows(NullPointerException.class, () -> {
      ContactDetails contactDetails = new ContactDetails(null, "Test", "Test", "Test",
          new Address("Test", "Test", "Test", "Test", "Test"));
    });
  }

  @Test
  public void testExceptionWhenPhoneNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      ContactDetails contactDetails = new ContactDetails("Test", null, "Test", "Test",
          new Address("Test", "Test", "Test", "Test", "Test"));
    });
  }

  @Test
  public void testExceptionWhenWebsiteIsNull() {
    assertThrows(NullPointerException.class, () -> {
      ContactDetails contactDetails = new ContactDetails("Test", "Test", null, "Test",
          new Address("Test", "Test", "Test", "Test", "Test"));
    });
  }

  @Test
  public void testExceptionWhenAdditionalInformationMassageIsNull() {
    assertThrows(NullPointerException.class, () -> {
      ContactDetails contactDetails = new ContactDetails("Test", "Test", "Test", null,
          new Address("Test", "Test", "Test", "Test", "Test"));
    });
  }

  @Test
  public void testExceptionWhenAddressClassIsNull() {
    assertThrows(NullPointerException.class, () -> {
      ContactDetails contactDetails = new ContactDetails("Test", "Test", "Test", "Test",
          null);
    });
  }
}
