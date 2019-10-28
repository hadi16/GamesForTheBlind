package gamesforblind.loader.adapter;

import gamesforblind.loader.enums.SelectedGame;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
class AdaptedLoaderGameSelectionAction {
    @XmlElement
    private SelectedGame selectedGame;

    public SelectedGame getSelectedGame() {
        return this.selectedGame;
    }

    public void setSelectedGame(SelectedGame selectedGame) {
        this.selectedGame = selectedGame;
    }
}
