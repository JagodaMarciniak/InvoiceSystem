package pl.coderstrust;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SampleTest {

  @Test
  public void sampleTest() {
    //given
    HelloWorld helloWorld = new HelloWorld();
    String expected = "Hello World";

    //when
    String result = helloWorld.returnHelloWorldString();

    //then
    assertEquals(expected, result);
  }
}
