import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;


public class MyTest extends StageTest {
    @DynamicTest
    CheckResult test1() {
        return CheckResult.correct();
    }

    @DynamicTest
    CheckResult test2() {
        return CheckResult.wrong("Hello, World!");
    }
}