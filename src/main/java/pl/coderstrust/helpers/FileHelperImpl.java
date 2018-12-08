package pl.coderstrust.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.io.FileUtils;

@AllArgsConstructor
public class FileHelperImpl implements FileHelper {

  @NonNull
  private String filePath;

  public void initialize() throws IOException, FileHelperException {
    File file = new File(filePath);
    if (file.exists()) {
      throw new FileHelperException("File already exists.");
    }
    file.createNewFile();
  }

  public void removeLine(int lineNumber) throws IOException, FileHelperException {
    if (lineNumber < 1) {
      throw new IllegalArgumentException("lineNumber cannot be lower than 1");
    }
    if (!exists()) {
      throw new FileNotFoundException("File does not exist");
    }
    if (isEmpty()) {
      throw new FileHelperException("Can't delete a line in an empty file");
    }
    RandomAccessFile file = new RandomAccessFile(filePath, "rw");

    long numberOfLines = 1;
    long offset = 0;

    while (file.readLine() != null && numberOfLines != lineNumber) {
      numberOfLines++;
      offset = file.getFilePointer();
    }

    if (lineNumber > numberOfLines) {
      file.close();
      throw new IllegalArgumentException("lineNumber is higher than file length");
    }

    long length = file.getFilePointer() - offset;

    byte[] buffer = new byte[4096];
    int read = -1;
    while ((read = file.read(buffer)) > -1) {
      file.seek(file.getFilePointer() - read - length);
      file.write(buffer, 0, read);
      file.seek(file.getFilePointer() + length);
    }
    file.setLength(file.length() - length);
    file.close();
  }

  @Override
  public void writeLines(@NonNull List<String> lines) throws IOException {
    FileUtils.writeLines(new java.io.File(filePath), lines, true);
  }

  @Override
  public void writeLine(@NonNull String line) throws IOException {
    FileUtils.writeLines(new java.io.File(filePath), Collections.singleton(line), true);
  }

  @Override
  public List<String> readLines() throws IOException {
    return FileUtils.readLines(new java.io.File(filePath));
  }

  @Override
  public void clear() throws IOException {
    if (!exists()) {
      throw new FileNotFoundException("File does not exist");
    }
    FileUtils.write(new java.io.File(filePath), "");
  }

  @Override
  public void delete() {
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
      throw new FileNotFoundException("File does not exist");
    }
    return new java.io.File(filePath).length() == 0;
  }
}
