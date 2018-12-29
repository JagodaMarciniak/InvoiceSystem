package pl.coderstrust.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

  @ElementCollection
  @OneToMany(cascade = CascadeType.ALL)
  List<InvoiceEntry> entries;

  BigDecimal totalNetValue;

  BigDecimal totalGrossValue;

  String comments;
}
