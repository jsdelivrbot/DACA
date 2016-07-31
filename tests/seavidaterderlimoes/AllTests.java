package seavidaterderlimoes;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ProblemsControllerTest.class, SolutionControllerTest.class, TestsControllerTest.class,
		UsersControllerTest.class })
public class AllTests {

}
