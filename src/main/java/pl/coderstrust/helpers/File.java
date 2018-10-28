package pl.coderstrust.helpers;

import java.io.IOException;
import java.util.List;

public interface File {

  void writeLines(String filePath, List<String> lines) throws IOException;

  void writeLine(String filePath, String line) throws IOException;

  List<String> readLines(String filePath) throws IOException;

  void clear(String filePath) throws IOException;

  void delete(String filePath) throws IOException;

  boolean exists(String filePath);

  boolean isEmpty(String filePath) throws IOException;

}
