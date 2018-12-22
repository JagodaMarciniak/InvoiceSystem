package pl.coderstrust.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
public class ContactDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  String email;

  String phoneNumber;

  String website;

  @OneToOne(cascade = CascadeType.ALL)
  Address address;

  public ContactDetails() {
  }
}
