package pl.coderstrust.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Entity
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  String street;

  String number;

  String postalCode;

  String city;

  String country;

  public Address() {
  }
}
