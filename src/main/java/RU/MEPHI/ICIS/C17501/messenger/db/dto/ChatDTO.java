package RU.MEPHI.ICIS.C17501.messenger.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ChatDTO {
    @JsonProperty("avatar_url")
    private String photoUrl;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("last_message_date")
    private java.util.Date lastMessageDate;
    @Column(name = "last_message_id")
    private Long lastMessageId;
    @Column(name = "is_subscriber")
    private Boolean isSubscriber;
}
