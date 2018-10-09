package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SampleTest {

   @Test
    public void sampleTest(){
       //given
       HelloWorld helloWorld = new HelloWorld();
       String expected = "Hello World";

       //when
       String result = helloWorld.returnHelloWorldString();

       //then
       assertEquals(expected, result);
   }
}
