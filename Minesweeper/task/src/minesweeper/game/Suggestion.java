package minesweeper.game;

public final class Suggestion {
    private final int index;
    private final Type state;

    public Suggestion(int x, int y, String state) {
        this.index = (y - 1) * Game.SIZE + (x - 1);
        this.state = Type.valueOf(state.toUpperCase());
    }

    public int getIndex() {
        return index;
    }

    public Type getState() {
        return state;
    }

    enum Type {
        FREE, MINE
    }
}
