package RU.MEPHI.ICIS.C17501.messenger.db.dao;

import javax.persistence.Column;
import java.io.Serializable;

public class RoleUserPK implements Serializable {
    @Column(nullable = false)
    private Long id_role;

    @Column( length = 20)
    private String telephoneNumber;
}
