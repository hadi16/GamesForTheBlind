package gamesforblind.loader.action;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Action for when the user wishes to exit from the loader screen. The body of this class is empty
 * because the method to receive the action just needs to check for "instanceof" the passed action.
 */
@XmlRootElement
public class LoaderExitAction extends LoaderAction {
    @Override
    public XmlAdapter getJaxbAdapter() {
        return null;
    }
}
