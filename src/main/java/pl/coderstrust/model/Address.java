package pl.coderstrust.model;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

  @ApiModelProperty(value = "The street name.", example = "Wojska Polskiego")
  String street;

  @ApiModelProperty(value = "The home number.", example = "5/27")
  String number;

  @ApiModelProperty(value = "The postal code.", example = "54765")
  String postalCode;

  @ApiModelProperty(value = "The city name.", example = "Ciechocinek")
  String city;

  @ApiModelProperty(value = "The country name.", example = "Polska")
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
