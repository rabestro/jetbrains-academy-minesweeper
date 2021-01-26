import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import java.util.ResourceBundle;

public class MyTest extends StageTest {
    //private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    @DynamicTest
    CheckResult FirstQuestionTest() {
        final var program = new TestedProgram();
        final var output = program.start().toLowerCase();
        if (!output.contains("how many mines")) {
            return CheckResult.wrong("The program should ask user about number of mines on the field.");
        }
        return CheckResult.correct();
    }


}