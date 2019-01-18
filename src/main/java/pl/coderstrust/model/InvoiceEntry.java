package pl.coderstrust.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InvoiceEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @ApiModelProperty(value = "The id of entries.")
  String id;

  @ApiModelProperty(value = "Name of the item.", example = "Flashlight")
  String item;

  @ApiModelProperty(value = "Total quantity of items.", example = "15")
  Long quantity;

  @ApiModelProperty(value = "Unit type.", example = "HOUR")
  UnitType unit;

  @ApiModelProperty(value = "Price for one unit of item.", example = "100")
  BigDecimal price;

  @ApiModelProperty(value = "Tax amount.", example = "VAT_23")
  Vat vatRate;

  @ApiModelProperty(value = "Value of the item, without tax.", example = "100")
  BigDecimal netValue;

  @ApiModelProperty(value = "Value of the item, with tax.", example = "123")
  BigDecimal grossValue;

  public InvoiceEntry(String item, Long quantity, UnitType unit,
                      BigDecimal price, Vat vatRate, BigDecimal netValue,
                      BigDecimal grossValue) {
    this.item = item;
    this.quantity = quantity;
    this.unit = unit;
    this.price = price;
    this.vatRate = vatRate;
    this.netValue = netValue;
    this.grossValue = grossValue;
  }
}
