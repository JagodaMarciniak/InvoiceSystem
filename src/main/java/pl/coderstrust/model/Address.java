package pl.coderstrust.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Entity
public class Address {
  @Id
  int id;

  String street;

  String number;

  String postalCode;

  String city;

  String country;
}
