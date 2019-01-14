package pl.coderstrust.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InvoiceEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  String id;

  String item;

  Long quantity;

  UnitType unit;

  BigDecimal price;

  Vat vatRate;

  BigDecimal netValue;

  BigDecimal grossValue;

  public InvoiceEntry(@NonNull String item, @NonNull Long quantity, @NonNull UnitType unit,
                      @NonNull BigDecimal price, @NonNull Vat vatRate, @NonNull BigDecimal netValue,
                      @NonNull BigDecimal grossValue) {
    this.item = item;
    this.quantity = quantity;
    this.unit = unit;
    this.price = price;
    this.vatRate = vatRate;
    this.netValue = netValue;
    this.grossValue = grossValue;
  }
}
