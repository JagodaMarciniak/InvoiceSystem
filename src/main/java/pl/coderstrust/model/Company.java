package pl.coderstrust.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
public class Company {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  String name;

  String taxIdentificationNumber;

  @OneToOne(cascade = CascadeType.ALL)
  AccountNumber accountNumber;

  @OneToOne(cascade = CascadeType.ALL)
  ContactDetails contactDetails;

  public Company() {
  }
}
