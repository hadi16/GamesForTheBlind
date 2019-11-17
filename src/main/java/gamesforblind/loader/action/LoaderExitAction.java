package gamesforblind.loader.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action for when the user wishes to exit from the loader screen. The body of this class is empty
 * because the method to receive the action just needs to check for "instanceof" the passed action.
 */
@XmlRootElement(name = "LoaderExitAction")
@XmlAccessorType(XmlAccessType.NONE)
public class LoaderExitAction extends LoaderAction {
}
