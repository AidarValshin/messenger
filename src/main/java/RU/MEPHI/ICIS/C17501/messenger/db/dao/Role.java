package RU.MEPHI.ICIS.C17501.messenger.db.dao;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "roles")
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Integer id_role;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id_role", cascade = CascadeType.ALL)
    private Set<RoleUser> usersOfRoleSet;
}
