package IHM.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * Created by greyna on 17/11/2014.
 */
@RunWith(JUnit4.class)
public class MyClassTest {

    // MyClass is tested
    MyClass tester = new MyClass();

    @Test
    public void multiplication() {
        assertEquals("10 x 0 must be 0", 0, tester.multiply(10, 0));
        assertEquals("0 x 10 must be 0", 0, tester.multiply(0, 10));
        assertEquals("0 x 0 must be 0", 0, tester.multiply(0, 0));
        assertEquals("3 x 3 must be 9", 9, tester.multiply(3, 3));
        assertEquals("4 x 7 must be 28", 28, tester.multiply(4, 7));
    }


    @Test
    public void division() {
        assertEquals("0 / 10 must be 0", 0, tester.divide(0, 10), 0.0); // last number is the precision accepted
        assertEquals("3 / 3 must be 1", 1, tester.divide(3, 3), 0.0);
        assertEquals("12 / 4 must be 3", 3, tester.divide(12, 4), 0.0);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Test
    public void divisionByZeroShouldThrow() throws ArithmeticException {
        thrown.expect(ArithmeticException.class);
        thrown.expectMessage("/ by zero");
        tester.divide(10, 0);
    }
}
