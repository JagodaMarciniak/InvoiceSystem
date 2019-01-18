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
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @ApiModelProperty(value = "The id of company.")
  String id;

  @ApiModelProperty(value = "Name of the company.", example = "PolStal")
  String name;

  @ApiModelProperty(value = "Tax identification number - NIP.", example = "715-10-01-126")
  String taxIdentificationNumber;

  @OneToOne(cascade = CascadeType.ALL)
  AccountNumber accountNumber;

  @OneToOne(cascade = CascadeType.ALL)
  ContactDetails contactDetails;

  public Company(String name, String taxIdentificationNumber,
                 AccountNumber accountNumber, ContactDetails contactDetails) {
    this.name = name;
    this.taxIdentificationNumber = taxIdentificationNumber;
    this.accountNumber = accountNumber;
    this.contactDetails = contactDetails;
  }
}
