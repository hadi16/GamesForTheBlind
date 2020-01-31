package gamesforblind.codebreaker.gui.listener;

import gamesforblind.codebreaker.action.CodebreakerAction;
import gamesforblind.enums.ArrowKeyDirection;

public class CodebreakerArrowKeyAction extends CodebreakerAction {
    private final ArrowKeyDirection arrowKeyDirection;

    public CodebreakerArrowKeyAction(ArrowKeyDirection arrowKeyDirection) {
        this.arrowKeyDirection = arrowKeyDirection;
    }

    public ArrowKeyDirection getArrowKeyDirection() {
        return this.arrowKeyDirection;
    }
}
