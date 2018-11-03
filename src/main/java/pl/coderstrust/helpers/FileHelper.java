package pl.coderstrust.helpers;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.NoSuchFileException;
import java.util.List;
import lombok.NonNull;
import org.apache.commons.io.FileUtils;

public class FileHelper implements File {

  private String filePath;

  public FileHelper(@NonNull String filePath) throws IOException{
    this.filePath = filePath;
  }

  public void removeLine(int lineNumber) throws IOException {
    RandomAccessFile file = new RandomAccessFile(filePath, "rw");
    int counter = 0;
    long offset = 0;

    while (file.readLine() != null) {
      counter++;
      if (counter == lineNumber) {
        break;
      }
      offset = file.getFilePointer();
    }

    if (lineNumber > counter || lineNumber < 1) {
      file.close();
      throw new IllegalArgumentException("Incorrect lineNumber");
    }

    long length = file.getFilePointer() - offset;

    byte[] buffer = new byte[4096];
    int read = -1;
    while ((read = file.read(buffer)) > -1){
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
    FileUtils.write(new java.io.File(filePath), String.format("%s\r\n", line), true);
  }

  @Override
  public List<String> readLines() throws IOException {
    if (!exists()) {
      throw new NoSuchFileException("File not exist");
    }
    return FileUtils.readLines(new java.io.File(filePath));
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
