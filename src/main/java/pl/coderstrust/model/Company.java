package pl.coderstrust.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
