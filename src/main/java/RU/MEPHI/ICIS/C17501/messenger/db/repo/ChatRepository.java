package RU.MEPHI.ICIS.C17501.messenger.db.repo;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    /*
        @Query(value="select c.id_chat as streamId," +
                "       c.name as name," +
                "       c.chat_id as streamId," +
                "       c.last_message_id as lastMessageId," +
                "       c.photo_url as photoUrl," +
                "       c.last_message_date as lastMessageDate," +
                "           "
                "from messenger.chats c left join  messenger.roles_users_mapping m on u.telephone_number=m.telephone_number" +
                " left join  messenger.roles r on m.id_role=r.id_role",nativeQuery = true)
    public List<ChatProjection> findAllByProjection(String telephoneNumber);
     */
    List<Chat> findAll();

    List<Chat> findAllByIdChatIn(Collection<Long> chats, Pageable pageable);

    List<Chat> findAllByChatName(String chatName);

}
