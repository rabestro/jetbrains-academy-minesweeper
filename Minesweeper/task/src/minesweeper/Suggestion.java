package minesweeper;

public final class Suggestion {
    private final int index;
    private final State state;

    public Suggestion(int x, int y, String state) {
        this.index = (y - 1) * Field.SIZE + (x - 1);
        this.state = State.valueOf(state.toUpperCase());
    }

    public int getIndex() {
        return index;
    }

    public State getState() {
        return state;
    }
}
