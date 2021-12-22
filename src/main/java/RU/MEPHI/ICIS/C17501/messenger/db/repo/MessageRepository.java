package RU.MEPHI.ICIS.C17501.messenger.db.repo;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {


    List<Message> findAllByTelephoneNumberEquals(String requesterTelephoneNumber);

    List<Message> findAllByIdChat(Long idChat);

    /*@Query
    List<Message> find getAllMessagesForClient(String requesterTelephoneNumber);*/
}
