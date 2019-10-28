package gamesforblind.loader.adapter;

import gamesforblind.loader.action.LoaderUnrecognizedKeyAction;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LoaderUnrecognizedKeyActionAdapter
        extends XmlAdapter<AdaptedLoaderUnrecognizedKeyAction, LoaderUnrecognizedKeyAction> {
    @Override
    public LoaderUnrecognizedKeyAction unmarshal(AdaptedLoaderUnrecognizedKeyAction adapted) {
        return new LoaderUnrecognizedKeyAction(adapted.getKeyCode());
    }

    @Override
    public AdaptedLoaderUnrecognizedKeyAction marshal(LoaderUnrecognizedKeyAction action) {
        AdaptedLoaderUnrecognizedKeyAction adapted = new AdaptedLoaderUnrecognizedKeyAction();
        adapted.setKeyCode(action.getKeyCode());
        return adapted;
    }
}
