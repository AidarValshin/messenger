package RU.MEPHI.ICIS.C17501.messenger.db.dao;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "roles_users_mapping")
@Entity
@IdClass(RoleUserPK.class)
public class RoleUser {
    @Id
    @Column(nullable = false)
    private Integer id_role;
    @Id
    @Column( length = 20)
    private String telephoneNumber;

    @OneToOne(fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    @JoinColumn(name="id_role" , insertable = false, updatable = false )
    private Role role;

    @OneToOne(fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    @JoinColumn(name="telephoneNumber" , insertable = false, updatable = false )
    private User user;
}
