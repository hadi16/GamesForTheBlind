package gamesforblind.codebreaker.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action that is sent when the user requests that a previous row is read
 */
@XmlRootElement(name = "CodebreakerReadBackAction")
@XmlAccessorType(XmlAccessType.NONE)
public class CodebreakerReadBackAction extends CodebreakerAction {
}
