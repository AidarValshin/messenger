package RU.MEPHI.ICIS.C17501.messenger.responce.user;

import RU.MEPHI.ICIS.C17501.messenger.db.dto.UserDTO;
import RU.MEPHI.ICIS.C17501.messenger.responce.Response;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserResponse extends Response {
    @JsonProperty(value = "user", required = true)
    private final UserDTO userDTO;

    public UserResponse(String message, String result, UserDTO userDTO) {
        super(message, result);
        this.userDTO = userDTO;
    }
}
