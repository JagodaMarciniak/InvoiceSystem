package pl.coderstrust.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

  public Invoice(@NonNull InvoiceType type, @NonNull LocalDate issueDate, @NonNull LocalDate dueDate,
                 @NonNull Company seller, @NonNull Company buyer, @NonNull List<InvoiceEntry> entries, @NonNull BigDecimal totalNetValue,
                 @NonNull BigDecimal totalGrossValue, @NonNull String comments) {
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
