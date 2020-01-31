package gamesforblind.codebreaker.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Action that is sent when the user requests that the instructions for Codebreaker are read.
 */
@XmlRootElement(name = "CodebreakerInstructionsAction")
@XmlAccessorType(XmlAccessType.NONE)
public class CodebreakerInstructionsAction extends CodebreakerAction{

}
