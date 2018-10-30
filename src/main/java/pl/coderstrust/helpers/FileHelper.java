package pl.coderstrust.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import lombok.NonNull;
import org.apache.commons.io.FileUtils;

public class FileHelper implements File {

  private String filePath;

  public FileHelper(@NonNull String filePath) throws IOException{
    this.filePath = filePath;
  }

  @Override
  public void writeLines(@NonNull List<String> lines) throws IOException {
    FileUtils.writeLines(new java.io.File(filePath), lines, true);
  }

  @Override
  public void writeLine(@NonNull String line) throws IOException {
    FileUtils.write(new java.io.File(filePath), String.format("%s\n", line), true);
  }

  @Override
  public Stream<String> readLines() throws IOException {
    if (!exists()) {
      throw new NoSuchFileException("File not exist");
    }
    return Files.lines(Paths.get(filePath));
  }

  @Override
  public void clear() throws IOException {
    if (!exists()) {
      throw new NoSuchFileException("File not exist");
    }
    FileUtils.write(new java.io.File(filePath), "");
  }

  @Override
  public void delete() throws IOException {
    if (exists()) {
      new java.io.File(filePath).delete();
    }
  }

  @Override
  public boolean exists() {
    return new java.io.File(filePath).exists();
  }

  @Override
  public boolean isEmpty() throws IOException {
    if (!exists()) {
      throw new NoSuchFileException("File not exist");
    }
    return new java.io.File(filePath).length() == 0;
  }
}
