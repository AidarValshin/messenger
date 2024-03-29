package RU.MEPHI.ICIS.C17501.messenger.db.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "chats")
@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigserial", name = "id_chat")
    private Long idChat;
    @Column(length = 200, name = "name")
    private String chatName;
    @Column(length = 200)
    private String photoUrl;
    private Date lastMessageDate;
    @Column(name = "last_message_id")
    private Long lastMessageId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "last_message_id", insertable = false, updatable = false)
    @ToString.Exclude
    private Message lastMessage;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idChat", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Message> messagesSet;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idChat", cascade = CascadeType.ALL)
    @ToString.Exclude
    Set<ChatContact> chatContacts;
}
