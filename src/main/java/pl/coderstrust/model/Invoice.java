package pl.coderstrust.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Invoice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @ApiModelProperty(value = "The id of invoice.")
  String id;

  @ApiModelProperty(value = "Type of the invoice.", example = "STANDARD")
  InvoiceType type;

  @ApiModelProperty(value = "Issue date of invoice.", example = "2019-01-01")
  LocalDate issueDate;

  @ApiModelProperty(value = "Due date of invoice.", example = "2019-01-01")
  LocalDate dueDate;

  @ManyToOne(cascade = CascadeType.ALL)
  Company seller;

  @ManyToOne(cascade = CascadeType.ALL)
  Company buyer;

  @ApiModelProperty(value = "List of purchased products.")
  @OneToMany(cascade = CascadeType.ALL)
  List<InvoiceEntry> entries;

  @ApiModelProperty(value = "Total net value of the invoice.", example = "100")
  BigDecimal totalNetValue;

  @ApiModelProperty(value = "Total value with tax of the invoice.", example = "123")
  BigDecimal totalGrossValue;

  @ApiModelProperty(value = "Comments fot the invoice.", example = "Some informations")
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
