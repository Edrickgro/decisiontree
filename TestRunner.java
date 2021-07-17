package decisiontree;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


/*
 * You do not need to edit this file.
 *
 * This class is used to run all of your JUnit tests, which should be written in the
 * the MyID3Test.java file. If any of your tests fail, a failure
 * message will be printed out, and if they all pass, "all tests passed!" will be printed.
 * 
 * One way of running your tests is to right-click on this class (TestRunner.java) in the
 * Eclipse package explorer and select Run As -> Java Application.
 *
 */
public class TestRunner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(MyID3Test.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      if(result.wasSuccessful()) {
        System.out.println("all tests passed!");
      }
   }
}
