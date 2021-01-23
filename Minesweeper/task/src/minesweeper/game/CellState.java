package minesweeper.game;

public enum CellState {
    ZERO('/'),
    ONO('1'),
    TWO('2'),
    TREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    MINE('X'),
    MARK('*'),
    UNEXPLORED('.');
    private final char symbol;

    CellState(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}
