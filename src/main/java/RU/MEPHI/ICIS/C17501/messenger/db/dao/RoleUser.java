package RU.MEPHI.ICIS.C17501.messenger.db.dao;

import RU.MEPHI.ICIS.C17501.messenger.db.key.RoleUserPK;
import lombok.*;

import javax.persistence.*;

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
    @Column(nullable = false, name = "id_role")
    private Long idRole;
    @Id
    @Column(nullable = false, length = 20)
    private String telephoneNumber;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idRole", insertable = false, updatable = false)
    @ToString.Exclude
    private Role role;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "telephoneNumber", insertable = false, updatable = false)
    @ToString.Exclude
    private User user;
}
