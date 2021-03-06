package IHM.tests;

import IHM.validators.LoginValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The Class LoginValidatorTest.
 */
@RunWith(JUnit4.class)
public class LoginValidatorTest {

    /**
     * The tester.
     */
    LoginValidator tester = new LoginValidator();

    /**
     * Validate.
     */
    @Test
    public void validate() {
        assertTrue("Validate right login", tester.validate("adoulrac34"));
        assertTrue("Validate right login with -/_", tester.validate("adoulrac_3-4"));
        assertFalse("Validate wrong login (too short)", tester.validate("ad"));
        assertFalse("Validate wrong login (too long)", tester.validate("adoulracisbackatschool"));
        assertFalse("Validate wrong login (special characters)", tester.validate("adoulrac@34"));
    }
}
