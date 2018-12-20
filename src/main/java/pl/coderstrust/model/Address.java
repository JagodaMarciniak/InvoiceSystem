package pl.coderstrust.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {
  String street;

  String number;

  String postalCode;

  String city;

  String country;
}
