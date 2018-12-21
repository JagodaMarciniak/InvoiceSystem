package pl.coderstrust.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
public class Invoice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  InvoiceType type;

  LocalDate issueDate;

  LocalDate dueDate;

  @ManyToOne(cascade = CascadeType.ALL)
  Company seller;

  @ManyToOne(cascade = CascadeType.ALL)
  Company buyer;

  @Transient
  List<InvoiceEntry> entries;

  BigDecimal totalNetValue;

  BigDecimal totalGrossValue;

  String comments;

  public Invoice() {
  }
}
