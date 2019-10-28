package gamesforblind.loader.adapter;

import gamesforblind.loader.action.LoaderSudokuSelectionAction;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LoaderSudokuSelectionActionAdapter
        extends XmlAdapter<AdaptedLoaderSudokuSelectionAction, LoaderSudokuSelectionAction> {
    @Override
    public LoaderSudokuSelectionAction unmarshal(AdaptedLoaderSudokuSelectionAction adapted) {
        return new LoaderSudokuSelectionAction(adapted.getSudokuType());
    }

    @Override
    public AdaptedLoaderSudokuSelectionAction marshal(LoaderSudokuSelectionAction action) {
        AdaptedLoaderSudokuSelectionAction adapted = new AdaptedLoaderSudokuSelectionAction();
        adapted.setSudokuType(action.getSudokuType());
        return adapted;
    }
}
