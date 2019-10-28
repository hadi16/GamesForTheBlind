package gamesforblind.loader.adapter;

import gamesforblind.loader.action.LoaderArrowKeyAction;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LoaderArrowKeyActionAdapter
        extends XmlAdapter<AdaptedLoaderArrowKeyAction, LoaderArrowKeyAction> {
    @Override
    public LoaderArrowKeyAction unmarshal(AdaptedLoaderArrowKeyAction adapted) {
        return new LoaderArrowKeyAction(adapted.getArrowKeyDirection());
    }

    @Override
    public AdaptedLoaderArrowKeyAction marshal(LoaderArrowKeyAction action) {
        AdaptedLoaderArrowKeyAction adapted = new AdaptedLoaderArrowKeyAction();
        adapted.setArrowKeyDirection(action.getArrowKeyDirection());
        return adapted;
    }
}
