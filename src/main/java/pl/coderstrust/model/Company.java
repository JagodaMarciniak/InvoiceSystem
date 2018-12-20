package pl.coderstrust.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Company {
  String name;

  String taxIdentificationNumber;

  AccountNumber accountNumber;

  ContactDetails contactDetails;
}
