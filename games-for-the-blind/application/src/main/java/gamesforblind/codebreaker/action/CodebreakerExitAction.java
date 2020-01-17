package gamesforblind.codebreaker.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action that is sent when a user wishes to exit.
 */
@XmlRootElement(name = "CodebreakerExitAction")
@XmlAccessorType(XmlAccessType.NONE)
public class CodebreakerExitAction extends CodebreakerAction {
}
