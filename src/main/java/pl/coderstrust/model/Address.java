package pl.coderstrust.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @ApiModelProperty(value = "The id of address.")
  String id;

  @ApiModelProperty(value = "The street name.", example = "")
  String street;

  String number;

  String postalCode;

  String city;

  String country;

  public Address(String street, String number, String postalCode,
                 String city, String country) {
    this.street = street;
    this.number = number;
    this.postalCode = postalCode;
    this.city = city;
    this.country = country;
  }
}
