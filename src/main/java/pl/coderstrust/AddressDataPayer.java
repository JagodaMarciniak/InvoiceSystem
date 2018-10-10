package pl.coderstrust;

public class AddressDataPayer {

    private String streetName;
    private String houseNumber;
    private String postalCode;
    private String city;
    private String country;

    public AddressDataPayer(String streetName, String houseNumber, String postalCode, String city, String country){
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    @Override
    public String toString() {
        return "AddressDataPayer{" +
                "streetName='" + streetName + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
