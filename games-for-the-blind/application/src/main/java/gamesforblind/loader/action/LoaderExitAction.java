package gamesforblind.loader.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action for when the user wishes to exit from the loader screen.
 */
@XmlRootElement(name = "LoaderExitAction")
@XmlAccessorType(XmlAccessType.NONE)
public class LoaderExitAction extends LoaderAction {
}
