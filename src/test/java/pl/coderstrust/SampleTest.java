package pl.coderstrust;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SampleTest {
<<<<<<< HEAD
<<<<<<< HEAD

  @Test
  public void sampleTest() {

  }
=======
=======

>>>>>>> master
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
>>>>>>> master
}
