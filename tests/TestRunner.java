package IHM.tests;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entry point for running tests.
 */
public class TestRunner {
    public static void main(String[] args) {
        // Run tests
        Result result = JUnitCore.runClasses(AllTests.class);

        Logger.getLogger(TestRunner.class.getName()).log(Level.INFO, result.getFailureCount() + " unit test(s) has(ve) failed.");
        for (Failure failure : result.getFailures()) {
            Logger.getLogger(TestRunner.class.getName()).log(Level.SEVERE, failure.toString());
        }
    }
}
