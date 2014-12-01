package IHM.tests;

import IHM.validators.IPAddressValidator;
import IHM.validators.LoginValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// TODO: Auto-generated Javadoc
/**
 * The Class IPAddressValidatorTest.
 */
@RunWith(JUnit4.class)
public class IPAddressValidatorTest {

    /** The tester. */
    IPAddressValidator tester = new IPAddressValidator();

    /**
     * Validate.
     */
    @Test
    public void validate() {
        assertTrue("Validate right IP address", tester.validate("192.168.1.1"));
        assertFalse("Validate wrong IP address (must have a 4 '.' characters)", tester.validate("192.102.100"));
        assertFalse("Validate wrong IP address (cannot have characters between '.')", tester.validate("a.b.c.d"));
        assertFalse("Validate wrong IP address (can have only until 255)", tester.validate("1.2.3.800"));
        assertFalse("Validate wrong IP address (must have 4 digit parts)", tester.validate("1.2.3"));
    }
}