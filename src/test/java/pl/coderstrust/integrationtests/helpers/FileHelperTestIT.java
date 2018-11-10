package pl.coderstrust.integrationtests.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.coderstrust.filehelper.EmptyFileException;
import pl.coderstrust.filehelperimpl.FileHelper;

class FileHelperTestIT {

  private static final String inputFile = "src\\test\\resources\\filehelper\\filehelper\\input_file";
  private static final String expectedFile = "src\\test\\resources\\filehelper\\filehelper\\expected_file";

  @BeforeEach
  private void removeTestFiles() {
    File fileIn = new File(inputFile);
    if (fileIn.exists()) {
      fileIn.delete();
    }

    File fileOut = new File(expectedFile);
    if (fileOut.exists()) {
      fileOut.delete();
    }
  }

  @Test
  void shouldRemoveSpecificLineFromFile() throws Exception {
    //given
    createFile(inputFile, Arrays.asList("1", "2", "3", "4"));
    createFile(expectedFile, Arrays.asList("1", "3", "4"));
    pl.coderstrust.filehelper.File fileHelper = new FileHelper(inputFile);
    File resultFile = new File(inputFile);
    File expectedFile = new File(FileHelperTestIT.expectedFile);

    //when
    fileHelper.removeLine(2);

    //then
    assertTrue(FileUtils.contentEquals(expectedFile, resultFile));
  }

  @ParameterizedTest
  @MethodSource(value = "invalidArgumentsForRemovingSpecificLineFromFile")
  void shouldThrowExceptionWhenLineNumberIsInvalidDuringRemovingSpecificLine(List<String> lines, int lineNumber) throws IOException {
    createFile(inputFile, lines);
    assertThrows(IllegalArgumentException.class, () -> new FileHelper(inputFile).removeLine(lineNumber));
  }

  private static Stream<Arguments> invalidArgumentsForRemovingSpecificLineFromFile() {
    return Stream.of(
        Arguments.of(Arrays.asList("1", "2", "3"), 5),
        Arguments.of(Arrays.asList("1", "2", "3"), -5));
  }

  @Test
  void shouldThrowExceptionWhenTryingToRemoveLineFromNotExistingFile() {
    assertThrows(FileNotFoundException.class, () -> new FileHelper(inputFile).removeLine(5));
  }

  @Test
  void shouldThrowExceptionWhenTryingToRemoveLineFromEmptyFile() throws IOException {
    new File(inputFile).createNewFile();
    assertThrows(EmptyFileException.class, () -> new FileHelper(inputFile).removeLine(1));
  }

  @Test
  void shouldWriteLinesToFile() throws IOException {
    //given
    pl.coderstrust.filehelper.File fileHelper = new FileHelper(inputFile);
    createFile(expectedFile, Arrays.asList("1", "2", "3"));
    File resultFile = new File(inputFile);
    File expectedFile = new File(FileHelperTestIT.expectedFile);

    //when
    fileHelper.writeLines(Arrays.asList("1", "2", "3"));

    //then
    assertTrue(FileUtils.contentEquals(expectedFile, resultFile));
  }

  @Test
  void shouldThrowExceptionWhenLinesArgumentIsNullDuringWritingLinesToFile() {
    assertThrows(IllegalArgumentException.class, () -> new FileHelper(inputFile).writeLines(null));
  }

  @Test
  void shouldWriteLineToFile() throws IOException {
    //given
    pl.coderstrust.filehelper.File fileHelper = new FileHelper(inputFile);
    createFile(expectedFile, Arrays.asList("This", "is", "a", "test"));
    final File resultFile = new File(inputFile);
    final File expectedFile = new File(FileHelperTestIT.expectedFile);

    //when
    fileHelper.writeLine("This");
    fileHelper.writeLine("is");
    fileHelper.writeLine("a");
    fileHelper.writeLine("test");

    //then
    assertTrue(FileUtils.contentEquals(expectedFile, resultFile));
  }

