package RU.MEPHI.ICIS.C17501.messenger.db.dao;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

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
    @Column(columnDefinition = "bigserial")
    private Long id_message;
    @Column(nullable = false, length = 1000)
    private String text;
    @Column(nullable = false)
    private Long id_chat;
    @Column(nullable = false)
    @UpdateTimestamp
    private Date LastChangesDate;
    @Column( length = 20)
    private String telephoneNumber;
    private Boolean getIsDeleted;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "telephoneNumber",insertable = false,updatable = false)
    private User user;

}
