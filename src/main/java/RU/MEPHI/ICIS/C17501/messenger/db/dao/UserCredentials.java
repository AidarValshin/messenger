package RU.MEPHI.ICIS.C17501.messenger.db.dao;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "user_credentials")
@Entity
@Builder
public class UserCredentials {
    @Id
    @Column(length = 20)
    private String login;

    @Column(nullable = false, length = 50)
    @Length(max = 50, min = 10)
    private String password;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "login", insertable = false, updatable = false)
    @ToString.Exclude
    private User user;

}
