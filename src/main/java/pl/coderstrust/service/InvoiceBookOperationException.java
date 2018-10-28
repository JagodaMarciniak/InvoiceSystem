package pl.coderstrust.service;

public class InvoiceBookOperationException extends Exception {
  public InvoiceBookOperationException(String message) {
    super(message);
  }

  public InvoiceBookOperationException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
