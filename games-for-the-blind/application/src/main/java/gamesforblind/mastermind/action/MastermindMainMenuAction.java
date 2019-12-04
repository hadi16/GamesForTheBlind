package gamesforblind.mastermind.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action that is sent when a user wishes to return to the main menu.
 */
@XmlRootElement(name = "MastermindMainMenuAction")
@XmlAccessorType(XmlAccessType.NONE)
public class MastermindMainMenuAction extends MastermindAction {
}
