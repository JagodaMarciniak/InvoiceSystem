package pl.coderstrust.integrationtests.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.coderstrust.helpers.FileHelperException;
import pl.coderstrust.helpers.FileHelper;

class FileHelperTestIT {

  private static final String INPUT_FILE = String.format("src%1$stest%1$sresources%1$shelpers%1$sinput_file", File.separator);
  private static final String EXPECTED_FILE = String.format("src%1$stest%1$sresources%1$shelpers%1$sexpected_file", File.separator);

  @BeforeEach
  private void removeTestFiles() {
    File fileIn = new File(INPUT_FILE);
    if (fileIn.exists()) {
      fileIn.delete();
    }

    File fileOut = new File(EXPECTED_FILE);
    if (fileOut.exists()) {
      fileOut.delete();
    }
  }

  @Test
  void shouldThrowExceptionWhenInitializeIsInvokedAndFileAlreadyExists() throws IOException {
    //given
    new File(INPUT_FILE).createNewFile();

    //when
    assertThrows(FileHelperException.class, () -> new FileHelper(INPUT_FILE).initialize());
  }

  @Test
  void shouldCreateFileWhenInitializeIsInvoked() throws Exception {
    //given
    FileHelper fileHelper = new FileHelper(INPUT_FILE);

    //when
    fileHelper.initialize();

    //then
    assertTrue(new File(INPUT_FILE).exists());
  }

  @Test
  void shouldRemoveSpecificLineFromFile() throws Exception {
    //given
    createFile(INPUT_FILE, Arrays.asList("1", "2", "3", "4"));
    createFile(EXPECTED_FILE, Arrays.asList("1", "3", "4"));
    FileHelper fileHelper = new FileHelper(INPUT_FILE);
    File resultFile = new File(INPUT_FILE);
    File expectedFile = new File(FileHelperTestIT.EXPECTED_FILE);

    //when
    fileHelper.removeLine(2);

    //then
    assertTrue(FileUtils.contentEquals(expectedFile, resultFile));
  }

  @ParameterizedTest
  @MethodSource(value = "invalidArgumentsForRemovingSpecificLineFromFile")
  void shouldThrowExceptionWhenLineNumberIsInvalidDuringRemovingSpecificLine(List<String> lines, int lineNumber) throws IOException {
    createFile(INPUT_FILE, lines);
    assertThrows(IllegalArgumentException.class, () -> new FileHelper(INPUT_FILE).removeLine(lineNumber));
  }

  @Test
  void shouldThrowExceptionWhenTryingToRemoveLineFromNotExistingFile() {
    assertThrows(FileNotFoundException.class, () -> new FileHelper(INPUT_FILE).removeLine(5));
  }

  private static Stream<Arguments> invalidArgumentsForRemovingSpecificLineFromFile() {
    return Stream.of(
        Arguments.of(Arrays.asList("1", "2", "3"), 5),
        Arguments.of(Arrays.asList("1", "2", "3"), -5));
  }

  @Test
  void shouldThrowExceptionWhenTryingToRemoveLineFromEmptyFile() throws IOException {
    new File(INPUT_FILE).createNewFile();
    assertThrows(FileHelperException.class, () -> new FileHelper(INPUT_FILE).removeLine(1));
  }

  @Test
  void shouldWriteLinesToFile() throws IOException {
    //given
    FileHelper fileHelper = new FileHelper(INPUT_FILE);
    createFile(EXPECTED_FILE, Arrays.asList("1", "2", "3"));
    File resultFile = new File(INPUT_FILE);
    File expectedFile = new File(FileHelperTestIT.EXPECTED_FILE);

    //when
    fileHelper.writeLines(Arrays.asList("1", "2", "3"));

    //then
    assertTrue(FileUtils.contentEquals(expectedFile, resultFile));
  }

  @Test
  void shouldThrowExceptionWhenLinesArgumentIsNullDuringWritingLinesToFile() {
    assertThrows(IllegalArgumentException.class, () -> new FileHelper(INPUT_FILE).writeLines(null));
  }

  @Test
  void shouldWriteLineToFile() throws IOException {
    //given
    FileHelper fileHelper = new FileHelper(INPUT_FILE);
    createFile(EXPECTED_FILE, Arrays.asList("This", "is", "a", "test"));
    final File resultFile = new File(INPUT_FILE);
    final File expectedFile = new File(FileHelperTestIT.EXPECTED_FILE);

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
    assertThrows(IllegalArgumentException.class, () -> new FileHelper(INPUT_FILE).writeLine(null));
  }

