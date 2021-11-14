package RU.MEPHI.ICIS.C17501.messenger.db.dao;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "messages")
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigserial", name = "id_message")
    private Long idMessage;
    @Column(nullable = false, length = 1000)
    private String text;
    @Column(nullable = false)
    private Long idChat;
    @Column(nullable = false)
    @UpdateTimestamp
    private Date lastChangesDate;
    @CreationTimestamp
    private Date date;
    @Column(length = 20)
    private String telephoneNumber;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "telephoneNumber", insertable = false, updatable = false)
    @ToString.Exclude
    private User user;

}
