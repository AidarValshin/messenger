package RU.MEPHI.ICIS.C17501.messenger.db.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "users")
@Entity
public class User  {

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
    @Column( length = 200)
    private String photoUrl;
    @Column(nullable = false)
    private Boolean isDeleted;
    @Column(nullable = false)
    private Boolean isLocked;
    @Column(nullable = false,length =1 )
    private String gender;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "login",insertable = false,updatable = false)
    @ToString.Exclude
    private UserCredentials userCredentials;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "telephoneNumber", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<RoleUser> rolesOfUserSet;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "telephoneNumber", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Message> messagesSet;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "telephoneNumber", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<ChatContact> chatContactsSet;
}
