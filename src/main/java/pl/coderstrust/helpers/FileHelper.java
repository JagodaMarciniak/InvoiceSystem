package pl.coderstrust.helpers;

import java.io.IOException;
import java.util.List;

public interface FileHelper {

  void initialize() throws IOException, FileHelperException;

  void writeLines(List<String> lines) throws IOException;

  void writeLine(String line) throws IOException;

  List<String> readLines() throws IOException;

  void clear() throws IOException;

  void delete();

  boolean exists();

  boolean isEmpty() throws IOException;

  void removeLine(int lineNumber) throws IOException, FileHelperException;

  String readLastLine() throws IOException;
}
