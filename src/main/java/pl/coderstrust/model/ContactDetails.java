package pl.coderstrust.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class ContactDetails {
  @NonNull
  String email;

  @NonNull
  String phoneNumber;

  @NonNull
  String website;

  @NonNull
  Address address;
}
