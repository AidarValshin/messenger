package RU.MEPHI.ICIS.C17501.messenger.db.dao;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "user_credentials")
@Entity
public class UserCredentials {
    @Id
    @Column(length = 20)
    private String login;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false, length = 500)
    private String token;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "login", insertable = false, updatable = false)
    @ToString.Exclude
    private User user;


}
