package pl.coderstrust;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class AddressDataPayer {
    private String streetName;
    private String houseNumber;
    private String postalCode;
    private String city;
    private String country;
}
