package RU.MEPHI.ICIS.C17501.messenger.db.dao;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "chat_contacts")
@Entity
public class ChatContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigserial")
    private Long idChatContact;
    @Column(nullable = false, name = "id_chat")
    private Long idChat;
    @Column(nullable = false, length = 20)
    private String telephoneNumber;
    @Column(name = "id_message")
    private Long lastMessageId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "id_chat", insertable = false, updatable = false)
    @ToString.Exclude
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "telephoneNumber", insertable = false, updatable = false, nullable = false)
    @ToString.Exclude
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_message", insertable = false, updatable = false)
    @ToString.Exclude
    private Message lastMessage;


}
