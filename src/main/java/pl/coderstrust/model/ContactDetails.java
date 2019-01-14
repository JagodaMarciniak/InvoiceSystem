package pl.coderstrust.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ContactDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  String id;

  String email;

  String phoneNumber;

  String website;

  @OneToOne(cascade = CascadeType.ALL)
  Address address;

  public ContactDetails(@NonNull String email, @NonNull String phoneNumber,
                        @NonNull String website, @NonNull Address address) {
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.website = website;
    this.address = address;
  }
}
