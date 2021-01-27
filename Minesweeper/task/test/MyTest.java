import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.IntPredicate;

public class MyTest extends StageTest {
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");
    private static final Random random = new Random();
    private static final String MINES_70 = "70";
    private static final String ONE_MINE = "1";

    @DynamicTest
    CheckResult firstQuestionShouldAskMineCount() {
        final var program = new TestedProgram();
        Assert.contains(program.start(), "how many mines", "first_question");
        return CheckResult.correct();
    }

    @DynamicTest(repeat = 10)
    CheckResult firstFreeMoveShouldBySafe() {
        final var program = new TestedProgram();
        program.start();
        program.execute(MINES_70);
        final var output = program.execute(getRandomFreeMove());
        final var game = GameStep.parse(output);

        Assert.that(!game.isFailed(), "first_free_move");
        return CheckResult.correct();
    }

    @DynamicTest
    CheckResult oneMineWinBySuggestingEachFieldTest() {
        final var program = new TestedProgram();
        program.start();
        program.execute(ONE_MINE);

        final IntPredicate isMineFound = index -> {
            var game = GameStep.parse(program.execute(toMove(index, true)));
            Assert.that(game.countSymbol('*') == 1, "expect_one_asterisk");
            Assert.that(!game.isFailed(), "no_failed_after_mark");
            if (game.isWin()) {
                return true;
            }
            game = GameStep.parse(program.execute(toMove(index, true)));
            Assert.that(game.countSymbol('*') == 0, "expect_no_asterisk");
            Assert.that(game.isPlaying(), "expected_playing");
            return false;
        };

        GameStep.allIndexes()
                .filter(isMineFound)
                .findFirst()
                .orElseThrow(() -> Assert.error("no_mine_found"));

        return CheckResult.correct();
    }


    // in progress
    int[] mines = {1, 2, 3, 4, 5};

    @DynamicTest(data = "mines")
    CheckResult GameBoardTest(final int minesCount) {
        final var program = new TestedProgram();
        program.start();
        final var output = program.execute(String.valueOf(minesCount));

        final var step = GameStep.parse(output);

        Assert.contains(output, "set/unset mines marks", "ask_coordinates");

        return CheckResult.correct();
    }

    private static String toMove(final int index, final boolean isMark) {
        return String.format("%d %d %s", 1 + index / GameStep.SIZE, 1 + index % GameStep.SIZE, isMark ? "mine" : "free");
    }

    private static String getRandomFreeMove() {
        return String.format("%d %d free", 1 + random.nextInt(9), 1 + random.nextInt(9));
    }
}