  @Test
  void shouldThrowExceptionWhenLineArgumentIsNullDuringWritingLineToFile() {
    assertThrows(IllegalArgumentException.class, () -> new FileHelper(inputFile).writeLine(null));
  }

  @Test
  void shouldReadLinesFromExistingFile() throws IOException {
    //given
    createFile(inputFile, Arrays.asList("1", "2", "3"));
    List<String> expected = new ArrayList<>(Arrays.asList("1", "2", "3"));
    pl.coderstrust.filehelper.File fileHelper = new FileHelper(inputFile);

    //when
    List<String> result = fileHelper.readLines();

    //then
    assertEquals(expected, result);
  }

  @Test
  void shouldThrowExceptionWhenTryingToReadFromNotExistingFile() {
    assertThrows(FileNotFoundException.class, () -> new FileHelper("notExistingFile").readLines());
  }

  @Test
  void shouldClearDataFromExistingFile() throws IOException {
    //given
    createFile(inputFile, Arrays.asList("1", "2", "3"));
    pl.coderstrust.filehelper.File fileHelper = new FileHelper(inputFile);
    File resultFile = new File(inputFile);
    File expectedFile = new File(FileHelperTestIT.expectedFile);
    expectedFile.createNewFile();

    //when
    fileHelper.clear();

    //then
    assertTrue(FileUtils.contentEquals(expectedFile, resultFile));
  }

  @Test
  void shouldThrowExceptionWhenTryingToClearNotExistingFile() {
    assertThrows(FileNotFoundException.class, () -> new FileHelper("notExistingFile").clear());
  }

  @Test
  void shouldDeleteExistingFile() throws IOException {
    //given
    createFile(inputFile, Arrays.asList("1", "2", "3"));
    pl.coderstrust.filehelper.File fileHelper = new FileHelper(inputFile);

    //when
    fileHelper.delete();
    boolean result = new File(inputFile).exists();

    //
    assertFalse(result);
  }

  @Test
  void shouldReturnCorrectResultWhenTryingToDeleteNotExistingFile() {
    //given
    pl.coderstrust.filehelper.File fileHelper = new FileHelper(inputFile);

    //when
    fileHelper.delete();
    boolean result = new File(inputFile).exists();

    //
    assertFalse(result);
  }

  @Test
  void shouldReturnTrueWhenFileExist() throws IOException {
    //given
    createFile(inputFile, Arrays.asList("1", "2", "3"));
    pl.coderstrust.filehelper.File fileHelper = new FileHelper(inputFile);

    //when
    boolean result = fileHelper.exists();

    //then
    assertTrue(result);
  }

  @Test
  void shouldReturnFalseWhenFileNotExist() {
    //given
    pl.coderstrust.filehelper.File fileHelper = new FileHelper(inputFile);

    //when
    boolean result = fileHelper.exists();

    //then
    assertFalse(result);
  }

  @Test
  void shouldReturnTrueWhenFileIsEmpty() throws IOException {
    //given
    new File(inputFile).createNewFile();
    pl.coderstrust.filehelper.File fileHelper = new FileHelper(inputFile);

    //when
    boolean result = fileHelper.isEmpty();

    //then
    assertTrue(result);
  }

  @Test
  void shouldReturnFalseWhenFileIsNotEmpty() throws IOException {
    //given
    createFile(inputFile, Arrays.asList("1", "2", "3"));
    pl.coderstrust.filehelper.File fileHelper = new FileHelper(inputFile);

    //when
    boolean result = fileHelper.isEmpty();

    //then
    assertFalse(result);
  }

  @Test
  void shouldThrowExceptionWhenTryingToCheckIfFileIsEmptyOfNotExistingFile() {
    assertThrows(FileNotFoundException.class, () -> new FileHelper("notExistingFile").isEmpty());
  }

  private void createFile(String path, List<String> lines) throws IOException {
    FileUtils.writeLines(new File(path), lines);
  }
}
