package RU.MEPHI.ICIS.C17501.messenger.db.repo;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByTelephoneNumberEquals(String requesterTelephoneNumber);

    List<Message> findAllByIdChat(Long idChat);

    @Query(value = "WITH anchor_message AS (\n" +
            "\t\tSELECT m0.id_message,\n" +
            "\t\t\t\tm0.is_deleted,\n" +
            "\t\t\t\tm0.date,\n" +
            "\t\t\t\tm0.id_chat,\n" +
            "\t\t\t\tm0.last_changes_date,\n" +
            "\t\t\t\tm0.telephone_number,\n" +
            "\t\t\t\tm0.text\n" +
            "\t\tFROM messenger.messages AS m0\n" +
            "\t\tWHERE m0.id_message= :anchorId AND m0.id_chat= :chatId)\n" +
            "\t(SELECT searchDataBefore.id_message,\n" +
            "\t \t\tsearchDataBefore.is_deleted,\n" +
            "\t \t\tsearchDataBefore.date,\n" +
            "\t\t\tsearchDataBefore.id_chat,\n" +
            "\t \t\tsearchDataBefore.last_changes_date,\n" +
            "\t\t\tsearchDataBefore.telephone_number,\n" +
            "\t\t\tsearchDataBefore.text \n" +
            "\t FROM messenger.messages AS searchDataBefore\n" +
            "\t CROSS JOIN anchor_message\n" +
            "\t WHERE searchDataBefore.date < anchor_message.date AND searchDataBefore.id_chat=:chatId\n" +
            "\t ORDER BY searchDataBefore.date desc LIMIT :numBefore)\n" +
            "\t UNION \n" +
            "\t (SELECT searchDataAfter.id_message,\n" +
            "\t \t\tsearchDataAfter.is_deleted,\n" +
            "\t \t\tsearchDataAfter.date,\n" +
            "\t\t\tsearchDataAfter.id_chat,\n" +
            "\t \t\tsearchDataAfter.last_changes_date,\n" +
            "\t\t\tsearchDataAfter.telephone_number,\n" +
            "\t\t\tsearchDataAfter.text\n" +
            "\t  FROM messenger.messages AS searchDataAfter\n" +
            "\t  CROSS JOIN anchor_message\n" +
            "\t  WHERE searchDataAfter.date > anchor_message.date AND searchDataAfter.id_chat=:chatId\n" +
            "\t  LIMIT :numAfter)\n" +
            "\t  UNION\n" +
            "\t  (SELECT anchor.id_message,\n" +
            "\t \t\tanchor.is_deleted,\n" +
            "\t \t\tanchor.date,\n" +
            "\t\t\tanchor.id_chat,\n" +
            "\t \t\tanchor.last_changes_date,\n" +
            "\t\t\tanchor.telephone_number,\n" +
            "\t\t\tanchor.text\n" +
            "\t   FROM anchor_message AS anchor)\n" +
            "\t   ORDER BY date;", nativeQuery = true)
    public List<Message> findAllInChatByWindow(Long chatId, Long anchorId, Long numBefore, Long numAfter);

    Page<Message> findAllByIdChat(Long chatId, Pageable pageable);

    List<Message> findByIdChatAndTextContainingOrderByLastChangesDate(Long chatId, String keyword);
}
