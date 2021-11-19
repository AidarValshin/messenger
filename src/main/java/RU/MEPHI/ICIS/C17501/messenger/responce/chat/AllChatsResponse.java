package RU.MEPHI.ICIS.C17501.messenger.responce.chat;

import RU.MEPHI.ICIS.C17501.messenger.db.dto.ChatDTO;
import RU.MEPHI.ICIS.C17501.messenger.responce.Response;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AllChatsResponse extends Response {
    @JsonProperty(value = "streams", required = true)
    private final List<ChatDTO> chatDTOS;

    public AllChatsResponse(String message, String result, List<ChatDTO> chatDTOS) {
        super(message, result);
        this.chatDTOS = chatDTOS;
    }
}
