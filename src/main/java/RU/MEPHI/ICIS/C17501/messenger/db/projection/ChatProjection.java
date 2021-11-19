package RU.MEPHI.ICIS.C17501.messenger.db.projection;


import java.sql.Date;
import java.util.Set;

public interface ChatProjection {
    public String getName();
    public String getStreamId();
    public Boolean getIsSubscribed();
    public Long getLastMessageId();
    public String getPhotoUrl();
    public Date getLastMessageDate();
}
