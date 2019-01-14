package pl.coderstrust.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  String id;

  String street;

  String number;

  String postalCode;

  String city;

  String country;

  public Address(@NonNull String street, @NonNull String number, @NonNull String postalCode,
                 @NonNull String city, @NonNull String country) {
    this.street = street;
    this.number = number;
    this.postalCode = postalCode;
    this.city = city;
    this.country = country;
  }
}
