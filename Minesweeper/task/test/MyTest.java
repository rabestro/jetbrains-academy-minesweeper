import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import java.util.ResourceBundle;

import static java.text.MessageFormat.format;

public class MyTest extends StageTest {
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    private static void assertContains(
            final String output,
            final String lookFor,
            final String errorMessage,
            final Object ...args) {

        if (!output.toLowerCase().contains(lookFor)) {
            final var feedback = format(messages.getString(errorMessage), args)
                    + "\nExpected to find '" + lookFor + "' in output."
                    + "\nYou program output:\n" + output;
            throw new WrongAnswer(feedback);
        }
    }

    @DynamicTest
    CheckResult FirstQuestionTest() {
        final var program = new TestedProgram();
        Assert.contains(program.start(), "how many mines", "first_question");
        return CheckResult.correct();
    }

    int[] mines = {
            1, 2, 3, 4, 5
    };
    @DynamicTest(data = "mines")
    CheckResult GameBoardTest(final int minesCount) {
        final var program = new TestedProgram();
        program.start();
        final var output = program.execute(String.valueOf(minesCount));

        final var step = GameStep.parse(output);

        Assert.contains(output, "set/unset mines marks", "ask_coordinates");

        return CheckResult.correct();
    }
}