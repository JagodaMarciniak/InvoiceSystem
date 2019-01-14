package pl.coderstrust.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class AccountNumber {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  String id;
  private String ibanNumber;
  private String localNumber;

  public AccountNumber(String ibanNumber) {
    if (!ibanNumber.matches("^([A-Z]{2}[0-9]{26})")) {
      throw new IllegalArgumentException("Incorrect iban number");
    }
    this.ibanNumber = ibanNumber;
    this.localNumber = ibanNumber.substring(2);
  }
}
