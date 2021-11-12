package RU.MEPHI.ICIS.C17501.messenger.db.dao;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "users")
@Entity
public class User {

    @Id
    @Column( length = 20)
    private String telephoneNumber;
    @Column(nullable = false, length = 20)
    private String login;
    @Column(nullable = false, length = 20)
    private String firstName;
    @Column(nullable = false, length = 20)
    private String secondName;
    @Column(nullable = false)
    private Date dateOfBirth;
    @Column(nullable = false)
    private Long idPhoto;
    @Column(nullable = false, length = 30)
    private String photoUrl;
    @Column(nullable = false)
    private Boolean isDeleted;
    @Column(nullable = false)
    private Boolean Locked;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "login",insertable = false,updatable = false)
    private UserCredentials userCredentials;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "telephoneNumber", cascade = CascadeType.ALL)
    private Set<RoleUser> rolesOfUserSet;



}
