package pl.coderstrust;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
