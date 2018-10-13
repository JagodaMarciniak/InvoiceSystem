package pl.coderstrust;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class InvoiceEntry {

  private String item;
  private Integer quantity;
  private UnitType unit;
  private Vat vat;
  private BigDecimal price;
  private BigDecimal netValue;
  private BigDecimal grossValue;

  public InvoiceEntry(String item, Integer quantity, UnitType unit, BigDecimal price, Vat vat) {
    this.item = Objects.requireNonNull(item, "Item must not be null.");
    this.quantity = Objects.requireNonNull(quantity, "Quantity must not be null.");
    this.unit = Objects.requireNonNull(unit, "Unit must not be null.");
    this.price = Objects.requireNonNull(price, "Price must not be null.");
    this.vat = Objects.requireNonNull(vat, "Vat must not be null.");
    this.netValue = calculateNetValue();
    this.grossValue = calculateGrossValue();
  }

  public String getItem() {
    return item;
  }

  public void setItem(String item) {
    this.item = Objects.requireNonNull(item, "Item must not be null.");
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = Objects.requireNonNull(quantity, "Quantity must not be null.");
  }

  public UnitType getUnit() {
    return unit;
  }

  public void setUnit(UnitType unit) {
    this.unit = Objects.requireNonNull(unit, "Unit must not be null.");
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = Objects.requireNonNull(price, "Price must not be null.");
  }

  public Vat getVat() {
    return vat;
  }

  public void setVat(Vat vat) {
    this.vat = Objects.requireNonNull(vat, "Vat must not be null.");
  }

  public BigDecimal getNetValue() {
    return netValue;
  }

  public BigDecimal getGrossValue() {
    return grossValue;
  }

  private BigDecimal calculateNetValue() {
    BigDecimal netValueUnformatted = new BigDecimal(quantity).multiply(price);
    return netValueUnformatted.setScale(2, RoundingMode.HALF_UP);
  }

  public void updateNetValue() {
    this.netValue = calculateNetValue();
  }

  private BigDecimal calculateGrossValue() {
    BigDecimal multiplier = new BigDecimal(1).add(vat.getRate());
    BigDecimal grossValueUnformatted = calculateNetValue().multiply(multiplier);
    return grossValueUnformatted.setScale(2, RoundingMode.HALF_UP);
  }

  public void updateGrossValue() {
    this.grossValue = calculateGrossValue();
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    InvoiceEntry that = (InvoiceEntry) object;
    return Objects.equals(item, that.item)
        && Objects.equals(quantity, that.quantity)
        && unit == that.unit
        && vat == that.vat
        && Objects.equals(price, that.price)
        && Objects.equals(netValue, that.netValue)
        && Objects.equals(grossValue, that.grossValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(item, quantity, unit, vat, price, netValue, grossValue);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + "{"
        + "item='" + item + '\''
        + ", quantity='" + quantity
        + ", unit=" + unit
        + ", price=" + price
        + ", vat=" + vat
        + ", netValue=" + netValue
        + ", grossValue=" + grossValue
        + "}";
  }
}
