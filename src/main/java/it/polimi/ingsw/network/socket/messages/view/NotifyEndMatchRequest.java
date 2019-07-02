package it.polimi.ingsw.network.socket.messages.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.common.StandingsItem;
import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.network.socket.messages.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Request a notifyEndMatch.
 */
public class NotifyEndMatchRequest extends RequestViewMethod {
    /**
     * Final standings.
     */
    private ArrayList<StandingsItem> standings;

    /**
     * Create a new request for notifyEndMatch method.
     * @param standings Final standings.
     */
    @JsonCreator
    public NotifyEndMatchRequest(@JsonProperty("standings") List<StandingsItem> standings) {
        this.standings = new ArrayList<>(standings);
    }

    /**
     * Command pattern. Invoke the notifyEndMatch on the given view.
     * @param view Method is invoked on this view.
     * @return VoidResponse.
     */
    @Override
    public Message invokeOn(LocalView view) {
        view.notifyEndMatch(standings);
        return new VoidResponse(this);
    }
}
