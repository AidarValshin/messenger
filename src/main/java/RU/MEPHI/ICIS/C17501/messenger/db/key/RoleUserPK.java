package RU.MEPHI.ICIS.C17501.messenger.db.key;

import javax.persistence.Column;
import java.io.Serializable;

public class RoleUserPK implements Serializable {
    @Column(nullable = false)
    private Long idRole;

    @Column( length = 20)
    private String telephoneNumber;
}
