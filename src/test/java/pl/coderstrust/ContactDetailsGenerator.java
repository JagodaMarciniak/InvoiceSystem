package pl.coderstrust;

public class ContactDetailsGenerator {
  public static ContactDetails getSampleContactDetails() {
    return new ContactDetails("maria-nawik@gmail.com", "5239766", "www.maria-nawik.org.pl",
        AddressGenerator.getSampleAddress());
  }
}
