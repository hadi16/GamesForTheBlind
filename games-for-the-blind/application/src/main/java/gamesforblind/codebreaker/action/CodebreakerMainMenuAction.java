package gamesforblind.codebreaker.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action that is sent when a user wishes to return to the main menu.
 */
@XmlRootElement(name = "CodebreakerMainMenuAction")
@XmlAccessorType(XmlAccessType.NONE)
public class CodebreakerMainMenuAction extends CodebreakerAction {
}
