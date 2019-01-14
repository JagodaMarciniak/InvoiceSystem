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
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  String id;

  String name;

  String taxIdentificationNumber;

  @OneToOne(cascade = CascadeType.ALL)
  AccountNumber accountNumber;

  @OneToOne(cascade = CascadeType.ALL)
  ContactDetails contactDetails;

  public Company(@NonNull String name, @NonNull String taxIdentificationNumber,
                 @NonNull AccountNumber accountNumber, @NonNull ContactDetails contactDetails) {
    this.name = name;
    this.taxIdentificationNumber = taxIdentificationNumber;
    this.accountNumber = accountNumber;
    this.contactDetails = contactDetails;
  }
}
