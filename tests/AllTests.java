package IHM.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ IPAddressValidatorTest.class,
                LoginValidatorTest.class,
                PasswordValidatorTest.class })
public class AllTests {

}
