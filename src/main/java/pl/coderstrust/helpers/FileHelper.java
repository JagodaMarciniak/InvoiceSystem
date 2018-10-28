package pl.coderstrust.helpers;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import lombok.NonNull;
import org.apache.commons.io.FileUtils;

public class FileHelper implements File {

  @Override
  public void writeLines(@NonNull String filePath, @NonNull List<String> lines) throws IOException {
    FileUtils.writeLines(new java.io.File(filePath), lines, true);
  }

  @Override
  public void writeLine(@NonNull String filePath, @NonNull String line) throws IOException {
    FileUtils.write(new java.io.File(filePath), String.format("%s\n", line), true);
  }

  @Override
  public List<String> readLines(@NonNull String filePath) throws IOException {
    if (!exists(filePath)) {
      throw new NoSuchFileException("File not exist");
    }
    return FileUtils.readLines(new java.io.File(filePath));
  }

  @Override
  public void clear(@NonNull String filePath) throws IOException {
    if (!exists(filePath)) {
      throw new NoSuchFileException("File not exist");
    }
    FileUtils.write(new java.io.File(filePath), "");
  }

  @Override
  public void delete(@NonNull String filePath) throws IOException {
    if (!exists(filePath)) {
      throw new NoSuchFileException("File not exist");
    }
    new java.io.File(filePath).delete();
  }

  @Override
  public boolean exists(@NonNull String filePath) {
    return new java.io.File(filePath).exists();
  }

  @Override
  public boolean isEmpty(@NonNull String filePath) throws IOException {
    if (!exists(filePath)) {
      throw new NoSuchFileException("File not exist");
    }
    return new java.io.File(filePath).length() == 0;
  }
}
