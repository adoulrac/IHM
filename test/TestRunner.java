package IHM.test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Created by greyna on 17/11/2014.
 * Entry point for running tests.
 */
public class TestRunner {
    public static void main(String[] args) {
        // One or more test classes
        //Result result = JUnitCore.runClasses(MyClassTest.class /*, MySecondClassTest.class*/);

        // Suite class
        Result result = JUnitCore.runClasses(AllTests.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
    }
}
