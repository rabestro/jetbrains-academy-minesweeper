package minesweeper;

import java.util.Scanner;

public class UI {
    private static final Scanner scanner = new Scanner(System.in);

    public Suggestion askSuggestion() {
        System.out.print("Set/unset mines marks or claim a cell as free: ");
        return new Suggestion(scanner.nextInt(), scanner.nextInt(), scanner.next());
    }
 }
