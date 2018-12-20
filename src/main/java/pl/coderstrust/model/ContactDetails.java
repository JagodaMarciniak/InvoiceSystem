package pl.coderstrust.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContactDetails {
  String email;

  String phoneNumber;

  String website;

  Address address;
}