  @Test
  void shouldReadLinesFromExistingFile() throws IOException {
    //given
    createFile(INPUT_FILE, Arrays.asList("1", "2", "3"));
    List<String> expected = new ArrayList<>(Arrays.asList("1", "2", "3"));
    FileHelper fileHelper = new FileHelper(INPUT_FILE);

    //when
    List<String> result = fileHelper.readLines();

    //then
    assertEquals(expected, result);
  }

  @Test
  void shouldThrowExceptionWhenTryingToReadFromNotExistingFile() {
    assertThrows(FileNotFoundException.class, new FileHelper(INPUT_FILE)::readLines);
  }

  @Test
  void shouldClearDataFromExistingFile() throws IOException {
    //given
    createFile(INPUT_FILE, Arrays.asList("1", "2", "3"));
    FileHelper fileHelper = new FileHelper(INPUT_FILE);
    File resultFile = new File(INPUT_FILE);
    File expectedFile = new File(FileHelperTestIT.EXPECTED_FILE);
    expectedFile.createNewFile();

    //when
    fileHelper.clear();

    //then
    assertTrue(FileUtils.contentEquals(expectedFile, resultFile));
  }

  @Test
  void shouldThrowExceptionWhenTryingToClearNotExistingFile() {
    assertThrows(FileNotFoundException.class, new FileHelper(INPUT_FILE)::clear);
  }

  @Test
  void shouldDeleteExistingFile() throws IOException {
    //given
    createFile(INPUT_FILE, Arrays.asList("1", "2", "3"));
    FileHelper fileHelper = new FileHelper(INPUT_FILE);

    //when
    fileHelper.delete();
    boolean result = new File(INPUT_FILE).exists();

    //then
    assertFalse(result);
  }

  @Test
  void shouldReturnCorrectResultWhenTryingToDeleteNotExistingFile() {
    //given
    FileHelper fileHelper = new FileHelper(INPUT_FILE);

    //when
    fileHelper.delete();
    boolean result = new File(INPUT_FILE).exists();

    //then
    assertFalse(result);
  }

  @Test
  void shouldReturnTrueWhenFileExist() throws IOException {
    //given
    new File(INPUT_FILE).createNewFile();
    FileHelper fileHelper = new FileHelper(INPUT_FILE);

    //when
    boolean result = fileHelper.exists();

    //then
    assertTrue(result);
  }

  @Test
  void shouldReturnFalseWhenFileNotExist() {
    //given
    FileHelper fileHelper = new FileHelper(INPUT_FILE);

    //when
    boolean result = fileHelper.exists();

    //then
    assertFalse(result);
  }

  @Test
  void shouldReturnTrueWhenFileIsEmpty() throws IOException {
    //given
    new File(INPUT_FILE).createNewFile();
    FileHelper fileHelper = new FileHelper(INPUT_FILE);

    //when
    boolean result = fileHelper.isEmpty();

    //then
    assertTrue(result);
  }

  @Test
  void shouldReturnFalseWhenFileIsNotEmpty() throws IOException {
    //given
    createFile(INPUT_FILE, Arrays.asList("1", "2", "3"));
    FileHelper fileHelper = new FileHelper(INPUT_FILE);

    //when
    boolean result = fileHelper.isEmpty();

    //then
    assertFalse(result);
  }

  @Test
  void shouldThrowExceptionWhenTryingToCheckIfFileIsEmptyOfNotExistingFile() {
    assertThrows(FileNotFoundException.class, new FileHelper(INPUT_FILE)::isEmpty);
  }

  @Test
  void shouldReadLastLineFromFile() throws IOException {
    //given
    createFile(INPUT_FILE, Arrays.asList("1", "2", "3"));
    FileHelper fileHelper = new FileHelper(INPUT_FILE);

    //when
    String lastLine = fileHelper.readLastLine();

    //then
    assertEquals("3", lastLine);
  }

  @Test
  void shouldReturnNullWhenReadLastLineInvokedAndFileIsEmpty() throws IOException {
    //given
    createFile(INPUT_FILE, Collections.emptyList());
    FileHelper fileHelper = new FileHelper(INPUT_FILE);

    //when
    String lastLine = fileHelper.readLastLine();

    //then
    assertNull(lastLine);
  }

  @Test
  void shouldThrowExceptionWhenTryingToReadLastLineFromNotExistingFile() {
    //given
    FileHelper fileHelper = new FileHelper(INPUT_FILE);
    new File(INPUT_FILE).delete();

    //then
    assertThrows(IOException.class, fileHelper::readLastLine);
  }

  private void createFile(String path, List<String> lines) throws IOException {
    FileUtils.writeLines(new File(path), lines);
  }
}
