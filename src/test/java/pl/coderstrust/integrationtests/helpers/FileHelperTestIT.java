package pl.coderstrust.integrationtests.helpers;

import static org.junit.jupiter.api.Assertions.*;

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

  private static final String inputFilePath = "src\\test\\resources\\helpers\\test_input.txt";
  private static final String expectedFilePath = "src\\test\\resources\\helpers\\test_expected.txt";

  public void createFile(List<String> lines, String path) throws IOException {
    FileUtils.writeLines(new File(path),
        lines);
  }

  @BeforeEach
  private void removeFile() throws IOException {
    File file = new File(inputFilePath);
    if (file.exists()) {
      file.delete();
    }

    file = new File(expectedFilePath);
    if (file.exists()) {
      file.delete();
    }
  }

  @Test
  public void shouldRemoveLineFromFile() throws IOException {
    //given
    createFile(Arrays.asList("1", "2", "3", "4"), inputFilePath);
    createFile(Arrays.asList("1", "3", "4"), expectedFilePath);
    FileHelper fileHelper = new FileHelper(inputFilePath);

    //when
    fileHelper.removeLine(2);
    File resultFile = new File(inputFilePath);
    File expectedFile = new File(expectedFilePath);

    //then
    assertTrue(FileUtils.contentEquals(expectedFile, resultFile));
  }

  @ParameterizedTest
  @ValueSource(ints = {5, -5})
  public void shouldThrowExceptionWhenLineNumberIsInvalid(int lineNumber) throws IOException {
    createFile(Arrays.asList("1","2","3"), inputFilePath);
    assertThrows(IllegalArgumentException.class, () -> {
      new FileHelper(inputFilePath).removeLine(lineNumber);
    });
  }

  @Test
  public void shouldWriteLinesToFile() throws IOException{
    //given
    FileHelper fileHelper = new FileHelper(inputFilePath);
    createFile(Arrays.asList("1","2","3"), expectedFilePath);

    //when
    fileHelper.writeLines(Arrays.asList("1","2","3"));
    File resultFile = new File(inputFilePath);
    File expectedFile = new File(expectedFilePath);

    //then
    assertTrue(FileUtils.contentEquals(expectedFile, resultFile));
  }

  @Test
  public void shouldThrowExceptionWhenWriteLinesArgumentIsNull(){
    assertThrows(IllegalArgumentException.class, () -> {
      new FileHelper(inputFilePath).writeLines(null);
    });
  }

  @Test
  public void shouldWriteLineToFile()throws IOException{
    //given
    FileHelper fileHelper = new FileHelper(inputFilePath);
    createFile(Collections.singletonList("test"), expectedFilePath);

    //when
    fileHelper.writeLine("test");
    File resultFile = new File(inputFilePath);
    File expectedFile = new File(expectedFilePath);

    //then
    assertTrue(FileUtils.contentEquals(expectedFile, resultFile));
  }

  @Test
  public void shouldThrowExceptionWhenWriteLineArgumentIsNull(){
    assertThrows(IllegalArgumentException.class, () -> {
      new FileHelper(inputFilePath).writeLine(null);
    });
  }

  @Test
  public void shouldReadLinesFromExistingFile()throws IOException{
    //given
    createFile(Arrays.asList("1", "2", "3"), inputFilePath);
    FileHelper fileHelper = new FileHelper(inputFilePath);

    //when
    List<String> result = fileHelper.readLines();
    List<String> expected = new ArrayList<>(Arrays.asList("1", "2", "3"));

    //then
    assertEquals(expected, result);
  }

  @Test
  public void shouldThrowExceptionWhenTryingToReadFromNotExistingFile(){
    assertThrows(NoSuchFileException.class, () -> {
      new FileHelper("notExistingFile").readLines();
    });
  }

  @Test
  public void shouldClearDataFromExistingFile()throws IOException{
    //given
    createFile(Arrays.asList("1", "2", "3"), inputFilePath);
    FileHelper fileHelper = new FileHelper(inputFilePath);

    //when
    File resultFile = new File(inputFilePath);
    File expectedFile = new File(expectedFilePath);
    expectedFile.createNewFile();
    fileHelper.clear();

    //then
    assertTrue(FileUtils.contentEquals(expectedFile, resultFile));
  }

  @Test
  public void shouldThrowExceptionWhenTryingToClearNotExistingFile(){
    assertThrows(NoSuchFileException.class, () ->{
      new FileHelper("notExistingFile").clear();
    });
  }

  @Test
  public void shouldDeleteExistingFile()throws IOException{
    //given
    createFile(Arrays.asList("1", "2", "3"), inputFilePath);
    FileHelper fileHelper = new FileHelper(inputFilePath);

    //when
    fileHelper.delete();
    boolean result = new File(inputFilePath).exists();

    //
    assertFalse(result);
  }

  @Test
  public void shouldReturnTrueWhenFileExist()throws IOException{
    //given
    createFile(Arrays.asList("1","2","3") ,inputFilePath);
    FileHelper fileHelper = new FileHelper(inputFilePath);

    //when
    boolean result = fileHelper.exists();

    //then
    assertTrue(result);
  }

  @Test
  public void shouldReturnFalseWhenFileNotExist()throws IOException{
    //given
    FileHelper fileHelper = new FileHelper(inputFilePath);

    //when
    boolean result = fileHelper.exists();

    //then
    assertFalse(result);
  }

  @Test
  public void shouldReturnTrueWhenFileIsClear()throws IOException{
    //given
    new File(inputFilePath).createNewFile();
    FileHelper fileHelper = new FileHelper(inputFilePath);

    //when
    boolean result = fileHelper.isEmpty();

    //then
    assertTrue(result);
  }

  @Test
  public void shouldReturnFalseWhenFileIsNotClear()throws IOException{
    //given
    createFile(Arrays.asList("1","2","3"), inputFilePath);
    FileHelper fileHelper = new FileHelper(inputFilePath);

    //when
    boolean result = fileHelper.isEmpty();

    //then
    assertFalse(result);
  }
}
