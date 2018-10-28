package pl.coderstrust.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class Company {
  @NonNull
  String name;

  @NonNull
  String taxIdentificationNumber;

  @NonNull
  AccountNumber accountNumber;

  @NonNull
  ContactDetails contactDetails;
}
