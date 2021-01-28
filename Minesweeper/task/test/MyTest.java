import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import java.util.Random;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import static java.util.stream.IntStream.range;

public class MyTest extends StageTest {
    private static final Random random = new Random();
    private static final String MINES_70 = "70";
    private static final String ONE_MINE = "1";
    private static final boolean SUGGEST_FREE = false;
    private static final boolean MARK_MINE = true;

    private static final Function<GameStep, String> REGULAR_PLAYER = step -> {
        final var mineIndex = step.freeIndexes().filter(step::isSureMine).findFirst();
        return mineIndex.isPresent()
                ? toMove(mineIndex.getAsInt(), MARK_MINE)
                : toMove(step.getRandomFreeIndex(), SUGGEST_FREE);
    };

    private static final Function<GameStep, String> RECREATIONAL_PLAYER =
            step -> toMove(step.getRandomFreeIndex(), SUGGEST_FREE);

    int[] mines = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25, 30, 40, 50, 60, 70};

    private static CheckResult checkStrategy(final int minesCount, final Function<GameStep, String> strategy) {
        final var game = new TestedProgram();
        game.start();
        var step = GameStep.parse(game.execute(String.valueOf(minesCount)));
        while (step.isPlaying()) {
            step = GameStep.parse(game.execute(strategy.apply(step)));
        }
        return checkFinalBoard(step, minesCount);
    }

    private static CheckResult checkFinalBoard(final GameStep step, final int minesCount) {
        if (step.isWin()) {
            Assert.that(step.count('*') == minesCount || step.count('.') == minesCount, "dot_not_equals_mines");
        } else if (step.isFailed()) {
            Assert.that(step.count('x') == minesCount, "x_not_equals_mines");
        } else {
            Assert.error("unexpected_error");
        }
        return CheckResult.correct();
    }

    @DynamicTest(order = 0)
    CheckResult firstQuestionShouldAskMineCount() {
        final var program = new TestedProgram();
        Assert.contains(program.start(), "how many mines", "first_question");
        return CheckResult.correct();
    }

    @DynamicTest(repeat = 10, order = 1)
    CheckResult firstFreeMoveShouldBySafe() {
        final var program = new TestedProgram();
        program.start();
        program.execute(MINES_70);
        final var output = program.execute(getRandomFreeMove());
        final var game = GameStep.parse(output);

        Assert.that(!game.isFailed(), "first_free_move");
        return CheckResult.correct();
    }

    @DynamicTest(order = 2)
    CheckResult oneMineGameTest() {
        final var game = new TestedProgram();
        game.start();
        game.execute(ONE_MINE);
        Assert.that(allIndexes().noneMatch(isMineFound(game)), "no_mines_before_free_move");
        return CheckResult.correct();
    }

    @DynamicTest(repeat = 10, order = 3)
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

    @DynamicTest(data = "mines", repeat = 2, order = 4)
    CheckResult dummyPlayerTest(final int minesCount) {
        return checkStrategy(minesCount, RECREATIONAL_PLAYER);
    }

    @DynamicTest(data = "mines", repeat = 2, order = 5)
    CheckResult regularPlayerTest(final int minesCount) {
        return checkStrategy(minesCount, REGULAR_PLAYER);
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