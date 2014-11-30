package IHM.tests;

import IHM.validators.LoginValidator;
import IHM.validators.PasswordValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


// TODO: Auto-generated Javadoc
/**
 * The Class PasswordValidatorTest.
 */
@RunWith(JUnit4.class)
public class PasswordValidatorTest {

    /** The tester. */
    PasswordValidator tester = new PasswordValidator();

    /**
     * Validate.
     */
    @Test
    public void validate() {
        assertTrue("Validate right password", tester.validate("mkYOn12$"));
        assertFalse("Validate wrong password (uppercase is required)", tester.validate("mkyong12@"));
        assertFalse("Validate wrong password (too short)", tester.validate("mY1A@"));
        assertFalse("Validate wrong password (lower case character is required)", tester.validate("MKYONG12$"));
        assertFalse("Validate wrong password (digit is required)", tester.validate("mkyonG$$"));
    }
}
