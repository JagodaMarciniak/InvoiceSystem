package pl.coderstrust.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import pl.coderstrust.generators.AddressGenerator;

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
}
