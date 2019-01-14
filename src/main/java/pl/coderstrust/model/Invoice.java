package pl.coderstrust.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Invoice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  String id;

  InvoiceType type;

  LocalDate issueDate;

  LocalDate dueDate;

  @ManyToOne(cascade = CascadeType.ALL)
  Company seller;

  @ManyToOne(cascade = CascadeType.ALL)
  Company buyer;

  @ElementCollection
  @OneToMany(cascade = CascadeType.ALL)
  List<InvoiceEntry> entries;

  BigDecimal totalNetValue;

  BigDecimal totalGrossValue;

  String comments;

  public Invoice(InvoiceType type, LocalDate issueDate, LocalDate dueDate,
                 Company seller, Company buyer, List<InvoiceEntry> entries, BigDecimal totalNetValue,
                 BigDecimal totalGrossValue, String comments) {
    this.type = type;
    this.issueDate = issueDate;
    this.dueDate = dueDate;
    this.seller = seller;
    this.buyer = buyer;
    this.entries = entries;
    this.totalNetValue = totalNetValue;
    this.totalGrossValue = totalGrossValue;
    this.comments = comments;
  }
}
