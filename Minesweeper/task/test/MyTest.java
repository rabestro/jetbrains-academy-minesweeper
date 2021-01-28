import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import java.util.Random;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import static java.util.stream.IntStream.range;

public class MyTest extends StageTest {
    private static final Random random = new Random();
    private static final String MINES_70 = "70";
    private static final String ONE_MINE = "1";
    private static final boolean SUGGEST_FREE = false;
    private static final boolean MARK_MINE = true;

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
    CheckResult oneMineGameTest() {
        final var game = new TestedProgram();
        game.start();
        game.execute(ONE_MINE);
        Assert.that(allIndexes().noneMatch(isMineFound(game)), "no_mines_before_free_move");
        return CheckResult.correct();
    }

    @DynamicTest(repeat = 10)
    CheckResult oneMineWinBySuggestingEachFieldTest() {
        final var game = new TestedProgram();
        game.start();
        game.execute(ONE_MINE);

        final var firstStep = GameStep.parse(game.execute(getRandomFreeMove()));

        if (firstStep.isWin()) {
            return CheckResult.correct();
        }
        Assert.that(firstStep.isPlaying(), "first_free_move");

        firstStep
                .freeIndexes()
                .filter(isMineFound(game))
                .findFirst()
                .orElseThrow(() -> Assert.error("no_mine_found"));

        return CheckResult.correct();
    }

    IntPredicate isMineFound(final TestedProgram program) {
        return index -> {
            var game = GameStep.parse(program.execute(toMove(index, MARK_MINE)));
            Assert.that(game.count('*') == 1, "expect_one_asterisk");
            if (game.isWin()) {
                return true;
            }
            Assert.that(game.isPlaying(), "no_failed_after_mark");
            game = GameStep.parse(program.execute(toMove(index, MARK_MINE)));
            Assert.that(game.count('*') == 0, "expect_no_asterisk");
            Assert.that(game.isPlaying(), "expected_playing");
            return false;
        };
    }

    int[] mines = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25, 30, 40, 50, 60, 70};

    @DynamicTest(data = "mines", repeat = 2)
    CheckResult dummyPlayerTest(final int minesCount) {
        final var game = new TestedProgram();
        game.start();
        var step = GameStep.parse(game.execute(String.valueOf(minesCount)));
        while (step.isPlaying()) {
            step = GameStep.parse(toMove(step.getRandomFreeIndex(), SUGGEST_FREE));
        }
        if (step.isWin()) {
            Assert.that(step.count('.') == minesCount, "dot_not_equals_mines");
        } else if (step.isFailed()) {
            Assert.that(step.count('x') == minesCount, "x_not_equals_mines");
        } else {
            Assert.error("unexpected_error");
        }
        return CheckResult.correct();
    }

    private static String toMove(final int index, final boolean isMark) {
        return String.format("%d %d %s", 1 + index % GameStep.SIZE, 1 + index / GameStep.SIZE, isMark ? "mine" : "free");
    }

    private static String getRandomFreeMove() {
        return String.format("%d %d free", 1 + random.nextInt(9), 1 + random.nextInt(9));
    }

    static IntStream allIndexes() {
        return range(0, GameStep.CELLS_COUNT);
    }
}