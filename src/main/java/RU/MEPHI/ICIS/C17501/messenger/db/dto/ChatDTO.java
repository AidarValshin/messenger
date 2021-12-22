package RU.MEPHI.ICIS.C17501.messenger.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ChatDTO {

    @JsonProperty(value = "avatar_url")
    private String photoUrl;

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "last_message_date")
    private java.util.Date lastMessageDate;

    @JsonProperty(value = "last_message_id")
    private Long lastMessageId;

    @JsonProperty(value = "is_subscriber")
    private Boolean isSubscriber;
}
