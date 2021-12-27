package RU.MEPHI.ICIS.C17501.messenger.db.projection;


import java.sql.Date;
import java.util.Set;

public interface UserProjection {
    String getPhotoUrl();

    String getTelephoneNumber();

    String getFirstName();

    String getSecondName();

    String getRoleName();

    Boolean getIsBlocked();

    Boolean getIsDeleted();

    String getLogin();

    Date getDateOfBirth();

    String getGender();
}
