package gamesforblind.sudoku.action;

import java.time.LocalDateTime;

public abstract class SudokuAction {
    private final LocalDateTime localDateTime;

    protected SudokuAction() {
        this.localDateTime = LocalDateTime.now();
    }

    public LocalDateTime getLocalDateTime() {
        return this.localDateTime;
    }
}
