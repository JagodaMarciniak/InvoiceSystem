package pl.coderstrust.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
