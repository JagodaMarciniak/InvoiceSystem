package pl.coderstrust.model;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@NoArgsConstructor
@Entity
public class AccountNumber {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @ApiModelProperty(value = "The id of account number.")
  String id;

  @ApiModelProperty(
      value = "26 digit account number with country code.",
      example = "PL83620519463926400000847295")
  private String ibanNumber;

  @ApiModelProperty(
      value = "26 digit account number.",
      example = "83620519463926400000847295")
  private String localNumber;

  public AccountNumber(String ibanNumber) {
    if (!ibanNumber.matches("^([A-Z]{2}[0-9]{26})")) {
      throw new IllegalArgumentException("Incorrect iban number");
    }
    this.ibanNumber = ibanNumber;
    this.localNumber = ibanNumber.substring(2);
  }
}
