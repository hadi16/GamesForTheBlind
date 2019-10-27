package gamesforblind.loader.gui.listener;

import gamesforblind.loader.GameLoader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoaderActionListener extends LoaderListener implements ActionListener {
    public LoaderActionListener(GameLoader gameLoader) {
        super(gameLoader);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.sendActionBasedOnButtonText(e.getActionCommand());
    }
}
