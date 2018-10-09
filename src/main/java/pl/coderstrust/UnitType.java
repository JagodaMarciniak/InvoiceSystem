package pl.coderstrust;

public enum UnitType {

  PIECE("szt."),
  HOUR("godz."),
  DAY("dzień"),
  FLAT_RATE("ryczałt");

  private String unit;

  UnitType(String unit) {
    this.unit = unit;
  }

  public String getUnit() {
    return unit;
  }
}
