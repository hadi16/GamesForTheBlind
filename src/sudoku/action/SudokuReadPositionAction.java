package sudoku.action;

public class SudokuReadPositionAction extends SudokuAction{
    private final String type;

    public SudokuReadPositionAction(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }
}

