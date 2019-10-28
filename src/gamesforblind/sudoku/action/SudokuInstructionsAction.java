package gamesforblind.sudoku.action;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

@XmlRootElement
public class SudokuInstructionsAction extends SudokuAction {
    @Override
    public XmlAdapter getJaxbAdapter() {
        return null;
    }
}
