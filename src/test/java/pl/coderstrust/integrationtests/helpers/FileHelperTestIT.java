package pl.coderstrust.integrationtests.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pl.coderstrust.helpers.FileHelper;

class FileHelperTestIT {

  private static final String inputFile = "src\\test\\resources\\helpers\\filehelper\\input_file.txt";
  private static final String outputFile = "src\\test\\resources\\helpers\\filehelper\\expected_file.txt";

  @BeforeEach
  private void removeTestFiles() {
    File fileIn = new File(inputFile);
    if (fileIn.exists()) {
      fileIn.delete();
    }

    File fileOut = new File(outputFile);
    if (fileOut.exists()) {
      fileOut.delete();
    }
  }

  @Test
  public void shouldRemoveSpecificLineFromFile() throws IOException {
    //given
    createFile(inputFile, Arrays.asList("1", "2", "3", "4"));
    createFile(outputFile, Arrays.asList("1", "3", "4"));
    pl.coderstrust.helpers.File fileHelper = new FileHelper(inputFile);
    File resultFile = new File(inputFile);
    File expectedFile = new File(outputFile);

    //when
    fileHelper.removeLine(2);

    //then
    assertTrue(FileUtils.contentEquals(expectedFile, resultFile));
  }

  @ParameterizedTest
  @ValueSource(ints = {5, -5})
  public void shouldThrowExceptionWhenLineNumberIsInvalidDuringRemovingSpecificLine(int lineNumber) throws IOException {
    createFile(inputFile, Arrays.asList("1", "2", "3"));
    assertThrows(IllegalArgumentException.class, () -> {
      new FileHelper(inputFile).removeLine(lineNumber);
    });
  }

  @Test
  public void shouldThrowExceptionWhenTryingToRemoveLineFromNotExistingFile() {
    assertThrows(NoSuchFileException.class, () -> {
      new FileHelper(inputFile).removeLine(5);
    });
  }

  @Test
  public void shouldThrowExceptionWhenTryingToRemoveLineFromEmptyFile() throws IOException {
    createFile(inputFile, Collections.singletonList(""));
    assertThrows(IllegalArgumentException.class, () -> {
      new FileHelper(inputFile).removeLine(5);
    });
  }

  @Test
  public void shouldWriteLinesToFile() throws IOException {
    //given
    pl.coderstrust.helpers.File fileHelper = new FileHelper(inputFile);
    createFile(outputFile, Arrays.asList("1", "2", "3"));
    File resultFile = new File(inputFile);
    File expectedFile = new File(outputFile);

    //when
    fileHelper.writeLines(Arrays.asList("1", "2", "3"));

    //then
    assertTrue(FileUtils.contentEquals(expectedFile, resultFile));
  }

  @Test
  public void shouldThrowExceptionWhenLinesArgumentIsNullDuringWritingLinesToFile() {
    assertThrows(IllegalArgumentException.class, () -> {
      new FileHelper(inputFile).writeLines(null);
    });
  }

  @Test
  public void shouldWriteLineToFile() throws IOException {
    //given
    pl.coderstrust.helpers.File fileHelper = new FileHelper(inputFile);
    createFile(outputFile, Arrays.asList("This", "is", "a", "test"));
    final File resultFile = new File(inputFile);
    final File expectedFile = new File(outputFile);

    //when
    fileHelper.writeLine("This");
    fileHelper.writeLine("is");
    fileHelper.writeLine("a");
    fileHelper.writeLine("test");

    //then
    assertTrue(FileUtils.contentEquals(expectedFile, resultFile));
  }

  @Test
  public void shouldThrowExceptionWhenLineArgumentIsNullDuringWritingLineToFile() {
    assertThrows(IllegalArgumentException.class, () -> {
      new FileHelper(inputFile).writeLine(null);
    });
  }

  @Test
  public void shouldReadLinesFromExistingFile() throws IOException {
    //given
    createFile(inputFile, Arrays.asList("1", "2", "3"));
    List<String> expected = new ArrayList<>(Arrays.asList("1", "2", "3"));
    pl.coderstrust.helpers.File fileHelper = new FileHelper(inputFile);

    //when
    List<String> result = fileHelper.readLines();

    //then
    assertEquals(expected, result);
  }

  @Test
  public void shouldThrowExceptionWhenTryingToReadFromNotExistingFile() {
    assertThrows(NoSuchFileException.class, () -> {
      new FileHelper("notExistingFile").readLines();
    });
  }

  @Test
  public void shouldClearDataFromExistingFile() throws IOException {
    //given
    createFile(inputFile, Arrays.asList("1", "2", "3"));
    pl.coderstrust.helpers.File fileHelper = new FileHelper(inputFile);
    File resultFile = new File(inputFile);
    File expectedFile = new File(outputFile);
    expectedFile.createNewFile();

    //when
    fileHelper.clear();

    //then
    assertTrue(FileUtils.contentEquals(expectedFile, resultFile));
  }

  @Test
  public void shouldThrowExceptionWhenTryingToClearNotExistingFile() {
    assertThrows(NoSuchFileException.class, () -> {
      new FileHelper("notExistingFile").clear();
    });
  }

  @Test
  public void shouldDeleteExistingFile() throws IOException {
    //given
    createFile(inputFile, Arrays.asList("1", "2", "3"));
    pl.coderstrust.helpers.File fileHelper = new FileHelper(inputFile);

    //when
    fileHelper.delete();
    boolean result = new File(inputFile).exists();

    //
    assertFalse(result);
  }

  @Test
  public void shouldReturnCorrectResultWhenTryingToDeleteNotExistingFile() throws IOException {
    //given
    pl.coderstrust.helpers.File fileHelper = new FileHelper(inputFile);

    //when
    fileHelper.delete();
    boolean result = new File(inputFile).exists();

    //
    assertFalse(result);
  }

  @Test
  public void shouldReturnTrueWhenFileExist() throws IOException {
    //given
    createFile(inputFile, Arrays.asList("1", "2", "3"));
    pl.coderstrust.helpers.File fileHelper = new FileHelper(inputFile);

    //when
    boolean result = fileHelper.exists();

    //then
    assertTrue(result);
  }

  @Test
  public void shouldReturnFalseWhenFileNotExist() throws IOException {
    //given
    pl.coderstrust.helpers.File fileHelper = new FileHelper(inputFile);

    //when
    boolean result = fileHelper.exists();

    //then
    assertFalse(result);
  }

  @Test
  public void shouldReturnTrueWhenFileIsEmpty() throws IOException {
    //given
    new File(inputFile).createNewFile();
    pl.coderstrust.helpers.File fileHelper = new FileHelper(inputFile);

    //when
    boolean result = fileHelper.isEmpty();

    //then
    assertTrue(result);
  }

  @Test
  public void shouldReturnFalseWhenFileIsNotEmpty() throws IOException {
    //given
    createFile(inputFile, Arrays.asList("1", "2", "3"));
    pl.coderstrust.helpers.File fileHelper = new FileHelper(inputFile);

    //when
    boolean result = fileHelper.isEmpty();

    //then
    assertFalse(result);
  }

  private void createFile(String path, List<String> lines) throws IOException {
    FileUtils.writeLines(new File(path), lines);
  }
}
