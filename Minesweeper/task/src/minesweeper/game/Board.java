package minesweeper.game;

import java.util.Arrays;

public class Board {
    public static final int SIZE = 9;
    public static final int CELLS = SIZE * SIZE;

    private final CellState[] state = new CellState[CELLS];

    public Board() {
        Arrays.fill(state, CellState.UNEXPLORED);
    }

    @Override
    public String toString() {
        final var output = new StringBuilder()
                .append(" │123456789│").append(System.lineSeparator())
                .append("—│—————————│").append(System.lineSeparator());
        int index = 0;
        for (int row = 1; row <= SIZE; ++row) {
            output.append(row).append('│');
            for (int col = 1; col <= SIZE; ++col) {
                output.append(state[index++].getSymbol());
            }
            output.append('│').append(System.lineSeparator());
        }
        return output.append("—│—————————│").toString();
    }
}