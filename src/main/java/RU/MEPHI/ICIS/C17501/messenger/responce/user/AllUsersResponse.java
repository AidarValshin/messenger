package RU.MEPHI.ICIS.C17501.messenger.responce.user;

import RU.MEPHI.ICIS.C17501.messenger.db.dto.UserDTO;
import RU.MEPHI.ICIS.C17501.messenger.responce.Response;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AllUsersResponse extends Response {
    @JsonProperty(value = "members", required = true)
    private final List<UserDTO> userDTOS;

    public AllUsersResponse(String message, String result, List<UserDTO> userDTOS) {
        super(message, result);
        this.userDTOS = userDTOS;
    }
}
