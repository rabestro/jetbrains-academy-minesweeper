import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import java.util.Random;
import java.util.ResourceBundle;

public class MyTest extends StageTest {
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");
    private static final Random random = new Random();
    private static final String MINES_70 = "70";

    @DynamicTest
    CheckResult FirstQuestionShouldAskMineCount() {
        final var program = new TestedProgram();
        Assert.contains(new TestedProgram().start(), "how many mines", "first_question");
        return CheckResult.correct();
    }

    @DynamicTest(repeat = 50)
    CheckResult FirstFreeMoveShouldBySafe() {
        final var program = new TestedProgram();
        program.start();
        program.execute(MINES_70);
        final var output = program.execute(getRandomFreeMove());
        final var game = GameStep.parse(output);

        Assert.that(!game.isFailed(), "first_free_move");
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

    private static String getRandomFreeMove() {
        return String.format("%d %d free", 1 + random.nextInt(9), 1 + random.nextInt(9));
    }
}