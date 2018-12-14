package pl.coderstrust.service;

public class InvoiceServiceOperationException extends Exception {
  public InvoiceServiceOperationException() {
    super();
  }

  public InvoiceServiceOperationException(String message) {
    super(message);
  }

  public InvoiceServiceOperationException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
