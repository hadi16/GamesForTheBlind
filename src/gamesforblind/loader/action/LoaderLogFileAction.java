package gamesforblind.loader.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action for when a user wishes to load a saved log file.
 */
@XmlRootElement(name = "LoaderLogFileAction")
@XmlAccessorType(XmlAccessType.NONE)
public class LoaderLogFileAction extends LoaderAction {
}
