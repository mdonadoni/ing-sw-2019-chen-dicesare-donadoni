package it.polimi.ingsw.network.socket.messages.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.network.socket.messages.Message;

/**
 * Response of method login.
 */
public class LoginResponse extends Message {
    /**
     * Result of login.
     */
    private boolean res;

    /**
     * Private constructor of LoginResponse used by Jackson.
     * @param uuid uuid of corresponding request.
     * @param res result of login method invocation.
     */
    @JsonCreator
    private LoginResponse(
            @JsonProperty("uuid") String uuid,
            @JsonProperty("res") boolean res) {
        super(uuid);
        this.res = res;
    }

    /**
     * Constructor of LoginResponse.
     * @param req Corresponding request of login.
     * @param res Result of login method invocation.
     */
    public LoginResponse(LoginRequest req, boolean res) {
        super(req.getUUID());
        this.res = res;
    }

    /**
     * Get the result of the login.
     * @return True if login is successful, false otherwise.
     */
    public boolean getResult() {
        return res;
    }

}
