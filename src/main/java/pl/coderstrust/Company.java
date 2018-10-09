package pl.coderstrust;

public class Company {
    
    private String name;
    private String taxIdentificationNumber;
    private String streetNameAndHouseNumber; //?
    private String postalCode; //?
    private String city; //?
    private String bankAccountNumber;
    private String country;
    private String email;
    private String phoneNumber;

    public Company(String name, String taxIdentificationNumber, String streetNameAndHouseNumber, String postalCode, String city, String bankAccountNumber, String country, String email, String phoneNumber) {
        this.name = name;
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.streetNameAndHouseNumber = streetNameAndHouseNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.bankAccountNumber = bankAccountNumber;
        this.country = country;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}