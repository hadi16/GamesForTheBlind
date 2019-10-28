package gamesforblind.loader.adapter;

import gamesforblind.loader.action.LoaderGameSelectionAction;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LoaderGameSelectionActionAdapter
        extends XmlAdapter<AdaptedLoaderGameSelectionAction, LoaderGameSelectionAction> {
    @Override
    public LoaderGameSelectionAction unmarshal(AdaptedLoaderGameSelectionAction adapted) {
        return new LoaderGameSelectionAction(adapted.getSelectedGame());
    }

    @Override
    public AdaptedLoaderGameSelectionAction marshal(LoaderGameSelectionAction action) {
        AdaptedLoaderGameSelectionAction adapted = new AdaptedLoaderGameSelectionAction();
        adapted.setSelectedGame(action.getSelectedGame());
        return adapted;
    }
}
