package gamesforblind.loader.adapter;

import gamesforblind.loader.enums.ArrowKeyDirection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
class AdaptedLoaderArrowKeyAction {
    @XmlElement
    private ArrowKeyDirection arrowKeyDirection;

    public ArrowKeyDirection getArrowKeyDirection() {
        return this.arrowKeyDirection;
    }

    public void setArrowKeyDirection(ArrowKeyDirection arrowKeyDirection) {
        this.arrowKeyDirection = arrowKeyDirection;
    }
}
