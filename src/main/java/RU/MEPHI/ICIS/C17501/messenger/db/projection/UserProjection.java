package RU.MEPHI.ICIS.C17501.messenger.db.projection;


import java.sql.Date;
import java.util.Set;

public interface UserProjection {
    public String getPhotoUrl();

    public String getTelephoneNumber();

    public String getFirstName();

    public String getSecondName();

    public Set<String> getRoleName();

    public Boolean getIsBlocked();

    public Boolean getIsDeleted();

    public String getLogin();

    public Date getDateOfBirth();

    public String getGender();
}
