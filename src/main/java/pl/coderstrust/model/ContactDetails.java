package pl.coderstrust.model;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ContactDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @ApiModelProperty(value = "The id of contact details.")
  String id;

  @ApiModelProperty(value = "Email address.", example = "polstal@gmail.com")
  String email;

  @ApiModelProperty(value = "Contact phone number.", example = "775956888")
  String phoneNumber;

  @ApiModelProperty(value = "Company website.", example = "http://www.polstal.com.pl")
  String website;

  @OneToOne(cascade = CascadeType.ALL)
  Address address;

  public ContactDetails(String email, String phoneNumber,
                        String website, Address address) {
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.website = website;
    this.address = address;
  }
}
