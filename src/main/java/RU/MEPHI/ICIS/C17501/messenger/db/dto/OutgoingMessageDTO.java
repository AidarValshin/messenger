package RU.MEPHI.ICIS.C17501.messenger.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OutgoingMessageDTO implements Serializable {

    @JsonProperty(value = "content")
    private String content;

    @JsonProperty(value = "message_id")
    private Long messageId;

    @JsonProperty(value = "sender_full_name")
    private String senderFullName;

    @JsonProperty(value = "sender_id")
    private String senderId;  // // помним, что senderId = telephoneNumber

    @JsonProperty(value = "timestamp")
    Long timestamp;
}
