package gamesforblind.mastermind.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action that is sent when a user wishes to exit.
 */
@XmlRootElement(name = "MastermindExitAction")
@XmlAccessorType(XmlAccessType.NONE)
public class MastermindExitAction extends MastermindAction {
}
