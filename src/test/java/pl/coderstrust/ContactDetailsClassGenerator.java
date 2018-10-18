package pl.coderstrust;

public class ContactDetailsClassGenerator {
  public static ContactDetails getSampleContactDetailsClass() {
    return new ContactDetails("maria-nawik@gmail.com", "5239766", "www.maria-nawik.org.pl",
        AddressClassGenerator.getSampleAddressClass());
  }
}
