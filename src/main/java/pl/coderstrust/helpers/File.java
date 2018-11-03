package pl.coderstrust.helpers;

import java.io.IOException;
import java.util.List;

public interface File {

  void writeLines(List<String> lines) throws IOException;

  void writeLine(String line) throws IOException;

  List<String> readLines() throws IOException;

  void clear() throws IOException;

  void delete() throws IOException;

  boolean exists();

  boolean isEmpty() throws IOException;

  public void removeLine(int lineNumber) throws IOException;
}
