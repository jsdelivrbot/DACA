

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestProblemController.class, TestSolutionController.class, TestTestController.class,
		TestUserController.class })
public class AllTests {
 
}
