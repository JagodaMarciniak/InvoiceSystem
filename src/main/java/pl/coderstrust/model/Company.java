package pl.coderstrust.model;

import lombok.Data;
import lombok.NonNull;

@Data
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
