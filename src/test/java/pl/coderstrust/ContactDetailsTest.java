package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ContactDetailsTest {

  @Test
  public void checkFullyInitialization() {
    //given
    String email = "janusz@gmail.com";
    String phoneNumber = "885-941-002";
    String website = "www.sampleWebsite.com";

    //when
    ContactDetails contactDetails = new ContactDetails(email, phoneNumber, website,
        AddressGenerator.getSampleAddress());

    //then
    assertEquals(email, contactDetails.getEmail());
    assertEquals(phoneNumber, contactDetails.getPhoneNumber());
    assertEquals(website, contactDetails.getWebsite());
  }

  @Test
  public void shouldThrowExceptionWhenEmailAddressIsNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      new ContactDetails(null, "5239766", "www.maria-nawik.org.pl",
          AddressGenerator.getSampleAddress());
    });
  }

  @Test
  public void shouldThrowExceptionWhenPhoneNumberIsNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      new ContactDetails("maria-nawik@gmail.com", null, "www.maria-nawik.org.pl",
          AddressGenerator.getSampleAddress());
    });
  }

  @Test
  public void shouldThrowExceptionWhenWebsiteIsNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      new ContactDetails("maria-nawik@gmail.com", "5239766", null,
          AddressGenerator.getSampleAddress());
    });
  }

  @Test
  public void shouldThrowExceptionWhenAddressIsNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      new ContactDetails("maria-nawik@gmail.com", "5239766", "www.maria-nawik.org.pl",
          null);
    });
  }
}
