package RU.MEPHI.ICIS.C17501.messenger.db.repo;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.ChatContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatContactRepository extends JpaRepository<ChatContact, Long> {
    List<ChatContact> findAllByTelephoneNumber(String telephoneNumber);

    List<ChatContact> findByTelephoneNumberAndIdChat(String telephoneNumber, Long chatId);


}
