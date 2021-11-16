package RU.MEPHI.ICIS.C17501.messenger.db.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(columnDefinition = "serial", name = "id_role")
    private Long idRole;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;


    @JsonIgnore
    @ManyToMany(cascade = CascadeType.REFRESH,mappedBy = "roles")
    @ToString.Exclude
    Set<User> users;
}
