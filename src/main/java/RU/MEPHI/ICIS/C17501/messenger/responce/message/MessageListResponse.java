package RU.MEPHI.ICIS.C17501.messenger.responce.message;

import RU.MEPHI.ICIS.C17501.messenger.db.dto.OutgoingMessageDTO;
import RU.MEPHI.ICIS.C17501.messenger.responce.Response;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageListResponse extends Response implements Serializable {

    @JsonProperty(value = "anchor", required = true)
    private Long anchor;

    @JsonProperty(value = "messages", required = true)
    private List<OutgoingMessageDTO> outgoingMessageDTOList;

}
