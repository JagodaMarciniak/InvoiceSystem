package pl.coderstrust.service;

public class ServiceOperationException extends Exception {
  public ServiceOperationException() {
    super();
  }

  public ServiceOperationException(String message) {
    super(message);
  }

  public ServiceOperationException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
